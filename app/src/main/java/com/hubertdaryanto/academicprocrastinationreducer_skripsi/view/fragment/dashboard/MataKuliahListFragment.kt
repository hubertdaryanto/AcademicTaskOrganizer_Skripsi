package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R

class MataKuliahListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mata_kuliah_list, container, false)
    }

}