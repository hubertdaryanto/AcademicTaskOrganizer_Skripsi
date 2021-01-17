package com.example.academictaskorganizer_skripsi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.databinding.FragmentTaskCompletionHistoryBinding
import com.example.academictaskorganizer_skripsi.viewModel.HomeFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.HomeFragmentViewModelFactory
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

        val adapter = TaskCompletionHistoryAdapter(TugasKuliahListener { tugasKuliahId ->
            taskCompletionHistoryFragmentViewModel.onTugasKuliahClicked(tugasKuliahId)
        })
        binding.tugasList.adapter = adapter

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}