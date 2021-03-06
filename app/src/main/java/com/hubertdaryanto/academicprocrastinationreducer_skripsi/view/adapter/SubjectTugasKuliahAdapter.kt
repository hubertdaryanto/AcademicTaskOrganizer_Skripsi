package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemSubjectBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemSubjectEmptyBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliahDataItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahDiffCallback
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val ITEM_VIEW_TYPE_HEADER: Int
    get() = 0
val ITEM_VIEW_TYPE_ITEM: Int
    get() = 1
val ITEM_VIEW_TYPE_EMPTY_ITEM: Int
    get() = 2

class  SubjectTugasKuliahAdapter(val clickTugasKuliahListener: SubjectTugasKuliahListener, val subjectTugasKuliahInterface: SubjectTugasKuliahInterface): ListAdapter<SubjectTugasKuliahDataItem, RecyclerView.ViewHolder>(
    SubjectTugasKuliahDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_EMPTY_ITEM -> ViewHolder2.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder ->
            {
                val item = getItem(position) as SubjectTugasKuliahDataItem.SubjectTugasKuliahItem
                if (shared_data.fromFragment.contains("MataKuliahListFragment"))
                {
                    holder.binding.subjectDeleteBtn.visibility = View.VISIBLE
                    holder.binding.subjectEditBtn.visibility = View.VISIBLE
                    holder.binding.subjectDeleteBtn.setOnClickListener {
                        subjectTugasKuliahInterface.onRemoveItem(item.subjectTugasKuliah.subjectTugasKuliahId)
                    }
                    holder.binding.subjectEditBtn.setOnClickListener {
                        subjectTugasKuliahInterface.onEditItem(item.subjectTugasKuliah.subjectTugasKuliahId)
                    }
                }
                else
                {
                    holder.binding.subjectDeleteBtn.visibility = View.GONE
                    holder.binding.subjectEditBtn.visibility = View.GONE
                }

                holder.bind(item.subjectTugasKuliah, clickTugasKuliahListener)
            }
            is ViewHolder2 ->
            {
                val item = getItem(position) as SubjectTugasKuliahDataItem.AddSubject
                holder.bind(clickTugasKuliahListener)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<SubjectTugasKuliah>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(SubjectTugasKuliahDataItem.Header)
                else -> list.map {
                    SubjectTugasKuliahDataItem.SubjectTugasKuliahItem(it)
                } + listOf(SubjectTugasKuliahDataItem.AddSubject)
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SubjectTugasKuliahDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is SubjectTugasKuliahDataItem.SubjectTugasKuliahItem -> ITEM_VIEW_TYPE_ITEM
            is SubjectTugasKuliahDataItem.AddSubject -> ITEM_VIEW_TYPE_EMPTY_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_subject_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemSubjectBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: SubjectTugasKuliah, clickTugasKuliahListener: SubjectTugasKuliahListener) {
            binding.subject = item
            binding.subjectClickListener = clickTugasKuliahListener
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

    class ViewHolder2 private constructor(val binding: ListItemSubjectEmptyBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(clickTugasKuliahListener: SubjectTugasKuliahListener) {
            binding.subject = SubjectTugasKuliah("Tambah")
            binding.subjectClickListener = clickTugasKuliahListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder2 {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSubjectEmptyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder2(binding)
            }
        }
    }
}