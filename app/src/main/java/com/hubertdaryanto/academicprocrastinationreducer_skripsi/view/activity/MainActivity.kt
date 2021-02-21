package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityMainBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard.HomeFragment
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard.MataKuliahListFragment

private const val STORAGE_PERMISSION_CODE = 1

private const val READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE"

class MainActivity : AppCompatActivity() {
    // Instance fields
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var mataKuliahListFragment: MataKuliahListFragment

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
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            android.app.AlertDialog.Builder(this).apply {
                setTitle(context.getString(R.string.allow_access_storage_title))
                setMessage(context.getString(R.string.allow_access_storage))

                setPositiveButton("OK") { _, _ ->
                    checkPermission(READ_STORAGE_PERMISSION, STORAGE_PERMISSION_CODE)
                }
            }.create().show()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        homeFragment = HomeFragment()
        mataKuliahListFragment = MataKuliahListFragment()

        makeCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_mata_kuliah_list -> makeCurrentFragment(mataKuliahListFragment)
            }
            true
        }

        //to change some view to visible, use this code: "<<xml view id name>>.visibility = View.VISIBLE"
        val navController = Navigation.findNavController(this, R.id.onboardingFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.onboardingFragment), null)
    }

}