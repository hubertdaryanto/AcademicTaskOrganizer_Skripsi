package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentSecondOnboardingScreenBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.onboarding_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data

class SecondOnboardingScreen : Fragment() {
    private lateinit var binding: FragmentSecondOnboardingScreenBinding
//    private val sharedVM: OnboardingActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val application = requireNotNull(this.activity).application
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_onboarding_screen, container, false)



        val subjectTugasKuliahDatabase = AppDatabase.getInstance(application).getSubjectTugasKuliahDao

        binding.button.setOnClickListener {
//            sharedVM.setSubjectText(binding.editSubjectName.text.toString().trim())
            onboarding_data.mSubjectName = binding.editSubjectName.text.toString().trim()
            shared_data.isFromOnboarding = true
            this.findNavController().navigate(SecondOnboardingScreenDirections.actionSecondScreenToThirdScreen())
        }


        return binding.root
    }

}