package com.atahmasebian.prefrences.preference;

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
import com.atahmasebian.prefrences.activity.MainActivity;


public class SecuritySettingPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_secure_setting);
        setHasOptionsMenu(true);


        final CheckBoxPreference fingerPrintPreference = (CheckBoxPreference) findPreference("FingerPrintKey");
        final CheckBoxPreference pinCodePreference = (CheckBoxPreference) findPreference("PinCodeKey");


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
                    fingerPrintPreference.setEnabled(true);
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
            Hi.setLoginValidationType(Hi.FINGERPRINT_LOGIN_TYPE);
        }else {

            //go to pin code insert fragment
        }
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
