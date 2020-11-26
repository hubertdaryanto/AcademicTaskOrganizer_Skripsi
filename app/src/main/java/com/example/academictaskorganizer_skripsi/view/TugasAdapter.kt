package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.databinding.ListItemTugasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class TugasAdapter(val clickListener: TugasKuliahListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(TugasKuliahDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.view.text_view_tugas.text = TugasKuliah[position].TugasKuliahName
//
//        holder.view.setOnClickListener {
//            val action = HomeFragmentDirections.actionEditTugas()
//            action.tugasKuliah = TugasKuliah[position]
//            Navigation.findNavController(it).navigate(action)
//        }
        when (holder) {
            is ViewHolder ->
            {
                val item = getItem(position) as DataItem.TugasKuliahItem
                holder.bind(item.tugas, clickListener)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<TugasKuliah>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {
                    DataItem.TugasKuliahItem(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.TugasKuliahItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTugasBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: TugasKuliah, clickListener: TugasKuliahListener) {
            binding.tugas = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTugasBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TugasKuliahDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class TugasKuliahListener(val clickListener: (TugasKuliahId: Long) -> Unit)
{
    fun onClick(tugas: TugasKuliah) = clickListener(tugas.tugasKuliahId)
}

sealed class DataItem {
    abstract val id: Long
    data class TugasKuliahItem(val tugas: TugasKuliah): DataItem(){
        override val id = tugas.tugasKuliahId
    }

    object Header: DataItem(){
        override val id = Long.MIN_VALUE
    }


}