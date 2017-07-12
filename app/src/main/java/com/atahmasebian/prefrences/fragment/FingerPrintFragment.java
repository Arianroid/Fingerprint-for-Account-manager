package com.atahmasebian.prefrences.fragment;

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
    public final int CANCEL_FINGERPRINT_REQUEST_CODE = 1;
    Button fingerprintBtn;
    private ImageView alertImageView;
    private TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.finger_print_layout, container, false);
        fingerprintBtn = (Button) view.findViewById(R.id.fingerPrintBtn);

        alertImageView = (ImageView) view.findViewById(R.id.alertRedIconImg);
        alertImageView.setVisibility(View.INVISIBLE);

        titleTextView = (TextView) view.findViewById(R.id.fingerPrintTitleTxt);
        titleTextView.setText("برای تنظیم ورود به اپلیکیشن از طریق اثر انگشت، انگشت خود را بروی سنسور گوشی قرار دهید .");


        return view;
    }


    @Override
    public void showErrorMessage() {
                fingerprintBtn.setText(" ورود با نام کاربری و رمز عبور ");
               alertImageView.setVisibility(View.VISIBLE);
                titleTextView.setText("اثر انگشت شما یافت نشد لطفا انگشت خود را مجددا روی سنسور قرار دهید .");
    }

    @Override
    public void showCompletedMessage() {
                fingerprintBtn.setText(" نمی خواهم ");
                alertImageView.setVisibility(View.INVISIBLE);
                titleTextView.setText("برای تنظیم ورود به اپلیکیشن از طریق اثر انگشت، انگشت خود را بروی سنسور گوشی قرار دهید .");
    }
}
