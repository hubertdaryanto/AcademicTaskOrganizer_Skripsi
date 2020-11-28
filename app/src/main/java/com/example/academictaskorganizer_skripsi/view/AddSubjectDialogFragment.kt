package com.example.academictaskorganizer_skripsi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.Subject
import com.example.academictaskorganizer_skripsi.databinding.AddSubjectDialogBinding
import com.example.academictaskorganizer_skripsi.viewModel.AddSubjectDialogFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.AddSubjectDialogFragmentViewModelFactory

class AddSubjectDialogFragment: DialogFragment() {
    private lateinit var binding: AddSubjectDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application





        val inflater = requireActivity().layoutInflater;

        binding = DataBindingUtil.inflate(inflater, R.layout.add_subject_dialog, null, false)

        val dataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = AddSubjectDialogFragmentViewModelFactory(application, dataSource)

        val addSubjectDialogViewModel = ViewModelProvider(this, viewModelFactory).get(AddSubjectDialogFragmentViewModel::class.java)

        addSubjectDialogViewModel.addSubjectAndDismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val subjectName = binding.editSubjectName.text.toString().trim()
                val mSubject = Subject(subjectName = subjectName)
                addSubjectDialogViewModel.addSubject(mSubject)
                dismiss()
                addSubjectDialogViewModel.afterAddSubjectClicked()
            }

        })

        addSubjectDialogViewModel.dismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                dismiss()
                addSubjectDialogViewModel.afterdismiss()
            }

        })

        binding.addSubjectDialogFragmentViewModel = addSubjectDialogViewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }
}