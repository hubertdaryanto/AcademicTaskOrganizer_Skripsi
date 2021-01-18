package com.example.academictaskorganizer_skripsi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.databinding.FragmentTaskCompletionHistoryBinding
import com.example.academictaskorganizer_skripsi.viewModel.TaskCompletionHistoryFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.TaskCompletionHistoryFragmentViewModelFactory

class TaskCompletionHistoryFragment: BaseFragment() {

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

        val adapter = TaskCompletionHistoryAdapter(TaskCompletionHistoryListener { tugasKuliahId ->
            taskCompletionHistoryFragmentViewModel.onTaskCompletionHistoryClicked(tugasKuliahId)
        })
        binding.taskCompletionHistoryList.adapter = adapter

        taskCompletionHistoryFragmentViewModel.taskCompletionHistories.observe(viewLifecycleOwner, Observer {
            it?.let {
                val date = taskCompletionHistoryFragmentViewModel.getTaskCompletionHistoryDate()
                adapter.addHeaderAndSubmitList(date)
            }
        })

        taskCompletionHistoryFragmentViewModel.navigateToViewTaskCompletionHistoryDetails.observe(viewLifecycleOwner, Observer {
            it.let {
//                this.findNavController().navigate(TaskCompletionHistoryDirections.actionTaskCompletionHistoryFragmentToTaskCompletionHistoryDetailFragment(it))
//                taskCompletionHistoryFragmentViewModel.onTaskCompletionHistoryNavigated()
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
}