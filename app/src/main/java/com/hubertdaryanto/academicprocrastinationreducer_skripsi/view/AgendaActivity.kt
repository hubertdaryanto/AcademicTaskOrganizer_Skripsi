package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.anggrayudi.storage.SimpleStorage
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ActivityMainBinding


// Constants
// The authority for the sync adapter's content provider
const val AUTHORITY = "com.example.academictaskorganizer_skripsi.provider"
// An account type, in the form of a domain name
const val ACCOUNT_TYPE = "com.example.academictaskorganizer_skripsi"
// The account name
const val ACCOUNT = "Testing Account"

private const val STORAGE_PERMISSION_CODE = 1

private const val READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE"

class AgendaActivity : AppCompatActivity() {
    // Instance fields
    private lateinit var mAccount: Account
    private lateinit var binding: ActivityMainBinding
    private lateinit var storage: SimpleStorage

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
                    .show();
            }
            else {
                Toast.makeText(this,
                    "Akses Penyimpanan Tidak Diizinkan",
                    Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the placeholder account

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
//        ReceiveBtn.setOnClickListener {
//            mAccount = loadSavedLoginInfo()
//
//        }

//        setupSimpleStorage()

        //to change some view to visible, use this code: "<<xml view id name>>.visibility = View.VISIBLE"
        val navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

//    private fun setupSimpleStorage() {
//        storage = SimpleStorage(this)
//        storage.storageAccessCallback = object : StorageAccessCallback {
//            override fun onRootPathNotSelected(rootPath: String, rootStorageType: StorageType) {
//                AlertDialog.Builder(applicationContext).apply {
//                    setTitle("Test request access to storage")
//                    setMessage("Please select $rootPath")
//                    .setNegativeButton(android.R.string.cancel) { _, _ ->
//
//                    }
//                    .setPositiveButton("Yes") { _, _ ->
//                        storage.requestStorageAccess(REQUEST_CODE_STORAGE_ACCESS, rootStorageType)
//                    }
//                }
//                    .create().show()
//            }
//
//            override fun onCancelledByUser() {
//                Toast.makeText(baseContext, "Cancelled by user", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onStoragePermissionDenied() {
//                /*
//                Request runtime permissions for Manifest.permission.WRITE_EXTERNAL_STORAGE
//                and Manifest.permission.READ_EXTERNAL_STORAGE
//                */
//            }
//
//            override fun onRootPathPermissionGranted(root: DocumentFile) {
//                Toast.makeText(baseContext, "Storage access has been granted for ${root.storageId}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), null)
    }

    private fun loadSavedLoginInfo(): Account
    {
        val accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        return Account(
            ACCOUNT,
            ACCOUNT_TYPE
        ).also {
            if (accountManager.getAccountsByType(ACCOUNT_TYPE).isEmpty())
            {
                val intent = Intent(this, BinusmayaLoginActivity::class.java)
                startActivity(intent)
            }
            else {
                mAccount = accountManager.getAccountsByType(ACCOUNT_TYPE).get(0)
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
//                val settingsBundle = Bundle().apply {
//                    putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
//                    putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
//                }
//                /*
//                 * Request the sync for the default account, authority, and
//                 * manual sync settings
//                 */
//                ContentResolver.requestSync(mAccount,
//                    AUTHORITY, settingsBundle)
                Log.d("0", accountManager.getPassword(mAccount))//ini password
                Log.d("0", mAccount.name)//ini username
            }
        }
    }
}