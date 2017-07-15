package com.atahmasebian.prefrences.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atahmasebian.prefrences.R;
import com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag;

import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.ACCOUNT_DTO;

public class LoginActivity extends AppCompatActivity {
    public final int CANCEL_FINGERPRINT_REQUEST_CODE = 1;
    public final int VALIDATION_FINGERPRINT_REQUEST_CODE = 2;
    public final int VALIDATION_PinCode_REQUEST_CODE = 3;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private String password, username;
    private String authTokenType;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        authTokenType = getString(R.string.auth_type);

        mAccountManager = AccountManager.get(getBaseContext());
        Account account = findAccount();

        if (account != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(ACCOUNT_DTO,account);
            startActivityForResult(intent, CANCEL_FINGERPRINT_REQUEST_CODE);
        }


        //account did not found plz login with userPassword
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = etPassword.getText().toString();
                username = etUsername.getText().toString();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle data = new Bundle();
                data.putString(AccountGeneralTag.ARG_ACCOUNT_NAME, username);
                data.putString(AccountGeneralTag.ARG_AUTH_TYPE, authTokenType);
                data.putString(AccountGeneralTag.PARAM_USER_PASS, password);


                //set customize out  data
                Bundle userData = new Bundle();
                userData.putString("UserID", "25");

                data.putBundle(AccountManager.KEY_USERDATA, userData);

                intent.putExtras(data);

                startActivityForResult(intent, CANCEL_FINGERPRINT_REQUEST_CODE);
            }
        });


    }

    public Account findAccount() {
        for (Account savedAccount : mAccountManager.getAccounts()) {
            if (TextUtils.equals(savedAccount.type, authTokenType)) {
                return savedAccount;
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==VALIDATION_PinCode_REQUEST_CODE){
            Toast.makeText(this, "login With pinCode", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == CANCEL_FINGERPRINT_REQUEST_CODE && resultCode == -1) {
            Toast.makeText(this, "finger print save in am ", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == VALIDATION_FINGERPRINT_REQUEST_CODE) {
            Toast.makeText(this, "finger print is valid plz doLogin ", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CANCEL_FINGERPRINT_REQUEST_CODE) {
            Toast.makeText(this, "finger print is canceld ! ", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
