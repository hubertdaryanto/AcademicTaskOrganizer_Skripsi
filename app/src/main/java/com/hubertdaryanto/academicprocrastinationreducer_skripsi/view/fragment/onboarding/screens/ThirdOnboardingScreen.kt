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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentThirdOnboardingScreenBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.onboarding_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.AddTugasKuliahFragment
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.OnboardingActivityViewModel

//interface ThirdOnboardingScreenListener {
//    fun onNextClicked()
//}

class ThirdOnboardingScreen
//    (
//    val listener: ThirdOnboardingScreenListener
//    )
    : Fragment() {
    private lateinit var binding: FragmentThirdOnboardingScreenBinding
    private val sharedVM: OnboardingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_third_onboarding_screen,
            container,
            false
        )
        val childFragment = childFragmentManager.fragments[0] as AddTugasKuliahFragment

        onboarding_data.mSubjectName?.let { childFragment.setupFromOnboarding(it) }

        binding.button.setOnClickListener {
//            listener.onNextClicked()
            childFragment.addTugasKuliahFragmentViewModel.onAddTugasKuliahClicked2()


            if (onboarding_data.approvalForGoToFourthScreen == true)
            {
                this.findNavController().navigate(ThirdOnboardingScreenDirections.actionThirdScreenToFourthOnboardingScreen())
            }
        }

        binding.button2.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }

}