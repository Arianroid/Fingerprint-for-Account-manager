package com.atahmasebian.prefrences.preference;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.atahmasebian.prefrences.R;


public class SecuritySettingPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_secure_setting);
        setHasOptionsMenu(true);

        PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("FingerPrintKey"));
        PreferencesSettingsActivity.bindPreferenceSummaryToValue(findPreference("PinCodeKey"));

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
