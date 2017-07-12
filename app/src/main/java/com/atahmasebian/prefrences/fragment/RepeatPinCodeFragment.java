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


public class RepeatPinCodeFragment extends Fragment {

    EditText etFirstTxt, etSecondTxt, etThirdTxt, etFourthTxt;
    Button saveBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repeat_pin_code_layout, container, false);
        saveBtn = (Button) view.findViewById(R.id.repeatPinCodeSaveBtn);

        etFirstTxt = (EditText) view.findViewById(R.id.etRepeatPinCodeFirstTxt);
        etSecondTxt = (EditText) view.findViewById(R.id.etRepeatPinCodeSecondTxt);
        etThirdTxt = (EditText) view.findViewById(R.id.etRepeatPinCodeThirdTxt);
        etFourthTxt = (EditText) view.findViewById(R.id.etRepeatPinCodeFourthTxt);

        return view;

    }

}
