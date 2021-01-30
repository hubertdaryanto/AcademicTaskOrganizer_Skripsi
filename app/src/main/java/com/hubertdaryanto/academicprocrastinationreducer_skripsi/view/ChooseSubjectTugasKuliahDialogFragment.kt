package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ChooseSubjectTugasKuliahDialogBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.SubjectTugasKuliahDialogFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.SubjectTugasKuliahDialogFragmentViewModelFactory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.SubjectTugasKuliahInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.SubjectTugasKuliahListener

class ChooseSubjectTugasKuliahDialogFragment : DialogFragment() {
    val TAG: String = this::class.java.simpleName

    private lateinit var binding: ChooseSubjectTugasKuliahDialogBinding
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        shared_data.fromFragment = this.targetFragment.toString()


        val inflater = requireActivity().layoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.choose_subject_tugas_kuliah_dialog, null, false)

        val dataSource = AppDatabase.getInstance(application).getAllQueryListDao
        val viewModelFactory = SubjectTugasKuliahDialogFragmentViewModelFactory(application, dataSource)

        val subjectDialogFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(SubjectTugasKuliahDialogFragmentViewModel::class.java)

        val adapter = SubjectTugasKuliahAdapter(SubjectTugasKuliahListener { subjectId ->
            subjectDialogFragmentViewModel.onSubjectTugasKuliahClicked(subjectId)
        }, object :
            SubjectTugasKuliahInterface {

            override fun onRemoveItem(id: Long) {
                AlertDialog.Builder(context).apply {
                    setTitle(context.getString(R.string.delete_subject_title_confirmation))
                    setMessage(context.getString(R.string.delete_subject_subtitle_confirmation))
                    setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                        subjectDialogFragmentViewModel.removeSubjectTugasKuliah(id)
                    }
                    setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

                    }
                }.create().show()

            }
        })
        binding.subjectList.adapter = adapter

        subjectDialogFragmentViewModel.subject.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        subjectDialogFragmentViewModel.selectSubject.observe(viewLifecycleOwner, Observer {
            it?.let{
                sendSubject(it)
                subjectDialogFragmentViewModel.afterSubjectTugasKuliahClicked()
            }
        })

        subjectDialogFragmentViewModel.showAddSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val dialog = AddSubjectTugasKuliahDialogFragment()
                dialog.show(parentFragmentManager, "AddSubjectDialogFragment")
            }
        })

        subjectDialogFragmentViewModel.dismiss.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                sendSubject(null)
                dismiss()
                subjectDialogFragmentViewModel.afterdismiss()
            }

        })


        binding.subjectDialogFragmentViewModel = subjectDialogFragmentViewModel
        binding.lifecycleOwner = this

        val manager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(binding.subjectList.context,
        manager.orientation)
        binding.subjectList.addItemDecoration(dividerItemDecoration)

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
    

    private fun sendSubject(message: Long?){
        if (targetFragment == null)
        {
            return
        }
        if (AddTugasKuliahFragment().getInstance() != null)
        {
            intent = AddTugasKuliahFragment().getInstance()?.newIntent(message)!!
        }

        if (EditTugasKuliahFragment().getInstance() != null)
        {
            intent = EditTugasKuliahFragment().getInstance()?.newIntent(message)!!
        }
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        dismiss()
    }
}