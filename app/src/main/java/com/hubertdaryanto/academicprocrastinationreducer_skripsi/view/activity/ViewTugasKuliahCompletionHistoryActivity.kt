package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityViewTugasKuliahCompletionHistoryBinding

class ViewTugasKuliahCompletionHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewTugasKuliahCompletionHistoryBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Riwayat Penyelesaian Tugas")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_tugas_kuliah_completion_history)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.view_tugas_kuliah_completion_history_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        navController.setGraph(R.navigation.view_tugas_kuliah_completion_history_navigation, intent.extras)
    }
}