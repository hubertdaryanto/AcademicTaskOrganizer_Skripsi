package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startMainActivity()
    }

    private fun startMainActivity() {
        mSharedPreferences = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        mRunnable = Runnable {
            if (mSharedPreferences.getBoolean("onboardingFinished",false))
            {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
            {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }

            finish()
        }

        mHandler = Handler()

        mHandler.postDelayed(mRunnable, 1000)
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(mRunnable)
    }

}