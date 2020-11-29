package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.academictaskorganizer_skripsi.database.ImageForTugas
import com.example.academictaskorganizer_skripsi.databinding.ListItemImageForTugasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageForTugasAdapter(val clickListener: ImageForTugasListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(ImageForTugasDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder)
        {
            is ViewHolder ->
            {
                val item = getItem(position) as DataItem.ImageForTugasItem
                holder.bind(item.ImageForTugas, clickListener)
            }
        }
    }

    fun updateList(list: List<ImageForTugas>?) {
        adapterScope.launch {
            val items = list?.map {
                DataItem.ImageForTugasItem(it)
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    class ViewHolder private constructor(val binding: ListItemImageForTugasBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageForTugas, clickListener: ImageForTugasListener)
        {
            binding.image = item
            binding.imageClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemImageForTugasBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ImageForTugasDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ImageForTugasListener(val clickListener: (ImageForTugasId: Long) -> Unit)
{
    fun onClick(ImageForTugas: ImageForTugas) = clickListener(ImageForTugas.imageId)
}

//DataItem class ada di ToDoListAdapter.kt