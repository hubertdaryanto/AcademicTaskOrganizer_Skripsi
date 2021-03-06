package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityOnboardingBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahDao

const val STORAGE_PERMISSION_CODE = 1

const val READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE"

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
//    val onboardingActivityViewModel: OnboardingActivityViewModel by viewModels()

    private lateinit var subjectTugasKuliahDataSource: subjectTugasKuliahDao
    private lateinit var tugasKuliahDataSource: tugasKuliahDao
    private lateinit var navController: NavController

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

    fun showPermissionDialog()
    {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
//            // Permission is not granted
//            android.app.AlertDialog.Builder(this).apply {
//                setTitle(context.getString(R.string.allow_access_storage_title))
//                setMessage(context.getString(R.string.allow_access_storage))
//
//                setPositiveButton("OK") { _, _ ->
//                    checkPermission(READ_STORAGE_PERMISSION, STORAGE_PERMISSION_CODE)
//                }
//            }.create().show()
            checkPermission(READ_STORAGE_PERMISSION, STORAGE_PERMISSION_CODE)
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)

        val application = requireNotNull(this).application

        tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        subjectTugasKuliahDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao

//        val viewModelFactory = OnboardingActivityViewModelFactory(tugasKuliahDataSource, subjectTugasKuliahDataSource, application)
//
//        onboardingActivityViewModel =
//            ViewModelProvider(this, viewModelFactory).get(OnboardingActivityViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.onboarding_nav_host_fragment) as NavHostFragment


        navController = navHostFragment.findNavController()

        navController.setGraph(R.navigation.onboarding_navigation, intent.extras)



    }
}