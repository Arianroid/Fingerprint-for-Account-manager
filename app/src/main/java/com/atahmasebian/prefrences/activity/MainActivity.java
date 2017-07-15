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
import android.util.Log;
import android.view.View;

import com.atahmasebian.prefrences.R;
import com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag;
import com.atahmasebian.prefrences.fingerPrintUtility.FingerprintHandler;
import com.atahmasebian.prefrences.fingerPrintUtility.IFingerPrintHandlerView;
import com.atahmasebian.prefrences.fragment.FingerPrintFragment;

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
    private KeyStore keyStore;
    private Cipher cipher;
    private FingerPrintFragment fingerPrintFragment;

    private AccountManager mAccountManager;
    private boolean isAccountExist = true;
    private String username;
    private String authTokenType;
    private String password;
    private Bundle userData;
    private SecretKey secretKey;
    private byte[] iv;
    private byte[] byteCipherText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_feature_main_layout);

        fingerPrintFragment = new FingerPrintFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.outputFragment, fingerPrintFragment);
        transaction.commit();

        mAccountManager = AccountManager.get(getBaseContext());


        if (mAccountManager.getAccounts().length == 0) {

            username = getIntent().getStringExtra(AccountGeneralTag.ARG_ACCOUNT_NAME);
            authTokenType = getIntent().getStringExtra(AccountGeneralTag.ARG_AUTH_TYPE);
            password = getIntent().getStringExtra(AccountGeneralTag.PARAM_USER_PASS);
            userData = getIntent().getExtras().getBundle(AccountGeneralTag.KEY_USERDATA);
            isAccountExist = findAccount(username);

        }

        if (authTokenType == null)
            authTokenType = getString(R.string.auth_type);


        fingerPringStuff();


    }

    void userSignIn() {

        if (!isAccountExist) {
            final Account account = new Account(username, authTokenType);

            if (mAccountManager.addAccountExplicitly(account, password, userData)) {

                Intent intent = new Intent();
                intent.putExtras(getIntent().getExtras());
                setAccountAuthenticatorResult(getIntent().getExtras());
                setResult(RESULT_OK, intent);
                finish();

            } else {

                Log.d("Logw", "Account NOT added");
            }

        } else {
            // exist so this user must be update
        }

    }


    private void fingerPringStuff() {


        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        // Check whether the device has a Fingerprint sensor.
        if (!fingerprintManager.isHardwareDetected()) {

            //device does not support fingerPrint Hardware
        } else {

            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                // textView.setText("Fingerprint authentication permission not enabled");
            } else {

                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {

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
    }

    public boolean findAccount(String accountName) {
        for (Account savedAccount : mAccountManager.getAccounts()) {
            if (TextUtils.equals(savedAccount.name, accountName)
                    && TextUtils.equals(savedAccount.type, authTokenType)) {
                return true;
            }
        }
        return false;
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

    public void onClick(View view) {
        int id = view.getId();
        // android.app.Fragment fragment = null;

        switch (id) {
            case R.id.fingerPrintBtn:
                setResult(VALIDATION_FINGERPRINT_REQUEST_CODE);
                finish();
                break;
          /*  case R.id.pinCodeInsertBtn:
                fragment = new RepeatPinCodeFragment();
                break;
            case R.id.pinCodeCancelBtn:
                setResult(VALIDATION_FINGERPRINT_REQUEST_CODE);
                finish();
                break;
            case R.id.repeatPinCodeSaveBtn:
                setResult(25);
                finish();
                break;*/
        }

        /*fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.outputFragment, fragment);
        transaction.commit();*/
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
