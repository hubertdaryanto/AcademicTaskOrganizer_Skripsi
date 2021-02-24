package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityMainBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.MainActivityViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {
    // Instance fields
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    fun checkPermission(permission: String, requestCode: Int) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat
                .requestPermissions(
                    this, arrayOf(permission),
                    requestCode
                )
        } else {
            Toast
                .makeText(
                    this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.count() > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                    "Akses Penyimpanan Diizinkan",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                Toast.makeText(this,
                    "Akses Penyimpanan Tidak Diizinkan",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED)
//        {
//            // Permission is not granted
//            android.app.AlertDialog.Builder(this).apply {
//                setTitle(context.getString(R.string.allow_access_storage_title))
//                setMessage(context.getString(R.string.allow_access_storage))
//
//                setPositiveButton("OK") { _, _ ->
//                    checkPermission(READ_STORAGE_PERMISSION, STORAGE_PERMISSION_CODE)
//                }
//            }.create().show()
//        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val application = requireNotNull(this).application

        val tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        val tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        val viewModelFactory = MainActivityViewModelFactory(tugasKuliahDataSource, tugasKuliahToDoListDataSource, application)

        mainActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.mataKuliahListFragment)
        )
        navController = navHostFragment.findNavController()


        binding.floatingActionButton.setOnClickListener {
//            navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddTugasFragment2())
            val intent = Intent(this, AddTugasKuliahActivity::class.java)
            this.startActivity(intent)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigation.menu.getItem(1).isEnabled = false
        binding.bottomNavigation.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.home_nav_host_fragment), null)
    }

}