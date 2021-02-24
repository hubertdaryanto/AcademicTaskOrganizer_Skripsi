package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.onboarding.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentSixthOnboardingScreenBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.MainActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.OnboardingActivityViewModel

class SixthOnboardingScreen : Fragment() {
    private lateinit var binding: FragmentSixthOnboardingScreenBinding
    private val sharedVM: OnboardingActivityViewModel by activityViewModels()

    private lateinit var mSharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sixth_onboarding_screen, container, false)

        binding.button.setOnClickListener {
            mSharedPreferences =
                activity?.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)!!
            val editor = mSharedPreferences?.edit()
            if (editor != null) {
                editor.putBoolean("onboardingFinished", true)
                editor.apply()
            }
            val intent = Intent (getActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            getActivity()?.startActivity(intent)
            getActivity()?.finish()

        }

        return binding.root
    }
}