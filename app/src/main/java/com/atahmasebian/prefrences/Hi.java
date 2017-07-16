package com.atahmasebian.prefrences;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

public class Hi extends Application {

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
    }
}
