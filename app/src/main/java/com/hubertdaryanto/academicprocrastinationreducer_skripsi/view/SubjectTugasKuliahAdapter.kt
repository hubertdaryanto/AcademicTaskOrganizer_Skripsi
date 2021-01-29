package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemSubjectBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SubjectInterface{
    fun onRemoveItem(id: Long)
}

val ITEM_VIEW_TYPE_HEADER: Int
    get() = 0
val ITEM_VIEW_TYPE_ITEM: Int
    get() = 1

class SubjectTugasKuliahAdapter(val clickListener: SubjectListener, val subjectInterface: SubjectInterface): ListAdapter<SubjectDataItem, RecyclerView.ViewHolder>(SubjectDiffCallback()) {

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
                val item = getItem(position) as SubjectDataItem.SubjectItem
                if (SubjectFrom.fromFragment.contains("AddTugasFragment"))
                {
                    holder.binding.subjectDeleteBtn.visibility = View.VISIBLE
                    holder.binding.subjectDeleteBtn.setOnClickListener {
                        subjectInterface.onRemoveItem(item.subjectTugasKuliah.subjectId)
                    }
                }
                else
                {
                    holder.binding.subjectDeleteBtn.visibility = View.GONE
                }

                holder.bind(item.subjectTugasKuliah, clickListener)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<SubjectTugasKuliah>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(SubjectDataItem.Header)
                else -> list.map {
                    SubjectDataItem.SubjectItem(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SubjectDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is SubjectDataItem.SubjectItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_subject_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemSubjectBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: SubjectTugasKuliah, clickListener: SubjectListener) {
            binding.subject = item
            binding.subjectClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSubjectBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SubjectDiffCallback : DiffUtil.ItemCallback<SubjectDataItem>() {
    override fun areItemsTheSame(oldItem: SubjectDataItem, newItem: SubjectDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: SubjectDataItem, newItem: SubjectDataItem): Boolean {
        return oldItem == newItem
    }

}

class SubjectListener(val clickListener: (subjectId: Long) -> Unit)
{
    fun onClick(subjectTugasKuliah: SubjectTugasKuliah) = clickListener(subjectTugasKuliah.subjectId)
}

sealed class SubjectDataItem {
    abstract val id: Long
    data class SubjectItem(val subjectTugasKuliah: SubjectTugasKuliah): SubjectDataItem(){
        override val id = subjectTugasKuliah.subjectId
    }

    object Header: SubjectDataItem(){
        override val id = Long.MIN_VALUE
    }


}