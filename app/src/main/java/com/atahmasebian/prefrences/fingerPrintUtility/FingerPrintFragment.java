package com.atahmasebian.prefrences.fingerPrintUtility;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atahmasebian.prefrences.R;


public class FingerPrintFragment extends Fragment implements IFingerPrintFragment {
    Button fingerprintCancelBtn;
    private ImageView alertImageView;
    private TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.finger_print_layout, container, false);

        fingerprintCancelBtn = (Button) view.findViewById(R.id.fingerPrintCancelBtn);
        titleTextView = (TextView) view.findViewById(R.id.fingerPrintTitleTxt);
        alertImageView = (ImageView) view.findViewById(R.id.alertRedIconImg);
        alertImageView.setVisibility(View.INVISIBLE);

        return view;
    }


    @Override
    public void showErrorMessage() {
        fingerprintCancelBtn.setText(R.string.fingerPrintButtonTitle);
        titleTextView.setText(R.string.fingerPrintTitleFragment);
        alertImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void doLogin() {
        fingerprintCancelBtn.setText(R.string.fingerPrintCancelButtonTitle);
        titleTextView.setText(R.string.fingerPrintTitleExceptionFragment);
        alertImageView.setVisibility(View.INVISIBLE);
    }
}
