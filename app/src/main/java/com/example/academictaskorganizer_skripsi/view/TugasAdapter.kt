package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.databinding.ListAgendaHeaderBinding
import com.example.academictaskorganizer_skripsi.databinding.ListItemTugasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TugasKuliahDate(d: String): TugasKuliahListItemType {
    var date = d

    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }

}



interface TugasKuliahListItemType {
    val ITEM_VIEW_TYPE_HEADER: Int
        get() = 0
    val ITEM_VIEW_TYPE_ITEM: Int
        get() = 1

    fun getType(): Int
}

val ITEM_VIEW_TYPE_HEADER: Int
    get() = 0
val ITEM_VIEW_TYPE_ITEM: Int
    get() = 1

class TugasAdapter(val clickListener: TugasKuliahListener): ListAdapter<TugasKuliahDataItem, RecyclerView.ViewHolder>(
    TugasKuliahDiffCallback()
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
//        holder.view.text_view_tugas.text = TugasKuliah[position].TugasKuliahName
//
//        holder.view.setOnClickListener {
//            val action = HomeFragmentDirections.actionEditTugas()
//            action.tugasKuliah = TugasKuliah[position]
//            Navigation.findNavController(it).navigate(action)
//        }
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahList
                holder.bind(item.tugasKuliahListItemType as TugasKuliah, clickListener)
            }

            is TextViewHolder ->
            {
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahList
                holder.bind(item.tugasKuliahListItemType as TugasKuliahDate)
            }

        }

    }

    fun addHeaderAndSubmitList(list: List<TugasKuliahListItemType>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(TugasKuliahDataItem.Header)
                else -> list.map {
                    TugasKuliahDataItem.TugasKuliahList(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)) {
//            is TugasKuliahDataItem.Header -> ITEM_VIEW_TYPE_HEADER
//            is TugasKuliahDataItem.TugasKuliahItem -> ITEM_VIEW_TYPE_ITEM
//        }
//        return when (getItem(position)) {
//            is TugasKuliahDataItem.TugasKuliahList as TugasKuliahDate ->
//            is TugasKuliahDataItem.TugasKuliahItem -> ITEM_VIEW_TYPE_ITEM
//        }
        return getItem(position).type
    }


    class TextViewHolder private constructor(val binding: ListAgendaHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TugasKuliahDate)
        {
            binding.tugasDate = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListAgendaHeaderBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTugasBinding): RecyclerView.ViewHolder(
        binding.root
    )
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
    open var count: Long = 0
    abstract val type: Int
//    data class TugasKuliahItem(val tugas: TugasKuliah): TugasKuliahDataItem(){
//        override val id = tugas.tugasKuliahId
//    }
//
    object Header: TugasKuliahDataItem(){
        override val id = Long.MIN_VALUE
         override val type = 0
}

    data class TugasKuliahList(val tugasKuliahListItemType: TugasKuliahListItemType): TugasKuliahDataItem()
    {
        fun getLong(): Long{
            if (tugasKuliahListItemType is TugasKuliah)
            {
                return tugasKuliahListItemType.tugasKuliahId + count
            }
            else
            {
                count = count + 1
                return count
            }

        }


        override val id: Long = getLong()
        override val type: Int = tugasKuliahListItemType.getType()
//        override val id: Long = {
//            fun getLong(): Long {
//                if (tugasKuliahListItemType is TugasKuliah)
//                {
//                    return tugasKuliahListItemType.tugasKuliahId
//                }
//            }
//            return getLong()
//        }
    }



}