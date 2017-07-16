package com.atahmasebian.prefrences.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.atahmasebian.prefrences.R;
import com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag;
import com.atahmasebian.prefrences.fingerPrintUtility.FingerPrintFragment;
import com.atahmasebian.prefrences.fingerPrintUtility.FingerprintHandler;
import com.atahmasebian.prefrences.fingerPrintUtility.IFingerPrintHandlerView;
import com.atahmasebian.prefrences.pinCode.ConfirmPinCodeFragmentPinCode;
import com.atahmasebian.prefrences.pinCode.InsertInsertPinCodeFragment;
import com.atahmasebian.prefrences.preference.PreferencesSettingsActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class MainActivity extends AccountAuthenticatorActivity implements IFingerPrintHandlerView {

    public final static int VALIDATION_FINGERPRINT_REQUEST_CODE = 2;
    private static final String KEY_NAME = "androidHive";
    public static AccountManager mAccountManager;
    public static InsertInsertPinCodeFragment insertPinCodeFragment;
    public static ConfirmPinCodeFragmentPinCode confirmPinCodeFragment;
    public static Account staticAccount;
    public static Bundle userData = new Bundle();
    public final int VALIDATION_PinCode_REQUEST_CODE = 3;
    private KeyStore keyStore;
    private Cipher cipher;
    private FingerPrintFragment fingerPrintFragment;
    private boolean isAccountExist = true;
    private String username;
    private String authTokenType;
    private String password;
    private SecretKey secretKey;
    private byte[] iv;
    private byte[] byteCipherText;
    private FragmentTransaction transaction;
    private FragmentManager fm;
    private String pinCode;
    private Account account;

    public static boolean isPinCodeConfirm() {

        String pinCode = userData.getString("PinCode");
        if (pinCode == null) {
            pinCode = insertPinCodeFragment.getPinCode();
        }
        if (pinCode.isEmpty()) {
            pinCode = mAccountManager.getUserData(staticAccount, "PinCode");
        }
        String confirmPinCode = confirmPinCodeFragment.getConfirmPinCode();

        boolean isValid = pinCode.equals(confirmPinCode);
        return isValid;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_feature_main_layout);


        fingerPrintFragment = new FingerPrintFragment();
        insertPinCodeFragment = new InsertInsertPinCodeFragment();
        confirmPinCodeFragment = new ConfirmPinCodeFragmentPinCode();

        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        fingerPringStuff();


        mAccountManager = AccountManager.get(getBaseContext());

        if (mAccountManager.getAccounts().length == 0) {
            // AM Adding  account first
            username = getIntent().getStringExtra(AccountGeneralTag.ARG_ACCOUNT_NAME);
            authTokenType = getIntent().getStringExtra(AccountGeneralTag.ARG_AUTH_TYPE);
            password = getIntent().getStringExtra(AccountGeneralTag.PARAM_USER_PASS);
            userData = getIntent().getExtras().getBundle(AccountGeneralTag.KEY_USERDATA);
            isAccountExist = false;

        } else {
            // AM ready for login with pin code
            account = getIntent().getParcelableExtra(AccountGeneralTag.ACCOUNT_DTO);
            if (account == null) {
                authTokenType = getIntent().getStringExtra(AccountGeneralTag.ARG_AUTH_TYPE);
                account = findAccount();
            }
            staticAccount = account;
            username = account.name;
            password = mAccountManager.getPassword(account);
            userData.putString("PinCode", mAccountManager.getUserData(account, "PinCode"));
            authTokenType = account.type;
            isAccountExist = true;

        }

        if (account != null) {
            //login page with pinCode ui
            transaction = fm.beginTransaction();
            transaction.replace(R.id.outputFragment, confirmPinCodeFragment);
            transaction.commit();
            confirmPinCodeFragment.setShownConfirmLayout(false);
        }


    }

    public Account findAccount() {
        for (Account savedAccount : mAccountManager.getAccounts()) {
            if (TextUtils.equals(savedAccount.type, authTokenType)) {
                return savedAccount;
            }
        }
        return null;
    }

    void userSignIn() {

        if (!isAccountExist) {

            account = new Account(username, authTokenType);
            mAccountManager.setUserData(account, "PinCode", userData.getString("PinCode"));
            if (mAccountManager.addAccountExplicitly(account, password, userData)) {
                Intent intent = new Intent();
                intent.putExtras(getIntent().getExtras());
                setAccountAuthenticatorResult(getIntent().getExtras());
                setResult(RESULT_OK, intent);
                finish();

            } else {
                //show error msg
            }

        } else {
            setResult(VALIDATION_PinCode_REQUEST_CODE);
            finish();
        }

    }

    public void onClick(View view) {
        int id = view.getId();


        switch (id) {
            case R.id.fingerPrintCancelBtn:
                setResult(VALIDATION_FINGERPRINT_REQUEST_CODE);
                finish();
                break;
            case R.id.pinCodeCancelBtn:
                setResult(VALIDATION_FINGERPRINT_REQUEST_CODE);
                finish();
                break;
            case R.id.pinCodeInsertBtn:
                transaction = fm.beginTransaction();
                transaction.replace(R.id.outputFragment, confirmPinCodeFragment);
                transaction.commit();
                userData.putString("PinCode", insertPinCodeFragment.getPinCode());

                break;
            case R.id.confirmBtn:
                //set pin code user data to bundle AM
                if (isPinCodeConfirm()) {
                    userSignIn();
                } else {
                    Toast.makeText(this, "pin code is not equal", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void fingerPringStuff() {


        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        //Device does not support fingerPrint Hardware
        if (!fingerprintManager.isHardwareDetected()) {

            transaction.replace(R.id.outputFragment, insertPinCodeFragment);

        } else {
            //Device Has fingerPrint Hardware

            transaction.replace(R.id.outputFragment, fingerPrintFragment);

            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                // textView.setText("Fingerprint authentication permission not enabled");
            } else {

                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    transaction = fm.beginTransaction();
                    transaction.replace(R.id.outputFragment, insertPinCodeFragment);

                    //  textView.setText("Register at least one fingerprint in Settings");
                } else {

                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {

                        //   textView.setText("Lock screen security not enabled in Settings");
                    } else {
                        generateKey();


                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(this, this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }
        transaction.commit();


    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();


        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

        try {
            encryptData();
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        try {
            decryptData();
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return true;


    }

    private String decryptData() throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher aesCipherForDecryption = null; // Must specify the mode explicitly as most JCE providers default to ECB mode!!
        aesCipherForDecryption = Cipher.getInstance("AES/CBC/PKCS7PADDING");

        aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] byteDecryptedText = new byte[0];
        byteDecryptedText = aesCipherForDecryption.doFinal(byteCipherText);
        String strDecryptedText = new String(byteDecryptedText);

        return strDecryptedText;
    }

    private void encryptData() throws BadPaddingException, IllegalBlockSizeException {

        //added pin code encryption
        String strDataToEncrypt = "1234 ";
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
        byteCipherText = new byte[0];
        byteCipherText = cipher.doFinal(byteDataToEncrypt);
        final int AES_KEYLENGTH = 128;    // change this as desired for the security level you want
        iv = new byte[AES_KEYLENGTH / 8];    // Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later

    }

    @Override
    public void showErrorMessage() {
        fingerPrintFragment.showErrorMessage();
    }

    @Override
    public void showCompletedMessage() {
        userSignIn();
        fingerPrintFragment.doLogin();
    }
}
