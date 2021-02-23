package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityAddTugasKuliahBinding

class AddTugasKuliahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTugasKuliahBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_tugas_kuliah)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.add_tugas_kuliah_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        navController.setGraph(R.navigation.add_tugas_kuliah_navigation, intent.extras)
    }
}