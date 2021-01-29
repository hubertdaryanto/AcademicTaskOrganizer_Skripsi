package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTaskCompletionHistoryBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListTaskCompletionHistoryHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TugasKuliahCompletionHistoryDate(d: String, c: String): TugasKuliahCompletionHistoryListItemType {
    var date = d
    var count = c
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }
}

interface TugasKuliahCompletionHistoryListItemType {
    val ITEM_VIEW_TYPE_HEADER: Int
        get() = 0
    val ITEM_VIEW_TYPE_ITEM: Int
        get() = 1
    val ITEM_VIEW_TYPE_ITEM_FINISHED: Int
        get() = 2

    fun getType(): Int
}

class TugasKuliahCompletionHistoryAdapter(val clickListener: TugasKuliahCompletionHistoryListener): ListAdapter<TugasKuliahCompletionHistoryDataItem, RecyclerView.ViewHolder>(TugasKuliahCompletionHistoryDiffCallback()) {
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

class TugasKuliahCompletionHistoryDiffCallback : DiffUtil.ItemCallback<TugasKuliahCompletionHistoryDataItem>() {
    override fun areItemsTheSame(oldItem: TugasKuliahCompletionHistoryDataItem, newItem: TugasKuliahCompletionHistoryDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TugasKuliahCompletionHistoryDataItem, newItem: TugasKuliahCompletionHistoryDataItem): Boolean {
        return oldItem == newItem
    }
}

class TugasKuliahCompletionHistoryListener(val clickListener: (TaskCompletionHistoryId: Long) -> Unit)
{
    fun onClick(taskCompletionHistory: TugasKuliahCompletionHistory) = clickListener(taskCompletionHistory.bindToTugasKuliahId)
}

sealed class TugasKuliahCompletionHistoryDataItem {
    abstract val id: Long
    open var count: Long = 0
    open var counter: Long = 0
    abstract val type: Int
    object Header: TugasKuliahCompletionHistoryDataItem(){
        override val id = Long.MIN_VALUE
        override val type = 0
    }
    data class TugasKuliahCompletionHistoryList(val tugasKuliahCompletionHistoryListItemType: TugasKuliahCompletionHistoryListItemType): TugasKuliahCompletionHistoryDataItem()
    {
        fun getLong(): Long{
            if (tugasKuliahCompletionHistoryListItemType is TugasKuliahCompletionHistory)
            {
                count = tugasKuliahCompletionHistoryListItemType.tugasKuliahCompletionHistoryId
                return count + counter
            }
            else
            {
                counter = counter + 1
                return counter
            }
        }
        override val id: Long = getLong()
        override val type: Int = tugasKuliahCompletionHistoryListItemType.getType()
    }
}