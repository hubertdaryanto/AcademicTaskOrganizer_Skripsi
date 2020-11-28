package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.ToDoList
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.databinding.ListItemToDoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class ToDoListAdapter(val clickListener: ToDoListListener): ListAdapter<ToDoListDataItem, RecyclerView.ViewHolder>(ToDoListDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder ->
            {
                val item = getItem(position) as ToDoListDataItem.ToDoListItem
                holder.bind(item.toDoList, clickListener)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<ToDoList>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(ToDoListDataItem.Header)
                else -> listOf(ToDoListDataItem.Header) + list.map {
                    ToDoListDataItem.ToDoListItem(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ToDoListDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is ToDoListDataItem.ToDoListItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_to_do_list_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemToDoListBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: ToDoList, clickListener: ToDoListListener) {
            binding.toDoList = item
            binding.toDoListClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemToDoListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ToDoListDiffCallback : DiffUtil.ItemCallback<ToDoListDataItem>() {
    override fun areItemsTheSame(oldItem: ToDoListDataItem, newItem: ToDoListDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ToDoListDataItem, newItem: ToDoListDataItem): Boolean {
        return oldItem == newItem
    }

}

class ToDoListListener(val clickListener: (ToDoListId: Long) -> Unit)
{
    fun onClick(toDoList: ToDoList) = clickListener(toDoList.toDoListId)
}

sealed class ToDoListDataItem {
    abstract val id: Long
    data class ToDoListItem(val toDoList: ToDoList): ToDoListDataItem(){
        override val id = toDoList.toDoListId
    }

    object Header: ToDoListDataItem(){
        override val id = Long.MIN_VALUE
    }


}