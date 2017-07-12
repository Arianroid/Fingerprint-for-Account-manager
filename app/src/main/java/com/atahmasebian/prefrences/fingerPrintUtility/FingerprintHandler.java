package com.atahmasebian.prefrences.fingerPrintUtility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;


public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private IFingerPrintHandlerView view;

    public FingerprintHandler(Context mContext, IFingerPrintHandlerView view) {
        context = mContext;
        this.view = view;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        view.showErrorMessage();
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        view.showErrorMessage();
    }


    @Override
    public void onAuthenticationFailed() {
        view.showErrorMessage();
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        view.showCompletedMessage();
    }


}
