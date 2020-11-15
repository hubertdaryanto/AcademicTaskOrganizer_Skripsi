package com.example.academictaskorganizer_skripsi.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.academictaskorganizer_skripsi.Database.TugasKuliah
import com.example.academictaskorganizer_skripsi.R
import kotlinx.android.synthetic.main.tugas_layout.view.*

class TugasAdapter(val TugasKuliah: List<TugasKuliah>): RecyclerView.Adapter<TugasAdapter.TugasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasViewHolder {
        return TugasViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tugas_layout, parent, false)
        )
    }

    override fun getItemCount() = TugasKuliah.size

    override fun onBindViewHolder(holder: TugasViewHolder, position: Int) {
        holder.view.text_view_tugas.text = TugasKuliah[position].TugasKuliahName

        holder.view.setOnClickListener {
            val action = HomeFragmentDirections.actionEditTugas()
            action.tugasKuliah = TugasKuliah[position]
            Navigation.findNavController(it).navigate(action)
        }
    }
    class TugasViewHolder(val view: View): RecyclerView.ViewHolder(view)
}