package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentMataKuliahListBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.AddTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.ViewTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.SubjectTugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.MataKuliahListFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.MataKuliahListFragmentViewModelFactory

class MataKuliahListFragment : Fragment() {
    private lateinit var binding: FragmentMataKuliahListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mata_kuliah_list, container,false)

        val subjectDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = MataKuliahListFragmentViewModelFactory(application, subjectDataSource)

        val mataKuliahListFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            MataKuliahListFragmentViewModel::class.java)
        val adapter = SubjectTugasKuliahAdapter(SubjectTugasKuliahListener { subjectId ->
            mataKuliahListFragmentViewModel.onSubjectTugasKuliahClicked(subjectId)
        }, object :
            SubjectTugasKuliahInterface {

            override fun onRemoveItem(id: Long) {
                AlertDialog.Builder(context).apply {
                    setTitle(context.getString(R.string.delete_subject_title_confirmation))
                    setMessage(context.getString(R.string.delete_subject_subtitle_confirmation))
                    setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                        mataKuliahListFragmentViewModel.removeSubjectTugasKuliah(id)
                    }
                    setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

                    }
                }.create().show()
            }
        })
        binding.subjectListInDashboard.adapter = adapter

        mataKuliahListFragmentViewModel.subject.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        mataKuliahListFragmentViewModel.selectSubject.observe(viewLifecycleOwner, Observer {
            it?.let{
                val intent = Intent(this.activity, ViewTugasKuliahActivity::class.java)
                this.startActivity(intent)
                mataKuliahListFragmentViewModel.afterSubjectTugasKuliahClicked()
            }
        })

        binding.mataKuliahListFragmentViewModel = mataKuliahListFragmentViewModel
        binding.lifecycleOwner = this

        val manager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(binding.subjectListInDashboard.context,
            manager.orientation)
        binding.subjectListInDashboard.addItemDecoration(dividerItemDecoration)

        binding.subjectListInDashboard.layoutManager = manager
        // Inflate the layout for this fragment
        return binding.root
    }

}