package com.example.academictaskorganizer_skripsi.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.databinding.SubjectDialogBinding
import com.example.academictaskorganizer_skripsi.viewModel.SubjectDialogFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.SubjectDialogFragmentViewModelFactory


class SubjectDialogFragment : DialogFragment() {

    private lateinit var binding: SubjectDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application





        val inflater = requireActivity().layoutInflater;

        binding = DataBindingUtil.inflate(inflater, R.layout.subject_dialog, null, false)

        val dataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = SubjectDialogFragmentViewModelFactory(application, dataSource)

        val subjectDialogFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(SubjectDialogFragmentViewModel::class.java)

        val adapter = SubjectAdapter(SubjectListener { subjectId ->
            subjectDialogFragmentViewModel.onSubjectClicked(subjectId)
        })
        binding.subjectList.adapter = adapter

        subjectDialogFragmentViewModel.subject.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        subjectDialogFragmentViewModel.showAddSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val dialog = AddSubjectDialogFragment()
                dialog.show(parentFragmentManager, "AddSubjectDialogFragment")
            }
        })

        subjectDialogFragmentViewModel.dismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                dismiss()
                subjectDialogFragmentViewModel.afterdismiss()
            }

        })


        binding.subjectDialogFragmentViewModel = subjectDialogFragmentViewModel
        binding.setLifecycleOwner(this)

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0 -> return 3
                    else -> return 1
                }
            }
        }
        binding.subjectList.layoutManager = manager

        return binding.root
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val application = requireNotNull(this.activity).application
//
//
//
//
//
//            val inflater = requireActivity().layoutInflater;
//
//            binding = DataBindingUtil.inflate(inflater, R.layout.subject_dialog, null, false)
//
//            val dataSource = AppDatabase.getInstance(application).getSubjectDao
//            val viewModelFactory = SubjectDialogFragmentViewModelFactory(application, dataSource)
//
//            val subjectDialogFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(SubjectDialogFragmentViewModel::class.java)
//
//            val adapter = SubjectAdapter(SubjectListener { subjectId ->
//                subjectDialogFragmentViewModel.onSubjectClicked(subjectId)
//            })
//            binding.subjectList.adapter = adapter
//
//            subjectDialogFragmentViewModel.showAddSubjectDialog.observe(viewLifecycleOwner, Observer {
//                if (it == true)
//                {
//                    //show add subject dialog
//                }
//            })
//
//
//
//            binding.subjectDialogFragmentViewModel = subjectDialogFragmentViewModel
//            binding.setLifecycleOwner(this)
//
//            val manager = GridLayoutManager(activity, 3)
//            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
//                override fun getSpanSize(position: Int): Int {
//                    when (position) {
//                        0 -> return 3
//                        else -> return 1
//                    }
//                }
//            }
//            binding.subjectList.layoutManager = manager
//
//            val builder = AlertDialog.Builder(it)
////            builder.setMessage(R.string.dialog_fire_missiles)
//            builder.setView(binding.root)
////                .setPositiveButton(R.string.fire,
////                    DialogInterface.OnClickListener { dialog, id ->
////                        // FIRE ZE MISSILES!
////                    })
//                .setPositiveButton("tambah subject",
//                DialogInterface.OnClickListener {
//                    dialog, id ->
//                    subjectDialogFragmentViewModel.onShowAddSubjectDialogClicked()
//                })
//                .setNegativeButton(
//                    R.string.batal,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                        dismiss()
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}