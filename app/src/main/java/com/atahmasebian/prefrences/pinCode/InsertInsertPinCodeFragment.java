package com.atahmasebian.prefrences.pinCode;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atahmasebian.prefrences.R;

public class InsertInsertPinCodeFragment extends Fragment implements IInsertPinCodeView {

    EditText etFirstTxt, etSecondTxt, etThirdTxt, etFourthTxt;
    Button insertBtn, cancelBtn;
    TextView titleTxt;

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
            int id = textView.getId();


            switch (id) {
                case R.id.etPinCodeFirstTxt:
                    etSecondTxt.requestFocus();
                    break;
                case R.id.etPinCodeSecondTxt:
                    etThirdTxt.requestFocus();
                    break;
                case R.id.etPinCodeThirdTxt:
                    etFourthTxt.requestFocus();
                    break;
            }

            return true;
        }
    };
    private TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                etFirstTxt.onEditorAction(EditorInfo.IME_ACTION_GO);
            }
        }

    };
    private TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                etSecondTxt.onEditorAction(EditorInfo.IME_ACTION_GO);
            } else if (etSecondTxt.getText().toString().length() <= 0) {
                etFirstTxt.requestFocus();
            }
        }

    };
    private TextWatcher textWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                etThirdTxt.onEditorAction(EditorInfo.IME_ACTION_GO);
            } else if (etThirdTxt.getText().toString().length() <= 0) {
                etSecondTxt.requestFocus();
            }
        }


    };
    private TextWatcher textWatcher4 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etFourthTxt.getText().toString().length() <= 0) {
                etThirdTxt.requestFocus();
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inser_pin_code, container, false);
        insertBtn = (Button) view.findViewById(R.id.pinCodeInsertBtn);
        cancelBtn = (Button) view.findViewById(R.id.pinCodeCancelBtn);
        titleTxt = (TextView) view.findViewById(R.id.pinCodeTitleTxt);

        etFirstTxt = (EditText) view.findViewById(R.id.etPinCodeFirstTxt);
        etSecondTxt = (EditText) view.findViewById(R.id.etPinCodeSecondTxt);
        etThirdTxt = (EditText) view.findViewById(R.id.etPinCodeThirdTxt);
        etFourthTxt = (EditText) view.findViewById(R.id.etPinCodeFourthTxt);

        etFirstTxt.addTextChangedListener(textWatcher1);
        etSecondTxt.addTextChangedListener(textWatcher2);
        etThirdTxt.addTextChangedListener(textWatcher3);

        etFirstTxt.setOnEditorActionListener(onEditorActionListener);
        etSecondTxt.setOnEditorActionListener(onEditorActionListener);
        etThirdTxt.setOnEditorActionListener(onEditorActionListener);
        etFourthTxt.setOnEditorActionListener(onEditorActionListener);

        showInsertPinCodeUi();
        return view;

    }


    @Override
    public void showInsertPinCodeUi() {
        titleTxt.setText("برای ورود به اپلیکشین میتوانید یک Pin چهار رقمی تنظیم نمایید .");
        insertBtn.setText("ثبت");
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


