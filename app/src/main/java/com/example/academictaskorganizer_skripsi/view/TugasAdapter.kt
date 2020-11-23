package com.example.academictaskorganizer_skripsi.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.databinding.ListItemTugasBinding

class TugasAdapter(val clickListener: TugasKuliahListener): ListAdapter<TugasKuliah, TugasAdapter.TugasViewHolder>(TugasKuliahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasViewHolder {
        return TugasViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TugasViewHolder, position: Int) {
//        holder.view.text_view_tugas.text = TugasKuliah[position].TugasKuliahName
//
//        holder.view.setOnClickListener {
//            val action = HomeFragmentDirections.actionEditTugas()
//            action.tugasKuliah = TugasKuliah[position]
//            Navigation.findNavController(it).navigate(action)
//        }
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
    class TugasViewHolder private constructor(val binding: ListItemTugasBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: TugasKuliah, clickListener: TugasKuliahListener) {
            binding.tugas = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TugasViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTugasBinding.inflate(layoutInflater, parent, false)
                return TugasViewHolder(binding)
            }
        }
    }
}

class TugasKuliahDiffCallback : DiffUtil.ItemCallback<TugasKuliah>() {
    override fun areItemsTheSame(oldItem: TugasKuliah, newItem: TugasKuliah): Boolean {
        return oldItem.tugasKuliahId == newItem.tugasKuliahId
    }

    override fun areContentsTheSame(oldItem: TugasKuliah, newItem: TugasKuliah): Boolean {
        return oldItem == newItem
    }

}

class TugasKuliahListener(val clickListener: (TugasKuliahId: Long) -> Unit)
{
    fun onClick(tugas: TugasKuliah) = clickListener(tugas.tugasKuliahId)
}