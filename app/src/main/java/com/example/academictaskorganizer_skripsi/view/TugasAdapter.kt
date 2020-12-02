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

val ITEM_VIEW_TYPE_HEADER = 0
val ITEM_VIEW_TYPE_ITEM = 1

class TugasAdapter(val clickListener: TugasKuliahListener): ListAdapter<TugasKuliahDataItem, RecyclerView.ViewHolder>(TugasKuliahDiffCallback()) {

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
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahItem
                holder.bind(item.tugas, clickListener)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<TugasKuliah>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(TugasKuliahDataItem.Header)
                else -> listOf(TugasKuliahDataItem.Header) + list.map {
                    TugasKuliahDataItem.TugasKuliahItem(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TugasKuliahDataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is TugasKuliahDataItem.TugasKuliahItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_agenda_header, parent, false)
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

class TugasKuliahDiffCallback : DiffUtil.ItemCallback<TugasKuliahDataItem>() {
    override fun areItemsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem == newItem
    }

}

class TugasKuliahListener(val clickListener: (TugasKuliahId: Long) -> Unit)
{
    fun onClick(tugas: TugasKuliah) = clickListener(tugas.tugasKuliahId)
}

sealed class TugasKuliahDataItem {
    abstract val id: Long
    data class TugasKuliahItem(val tugas: TugasKuliah): TugasKuliahDataItem(){
        override val id = tugas.tugasKuliahId
    }

    object Header: TugasKuliahDataItem(){
        override val id = Long.MIN_VALUE
    }


}