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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentFourthOnboardingScreenBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.onboarding_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.AddTugasKuliahFinishCommitmentFragment
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.AddTugasKuliahFragment
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.OnboardingActivityViewModel

class FourthOnboardingScreen : Fragment() {
    private lateinit var binding: FragmentFourthOnboardingScreenBinding
    private val sharedVM: OnboardingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fourth_onboarding_screen, container, false)

        val childFragment = childFragmentManager.fragments[0] as AddTugasKuliahFinishCommitmentFragment


        binding.button.setOnClickListener {
            childFragment.addTugasKuliahFinishCommitmentFragmentViewModel.onAddTugasKuliahClicked2()
            if (onboarding_data.approvalForGoToFifthScreen == true)
            {
                shared_data.isFromOnboarding = false
                this.findNavController().navigate(FourthOnboardingScreenDirections.actionFourthOnboardingScreenToFifthOnboardingScreen())
            }

        }

        binding.button2.setOnClickListener {
            onboarding_data.approvalForGoToFourthScreen = false
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}