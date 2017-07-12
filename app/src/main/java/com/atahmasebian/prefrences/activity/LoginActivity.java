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

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    String password, username;
    public final int CANCEL_FINGERPRINT_REQUEST_CODE =1;
    public final int VALIDATION_FINGERPRINT_REQUEST_CODE =2;
    private String authTokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        authTokenType =getString(R.string.auth_type);


        boolean isExist = findAccount("cpuman");
        if (isExist){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivityForResult(intent,CANCEL_FINGERPRINT_REQUEST_CODE);

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = etPassword.getText().toString();
                username = etUsername.getText().toString();



                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle data = new Bundle();
                data.putString(AccountGeneralTag.ARG_ACCOUNT_NAME,username);
                data.putString(AccountGeneralTag.ARG_AUTH_TYPE, authTokenType);
                data.putString(AccountGeneralTag.PARAM_USER_PASS, password);

                Bundle userData = new Bundle();
                userData.putString("UserID", "25");//set finger print data

                data.putBundle(AccountManager.KEY_USERDATA, userData);

                intent.putExtras(data);

                startActivityForResult(intent,CANCEL_FINGERPRINT_REQUEST_CODE);
            }
        });


    }

    public boolean findAccount(String accountName) {
        for (Account savedAccount : AccountManager.get(getBaseContext()).getAccounts()) {
            if (TextUtils.equals(savedAccount.name, accountName)
                    && TextUtils.equals(savedAccount.type, authTokenType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CANCEL_FINGERPRINT_REQUEST_CODE && resultCode == -1){
            Toast.makeText(this, "finger print save in am ", Toast.LENGTH_SHORT).show();

        }
        if (requestCode==VALIDATION_FINGERPRINT_REQUEST_CODE){
            Toast.makeText(this, "finger print is valid plz doLogin ", Toast.LENGTH_SHORT).show();
        }
        if (requestCode==CANCEL_FINGERPRINT_REQUEST_CODE){
            Toast.makeText(this, "finger print is canceld ! ", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
