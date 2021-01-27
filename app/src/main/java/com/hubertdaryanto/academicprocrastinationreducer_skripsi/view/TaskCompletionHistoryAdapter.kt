package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TaskCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListAgendaHeaderBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTaskCompletionHistoryBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTugasBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListTaskCompletionHistoryHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskCompletionHistoryDate(d: String, c: String): TaskCompletionHistoryListItemType {
    var date = d
    var count = c
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }
}

interface TaskCompletionHistoryListItemType {
    val ITEM_VIEW_TYPE_HEADER: Int
        get() = 0
    val ITEM_VIEW_TYPE_ITEM: Int
        get() = 1
    val ITEM_VIEW_TYPE_ITEM_FINISHED: Int
        get() = 2

    fun getType(): Int
}

class TaskCompletionHistoryAdapter(val clickListener: TaskCompletionHistoryListener): ListAdapter<TaskCompletionHistoryDataItem, RecyclerView.ViewHolder>(TaskCompletionHistoryDiffCallback()) {
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
                val item = getItem(position) as TaskCompletionHistoryDataItem.TaskCompletionHistoryList
                holder.bind(item.taskCompletionHistoryListItemType as TaskCompletionHistory, clickListener)
            }
            is TextViewHolder -> {
                val item = getItem(position) as TaskCompletionHistoryDataItem.TaskCompletionHistoryList
                holder.bind(item.taskCompletionHistoryListItemType as TaskCompletionHistoryDate)
            }
        }
    }
    fun addHeaderAndSubmitList(list: List<TaskCompletionHistoryListItemType>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(TaskCompletionHistoryDataItem.Header)
                else -> list.map {
                    TaskCompletionHistoryDataItem.TaskCompletionHistoryList(it)
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
        fun bind(item: TaskCompletionHistoryDate)
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
        fun bind(item: TaskCompletionHistory, clickListener: TaskCompletionHistoryListener) {
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

class TaskCompletionHistoryDiffCallback : DiffUtil.ItemCallback<TaskCompletionHistoryDataItem>() {
    override fun areItemsTheSame(oldItem: TaskCompletionHistoryDataItem, newItem: TaskCompletionHistoryDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TaskCompletionHistoryDataItem, newItem: TaskCompletionHistoryDataItem): Boolean {
        return oldItem == newItem
    }
}

class TaskCompletionHistoryListener(val clickListener: (TaskCompletionHistoryId: Long) -> Unit)
{
    fun onClick(taskCompletionHistory: TaskCompletionHistory) = clickListener(taskCompletionHistory.bindToTugasKuliahId)
}

sealed class TaskCompletionHistoryDataItem {
    abstract val id: Long
    open var count: Long = 0
    open var counter: Long = 0
    abstract val type: Int
    object Header: TaskCompletionHistoryDataItem(){
        override val id = Long.MIN_VALUE
        override val type = 0
    }
    data class TaskCompletionHistoryList(val taskCompletionHistoryListItemType: TaskCompletionHistoryListItemType): TaskCompletionHistoryDataItem()
    {
        fun getLong(): Long{
            if (taskCompletionHistoryListItemType is TaskCompletionHistory)
            {
                count = taskCompletionHistoryListItemType.taskCompletionHistoryId
                return count + counter
            }
            else
            {
                counter = counter + 1
                return counter
            }
        }
        override val id: Long = getLong()
        override val type: Int = taskCompletionHistoryListItemType.getType()
    }
}