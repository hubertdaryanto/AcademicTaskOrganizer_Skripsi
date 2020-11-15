package com.example.academictaskorganizer_skripsi.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.academictaskorganizer_skripsi.Database.TugasDatabase
import com.example.academictaskorganizer_skripsi.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view_tugas.setHasFixedSize(true)
        recycler_view_tugas.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        launch {
            context?.let {
                val tugas = TugasDatabase(it).getTugasDao().getAllSortedByName()
                recycler_view_tugas.adapter = TugasAdapter(tugas)
            }

        }
        floatingActionButton.setOnClickListener {
            val action = HomeFragmentDirections.actionEditTugas()
            Navigation.findNavController(it).navigate(action)
        }
    }
}