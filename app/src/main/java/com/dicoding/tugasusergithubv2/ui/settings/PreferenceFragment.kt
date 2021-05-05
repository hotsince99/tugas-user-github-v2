package com.dicoding.tugasusergithubv2.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.alarm.AlarmReceiver


class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var LANGUAGE: String
    private lateinit var isAlarmActivated: SwitchPreference
    private lateinit var changeLanguage: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.fragment_preference)

        init()
        setSummaries()
    }
    private fun setSummaries() {
        val sharedPreference = preferenceManager.sharedPreferences
        isAlarmActivated.isChecked = sharedPreference.getBoolean(ALARM, false)
    }
    private fun init() {
        ALARM = resources.getString(R.string.key_alarm)
        LANGUAGE = resources.getString(R.string.key_language)
        isAlarmActivated = findPreference<SwitchPreference>(ALARM) as SwitchPreference
        changeLanguage = findPreference<Preference>(LANGUAGE) as Preference

        alarmReceiver = AlarmReceiver()

        isAlarmActivated.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            val isChecked = newValue as? Boolean ?: false

            if (isChecked) {
                alarmReceiver.setRepeatingAlarm(requireContext())
            } else {
                alarmReceiver.cancelAlarm(requireContext())
            }
            true
        }

        changeLanguage.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == ALARM) {
            isAlarmActivated.isChecked = sharedPreferences.getBoolean(ALARM, false)
        }
    }




}