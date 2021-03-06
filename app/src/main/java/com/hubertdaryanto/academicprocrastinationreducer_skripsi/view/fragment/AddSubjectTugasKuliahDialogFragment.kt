package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.AddSubjectTugasKuliahDialogBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.AddSubjectTugasKuliahDialogFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.AddSubjectTugasKuliahDialogFragmentViewModelFactory

class AddSubjectTugasKuliahDialogFragment: DialogFragment() {
    private lateinit var binding: AddSubjectTugasKuliahDialogBinding
    private lateinit var addSubjectTugasKuliahDialogViewModel: AddSubjectTugasKuliahDialogFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val inflater = requireActivity().layoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.add_subject_tugas_kuliah_dialog, null, false)

        val dataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = AddSubjectTugasKuliahDialogFragmentViewModelFactory(application, dataSource)

        addSubjectTugasKuliahDialogViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddSubjectTugasKuliahDialogFragmentViewModel::class.java)

        addSubjectTugasKuliahDialogViewModel.addSubjectAndDismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                if (TextUtils.isEmpty(binding.editSubjectName.text.toString()))
                {
                    binding.editSubjectName.error = context?.getString(R.string.add_subject_error)
                    Toast.makeText(context,binding.editSubjectName.error, Toast.LENGTH_LONG).show()
                }
                else
                {
                    val subjectName = binding.editSubjectName.text.toString().trim()
                    val mSubject =
                        SubjectTugasKuliah(
                            subjectTugasKuliahName = subjectName
                        )
                    addSubjectTugasKuliahDialogViewModel.addSubject(mSubject)
                    dismiss()
                    addSubjectTugasKuliahDialogViewModel.afterAddSubjectClicked()
                }

            }

        })

        addSubjectTugasKuliahDialogViewModel.dismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                dismiss()
                addSubjectTugasKuliahDialogViewModel.afterdismiss()
            }

        })

        binding.addSubjectDialogFragmentViewModel = addSubjectTugasKuliahDialogViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}