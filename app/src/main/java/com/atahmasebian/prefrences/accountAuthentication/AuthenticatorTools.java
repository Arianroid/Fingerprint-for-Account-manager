package com.atahmasebian.prefrences.accountAuthentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.atahmasebian.prefrences.activity.MainActivity;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.ARG_ACCOUNT_NAME;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.ARG_ACCOUNT_TYPE;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.ARG_AUTH_TYPE;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.AUTHTOKEN_TYPE_READ_ONLY;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
import static com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;


class AuthenticatorTools extends AbstractAccountAuthenticator {

    private final Context mContext;

    AuthenticatorTools(Context context) {
        super(context);
        this.mContext = context;

    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);


        return bundle;

    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {



        final AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);

        // login in server
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                try {
                    //authToken = sServerAuthenticate.userSignIn(account.name, password, authTokenType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // authToken is not null - set data in bundle
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(ARG_ACCOUNT_NAME, account.name);
            result.putString(ARG_AUTH_TYPE, account.type);
            return result;
        }

        // authToken is null - need to login or signup again in AuthenticationActivity
        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(ARG_ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }
}
