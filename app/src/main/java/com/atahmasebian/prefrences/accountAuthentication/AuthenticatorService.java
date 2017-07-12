package com.atahmasebian.prefrences.accountAuthentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    private AuthenticatorTools authenticatorTools;

    public AuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder ret = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            ret = getAuthenticatorTools().getIBinder();
        }
        return ret;
    }

    private AuthenticatorTools getAuthenticatorTools() {
        if (authenticatorTools == null) {
            authenticatorTools = new AuthenticatorTools(this);
        }
        return authenticatorTools;
    }

}
