package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentTaskCompletionHistoryBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TaskCompletionHistoryFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TaskCompletionHistoryFragmentViewModelFactory

class TaskCompletionHistoryFragment: Fragment() {

//    val application = requireNotNull(this.activity).application
//
//    val dataSource = AppDatabase.getInstance(application).getAllQueryListDao
//    val viewModelFactory = TaskCompletionHistoryFragmentViewModelFactory(dataSource, application)
//
//    val taskCompletionHistoryFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(TaskCompletionHistoryFragmentViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskCompletionHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_completion_history, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).getAllQueryListDao
        val viewModelFactory = TaskCompletionHistoryFragmentViewModelFactory(dataSource, application)

        val taskCompletionHistoryFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(TaskCompletionHistoryFragmentViewModel::class.java)

        taskCompletionHistoryFragmentViewModel.loadTaskCompletionHistory()
        val adapter = TaskCompletionHistoryAdapter(TaskCompletionHistoryListener { tugasKuliahId ->
            taskCompletionHistoryFragmentViewModel.onTaskCompletionHistoryClicked(tugasKuliahId)
        })
        binding.taskCompletionHistoryList.adapter = adapter

        taskCompletionHistoryFragmentViewModel.taskCompletionHistories.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.count() == 0)
                {
                    binding.textView3.visibility = View.VISIBLE
                }
                else
                {
                    binding.textView3.visibility = View.GONE
                    val date = taskCompletionHistoryFragmentViewModel.getTaskCompletionHistoryDate()
                    adapter.addHeaderAndSubmitList(date)
                }
            }
        })

        taskCompletionHistoryFragmentViewModel.navigateToViewTaskCompletionHistoryDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(TaskCompletionHistoryFragmentDirections.actionTaskCompletionHistoryFragmentToEditTugasFragment(it))
//                this.findNavController().navigate(TaskCompletionHistoryDirections.actionTaskCompletionHistoryFragmentToTaskCompletionHistoryDetailFragment(it))
                taskCompletionHistoryFragmentViewModel.onTaskCompletionHistoryNavigated()
            }
        })

        binding.taskCompletionHistoryFragmentViewModel = taskCompletionHistoryFragmentViewModel
        binding.setLifecycleOwner(this)

        val manager = GridLayoutManager(activity, 1)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0 -> return 1
                    else -> return 1
                }
            }
        }
        binding.taskCompletionHistoryList.layoutManager = manager

        return binding.root
    }

    override fun onStop() {
//        taskCompletionHistoryFragmentViewModel.onTaskCompletionHistoryNavigated()
        super.onStop()
    }
}