package com.atahmasebian.prefrences.pinCode;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atahmasebian.prefrences.R;

public class InsertInsertPinCodeFragment extends Fragment implements IInsertPinCodeView {

    EditText etFirstTxt, etSecondTxt, etThirdTxt, etFourthTxt;
    Button saveBtn, cancelBtn;
    TextView titleTxt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inser_pin_code, container, false);
        saveBtn = (Button) view.findViewById(R.id.pinCodeInsertBtn);
        cancelBtn = (Button) view.findViewById(R.id.pinCodeCancelBtn);
        titleTxt = (TextView) view.findViewById(R.id.pinCodeTitleTxt);

        etFirstTxt = (EditText) view.findViewById(R.id.etPinCodeFirstTxt);
        etSecondTxt = (EditText) view.findViewById(R.id.etPinCodeSecondTxt);
        etThirdTxt = (EditText) view.findViewById(R.id.etPinCodeThirdTxt);
        etFourthTxt = (EditText) view.findViewById(R.id.etPinCodeFourthTxt);

        showInsertPinCodeUi();
        return view;

    }



    @Override
    public void showInsertPinCodeUi() {
        titleTxt.setText("برای ورود به اپلیکشین میتوانید یک Pin چهار رقمی تنظیم نمایید .");
        saveBtn.setText("ثبت");
        cancelBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public String getPinCode() {
        return combinedPinCodeValue();

    }

    private String combinedPinCodeValue() {
        return etFirstTxt.getText().toString() +
                etSecondTxt.getText().toString() +
                etThirdTxt.getText().toString() +
                etFourthTxt.getText().toString();
    }
}


