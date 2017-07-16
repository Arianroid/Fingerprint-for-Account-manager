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


public class SecuritySettingPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_secure_setting);
        setHasOptionsMenu(true);


        final CheckBoxPreference fingerPrintPreference = (CheckBoxPreference) findPreference("FingerPrintKey");
        fingerPrintPreference.setEnabled(false);

        if (Hi.isFingerPrintHardwareDetected()) {
            fingerPrintPreference.setEnabled(true);
        }

        fingerPrintPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {

                boolean newState = (boolean) value;

                boolean outputState = true;
                if (newState) {
                    if (Hi.isFingerPrintsHasEnrolled()) {
                        //show fingerPrint layout for sign up
                        return newState;
                    } else {
                        Toast.makeText(getActivity(), "کاربر گرامی، شما می بایست برای استفاده از این امکان اثر انگشت خود را از قسمت تنظیمات گوشی ثبت نمایید.", Toast.LENGTH_SHORT).show();
                        return !newState;
                    }
                } else {
                    return !newState;
                }

            }
        });


        // PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("FingerPrintKey"));
        //PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("PinCodeKey"));

        /*
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "NA");
        boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
        String downloadType = SP.getString("downloadType","1");
        */

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
