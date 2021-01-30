package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTaskCompletionHistoryBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListTaskCompletionHistoryHeaderBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistoryDataItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistoryDate
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TugasKuliahCompletionHistoryDiffCallback
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TugasKuliahCompletionHistoryListItemType
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TugasKuliahCompletionHistoryListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TugasKuliahCompletionHistoryAdapter(val clickListener: TugasKuliahCompletionHistoryListener): ListAdapter<TugasKuliahCompletionHistoryDataItem, RecyclerView.ViewHolder>(
    TugasKuliahCompletionHistoryDiffCallback()
) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> TextViewHolder.from(parent)
            1 -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as TugasKuliahCompletionHistoryDataItem.TugasKuliahCompletionHistoryList
                holder.bind(item.tugasKuliahCompletionHistoryListItemType as TugasKuliahCompletionHistory, clickListener)
            }
            is TextViewHolder -> {
                val item = getItem(position) as TugasKuliahCompletionHistoryDataItem.TugasKuliahCompletionHistoryList
                holder.bind(item.tugasKuliahCompletionHistoryListItemType as TugasKuliahCompletionHistoryDate)
            }
        }
    }
    fun addHeaderAndSubmitList(list: List<TugasKuliahCompletionHistoryListItemType>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(TugasKuliahCompletionHistoryDataItem.Header)
                else -> list.map {
                    TugasKuliahCompletionHistoryDataItem.TugasKuliahCompletionHistoryList(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class TextViewHolder private constructor(val binding: ListTaskCompletionHistoryHeaderBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: TugasKuliahCompletionHistoryDate)
        {
            binding.taskCompletionHistoryDate = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListTaskCompletionHistoryHeaderBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
            }
        }
    }
    class ViewHolder private constructor(val binding: ListItemTaskCompletionHistoryBinding): RecyclerView.ViewHolder(
        binding.root
    )
    {
        fun bind(item: TugasKuliahCompletionHistory, clickListener: TugasKuliahCompletionHistoryListener) {
            binding.taskCompletionHistory = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskCompletionHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

