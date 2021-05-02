package com.dicoding.tugasusergithubv2.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import com.dicoding.tugasusergithubv2.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.setting_activity, PreferenceFragment()).commit()
    }
}