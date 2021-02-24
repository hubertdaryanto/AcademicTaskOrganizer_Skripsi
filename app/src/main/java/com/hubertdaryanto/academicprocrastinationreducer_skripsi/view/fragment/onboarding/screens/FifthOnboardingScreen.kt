package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentFifthOnboardingScreenBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.OnboardingActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.OnboardingActivityViewModel

class FifthOnboardingScreen : Fragment() {
    private lateinit var binding: FragmentFifthOnboardingScreenBinding
    private val sharedVM: OnboardingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fifth_onboarding_screen, container, false)

        val parentActivity = activity as OnboardingActivity

        binding.button.setOnClickListener {
            parentActivity.showPermissionDialog()
            this.findNavController().navigate(FifthOnboardingScreenDirections.actionFifthOnboardingScreenToSixthOnboardingScreen())
        }

        return binding.root
    }
}