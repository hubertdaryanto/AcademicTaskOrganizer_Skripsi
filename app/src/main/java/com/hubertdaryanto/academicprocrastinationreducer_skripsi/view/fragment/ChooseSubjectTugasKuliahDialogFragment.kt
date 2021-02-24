package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.SubjectTugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.SubjectTugasKuliahDialogFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.SubjectTugasKuliahDialogFragmentViewModelFactory

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

        val subjectDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = SubjectTugasKuliahDialogFragmentViewModelFactory(application, subjectDataSource)

        val subjectDialogFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            SubjectTugasKuliahDialogFragmentViewModel::class.java)

        subjectDialogFragmentViewModel.loadSubjectTugasKuliah()

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

            override fun onEditItem(id: Long) {

            }
        })
        binding.subjectList.adapter = adapter

        subjectDialogFragmentViewModel.subjectTugasKuliah.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        subjectDialogFragmentViewModel.selectSubject.observe(viewLifecycleOwner, Observer {
            it?.let{
                if (it == 0L)
                {
                    val dialog = AddSubjectTugasKuliahDialogFragment()
                    dialog.show(parentFragmentManager, "AddSubjectDialogFragment")
                }
                else
                {
                    sendSubject(it)
                    subjectDialogFragmentViewModel.afterSubjectTugasKuliahClicked()
                }

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