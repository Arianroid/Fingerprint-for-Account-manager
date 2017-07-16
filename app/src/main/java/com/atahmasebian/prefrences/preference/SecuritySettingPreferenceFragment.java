package com.atahmasebian.prefrences.preference;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Toast;

import com.atahmasebian.prefrences.Hi;
import com.atahmasebian.prefrences.R;
import com.atahmasebian.prefrences.accountAuthentication.AccountGeneralTag;
import com.atahmasebian.prefrences.activity.MainActivity;


public class SecuritySettingPreferenceFragment extends PreferenceFragment {

    public static final int PinCodeConfirmed = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_secure_setting);
        setHasOptionsMenu(true);


        final CheckBoxPreference fingerPrintPreference = (CheckBoxPreference) findPreference(Hi.FINGERPRINT_KEY);
        final CheckBoxPreference pinCodePreference = (CheckBoxPreference) findPreference(Hi.PINCODE_KEY);


        fingerPrintPreference.setEnabled(false);

        if (Hi.isFingerPrintHardwareDetected()) {
            fingerPrintPreference.setEnabled(true);
        }

        if (Hi.isFingerPrintsHasEnrolled() && fingerPrintPreference.isChecked()) {
            pinCodePreference.setEnabled(false);
            pinCodePreference.setChecked(false);
        } else pinCodePreference.setEnabled(true);

        if (pinCodePreference.isChecked()) {
            fingerPrintPreference.setEnabled(false);
            fingerPrintPreference.setChecked(false);
        }

        fingerPrintPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {

                boolean visibile = (boolean) value;

                if (visibile) {
                    if (Hi.isFingerPrintsHasEnrolled()) {
                        //disable pinCode checkbox
                        pinCodePreference.setEnabled(false);
                        pinCodePreference.setChecked(false);

                        //show fingerPrint layout for sign up

                        return visibile;
                    } else {
                        Toast.makeText(getActivity(), "کاربر گرامی، شما می بایست برای استفاده از این امکان اثر انگشت خود را از قسمت تنظیمات گوشی ثبت نمایید.", Toast.LENGTH_SHORT).show();
                        return !visibile;
                    }
                } else {
                    return !visibile;
                }

            }
        });


        pinCodePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                boolean isChecked = (boolean) value;
                if (isChecked) {
                    fingerPrintPreference.setChecked(false);
                    fingerPrintPreference.setEnabled(false);

                    checkPinCodeValidation();
                } else {
                    if (Hi.isFingerPrintHardwareDetected()) {
                        fingerPrintPreference.setEnabled(true);
                    }
                    Hi.setLoginValidationType(Hi.USERPASS_LOGIN_TYPE);
                }
                return true;
            }


        });


        //PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("FingerPrintKey"));
        //PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("PinCodeKey"));


        /*SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "NA");
        boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
        String downloadType = SP.getString("downloadType","1");*/


    }

    private void checkPinCodeValidation() {
        String pinCode = MainActivity.userData.getString("PinCode");
        if (pinCode != null && !pinCode.isEmpty()) {
            Hi.setLoginValidationType(Hi.PINCODE_LOGIN_TYPE);
            Toast.makeText(getActivity(), "ورود با پین کد فعال شد", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);

            Bundle data = new Bundle();
            data.putString(AccountGeneralTag.ARG_ACCOUNT_NAME, "cpuman");
            data.putString(AccountGeneralTag.ARG_AUTH_TYPE, getString(R.string.auth_type));
            data.putString(AccountGeneralTag.PARAM_USER_PASS, "a1234567");


            //set customize out  data
            Bundle userData = new Bundle();
            userData.putString("UserID", "25");
            data.putBundle(AccountManager.KEY_USERDATA, userData);
            intent.putExtras(data);

            startActivityForResult(intent, PinCodeConfirmed);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PinCodeConfirmed) {
            Toast.makeText(getActivity(), "pinCode is confirm for new login type", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), PreferencesSettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
