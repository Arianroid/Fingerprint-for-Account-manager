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
import android.widget.Toast;

import com.atahmasebian.prefrences.R;
import com.atahmasebian.prefrences.activity.MainActivity;

public class ConfirmPinCodeFragmentPinCode extends Fragment implements IConfirmPinCodeView {

    EditText firstEdittext, secondEdittext, thirdEdittext, fourthEditText;
    Button btn;
    TextView title;
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
            int id = textView.getId();
            switch (id) {
                case R.id.confirmFirstEditText:
                    secondEdittext.requestFocus();
                    break;
                case R.id.confirmSecondEditText:
                    thirdEdittext.requestFocus();
                    break;
                case R.id.confirmThirdEditText:
                    fourthEditText.requestFocus();
                    break;
                case R.id.confirmFourhEditText:
                    //dologi
                    break;
            }

            return true;
        }
    };
    private boolean isConfirmLayoutShown = false;
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
                firstEdittext.onEditorAction(EditorInfo.IME_ACTION_GO);
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
                secondEdittext.onEditorAction(EditorInfo.IME_ACTION_GO);
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
                thirdEdittext.onEditorAction(EditorInfo.IME_ACTION_GO);
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
            if (s.length() > 0) {
                if (!MainActivity.isPinCodeConfirm()) {
                    showExceptionLayout();
                }
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_pin_code, container, false);
        btn = (Button) view.findViewById(R.id.confirmBtn);
        title = (TextView) view.findViewById(R.id.confirmTitle);

        firstEdittext = (EditText) view.findViewById(R.id.confirmFirstEditText);
        secondEdittext = (EditText) view.findViewById(R.id.confirmSecondEditText);
        thirdEdittext = (EditText) view.findViewById(R.id.confirmThirdEditText);
        fourthEditText = (EditText) view.findViewById(R.id.confirmFourhEditText);
        firstEdittext.addTextChangedListener(textWatcher1);
        secondEdittext.addTextChangedListener(textWatcher2);
        thirdEdittext.addTextChangedListener(textWatcher3);
        fourthEditText.addTextChangedListener(textWatcher4);

        firstEdittext.setOnEditorActionListener(onEditorActionListener);
        secondEdittext.setOnEditorActionListener(onEditorActionListener);
        thirdEdittext.setOnEditorActionListener(onEditorActionListener);
        fourthEditText.setOnEditorActionListener(onEditorActionListener);

        if (isConfirmLayoutShown) {
            showConfirmPinCodeUi();
        } else showLoginwithPinCodeUi();
        return view;

    }

    @Override
    public void showConfirmPinCodeUi() {
        title.setText("لطفا مجددا Pin خود را وارد کنید .");
        btn.setText("ذخیره");
    }


    @Override
    public void showLoginwithPinCodeUi() {
        title.setText("لطفا برای ورود پین خود را وارد کنید .");
        btn.setText("ورود با شناسه کاربری و رمز عبور ");
    }

    @Override
    public String getConfirmPinCode() {
        return combinedPinCodeValue();

    }

    @Override
    public void showExceptionLayout() {
        title.setText("پین را اشتباه وارد کردید لطفا پین را صحیح وارد نمایید .");

    }

    @Override
    public void setShownConfirmLayout(boolean isShown) {
        isConfirmLayoutShown = isShown;
    }

    private String combinedPinCodeValue() {

        return firstEdittext.getText().toString() +
                secondEdittext.getText().toString() +
                thirdEdittext.getText().toString() +
                fourthEditText.getText().toString();
    }
}


