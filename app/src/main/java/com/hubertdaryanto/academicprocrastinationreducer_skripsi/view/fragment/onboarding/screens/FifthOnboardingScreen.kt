package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentFifthOnboardingScreenBinding

class FifthOnboardingScreen : Fragment() {
    private lateinit var binding: FragmentFifthOnboardingScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fifth_onboarding_screen, container, false)

        binding.button.setOnClickListener {
            this.findNavController().navigate(FifthOnboardingScreenDirections.actionFifthOnboardingScreenToSixthOnboardingScreen())
        }

        return binding.root
    }
}