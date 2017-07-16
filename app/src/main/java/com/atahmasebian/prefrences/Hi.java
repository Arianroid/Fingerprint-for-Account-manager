package com.atahmasebian.prefrences;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.preference.PreferenceManager;

public class Hi extends Application {

    public static final int PINCODE_LOGIN_TYPE = 88;
    public static final int FINGERPRINT_LOGIN_TYPE = 77;
    public static final int USERPASS_LOGIN_TYPE = 66;
    public static final String LOGIN_TYPE = "loginType";

    public static final String FINGERPRINT_KEY="FingerPrintKey";
    public static final String PINCODE_KEY="PinCodeKey";



    static Context context;
    private static FingerprintManager fingerprintManager;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isFingerPrintHardwareDetected() {

        //Device does not support fingerPrint Hardware
        return fingerprintManager.isHardwareDetected();

    }

    public static boolean isFingerPrintsHasEnrolled() {
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public static int getLoginValidationType() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(LOGIN_TYPE, 0);

    }

    public static void setLoginValidationType(int loginType) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        switch (loginType) {
            case PINCODE_LOGIN_TYPE:
                editor.putInt(LOGIN_TYPE, PINCODE_LOGIN_TYPE);
                editor.putBoolean(PINCODE_KEY,true);
                editor.putBoolean(FINGERPRINT_KEY,false);

                break;
            case FINGERPRINT_LOGIN_TYPE:
                editor.putInt(LOGIN_TYPE, FINGERPRINT_LOGIN_TYPE);
                editor.putBoolean(FINGERPRINT_KEY,true);
                editor.putBoolean(PINCODE_KEY,false);
                break;
            case USERPASS_LOGIN_TYPE:
                editor.putInt(LOGIN_TYPE,USERPASS_LOGIN_TYPE);
                editor.putBoolean(FINGERPRINT_KEY,false);
                editor.putBoolean(PINCODE_KEY,false);
                break;
        }
        editor.apply();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
    }
}
