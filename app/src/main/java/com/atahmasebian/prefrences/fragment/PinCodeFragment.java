package com.atahmasebian.prefrences.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.atahmasebian.prefrences.R;

public class PinCodeFragment extends Fragment {

    EditText etFirstTxt, etSecondTxt, etThirdTxt, etFourthTxt;
    Button saveBtn, cancelBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_code_layout, container, false);
        saveBtn = (Button) view.findViewById(R.id.pinCodeInsertBtn);
        cancelBtn = (Button) view.findViewById(R.id.pinCodeCancelBtn);

        etFirstTxt = (EditText) view.findViewById(R.id.etPinCodeFirstTxt);
        etSecondTxt = (EditText) view.findViewById(R.id.etPinCodeSecondTxt);
        etThirdTxt = (EditText) view.findViewById(R.id.etPinCodeThirdTxt);
        etFourthTxt = (EditText) view.findViewById(R.id.etPinCodeFourthTxt);

        return view;

    }


}


