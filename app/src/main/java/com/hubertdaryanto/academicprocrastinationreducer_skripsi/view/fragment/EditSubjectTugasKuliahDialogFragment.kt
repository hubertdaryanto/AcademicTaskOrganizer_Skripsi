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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.EditSubjectTugasKuliahDialogBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.EditSubjectTugasKuliahDialogFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.EditSubjectTugasKuliahDialogFragmentViewModelFactory

class EditSubjectTugasKuliahDialogFragment(subjectId: Long): DialogFragment() {
    private lateinit var binding: EditSubjectTugasKuliahDialogBinding
    private lateinit var editSubjectTugasKuliahDialogViewModel: EditSubjectTugasKuliahDialogFragmentViewModel
    private val subjectId: Long = subjectId

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val inflater = requireActivity().layoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_subject_tugas_kuliah_dialog, null, false)

        val dataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = EditSubjectTugasKuliahDialogFragmentViewModelFactory(application, dataSource)

        editSubjectTugasKuliahDialogViewModel = ViewModelProvider(this, viewModelFactory).get(
            EditSubjectTugasKuliahDialogFragmentViewModel::class.java)

        editSubjectTugasKuliahDialogViewModel.loadSubject(subjectId)

        editSubjectTugasKuliahDialogViewModel.subjectTugasKuliah.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                binding.editSubjectName.setText(it.subjectTugasKuliahName)
            }
        })

        editSubjectTugasKuliahDialogViewModel.addSubjectAndDismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                if (TextUtils.isEmpty(binding.editSubjectName.text.toString()))
                {
                    binding.editSubjectName.error = context?.getString(R.string.add_subject_error)
                    Toast.makeText(context,binding.editSubjectName.error, Toast.LENGTH_LONG).show()
                }
                else
                {
                    editSubjectTugasKuliahDialogViewModel.subjectTugasKuliah.value?.subjectTugasKuliahName = binding.editSubjectName.text.toString().trim()
                    editSubjectTugasKuliahDialogViewModel.updateSubject(
                        editSubjectTugasKuliahDialogViewModel.subjectTugasKuliah.value!!
                    )
                    dismiss()
                    editSubjectTugasKuliahDialogViewModel.afterAddSubjectClicked()
                }

            }

        })

        editSubjectTugasKuliahDialogViewModel.dismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                dismiss()
                editSubjectTugasKuliahDialogViewModel.afterdismiss()
            }

        })

        binding.editSubjectDialogFragmentViewModel = editSubjectTugasKuliahDialogViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}