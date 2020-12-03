package com.example.academictaskorganizer_skripsi.view

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.anggrayudi.storage.SimpleStorage
import com.anggrayudi.storage.callback.StorageAccessCallback
import com.anggrayudi.storage.file.StorageType
import com.anggrayudi.storage.file.storageId
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.databinding.ActivityMainBinding

// Constants
// The authority for the sync adapter's content provider
const val AUTHORITY = "com.example.academictaskorganizer_skripsi.provider"
// An account type, in the form of a domain name
const val ACCOUNT_TYPE = "com.example.academictaskorganizer_skripsi"
// The account name
const val ACCOUNT = "Testing Account"

class AgendaActivity : AppCompatActivity() {
    // Instance fields
    private lateinit var mAccount: Account
    private lateinit var binding: ActivityMainBinding
    private lateinit var storage: SimpleStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the placeholder account

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
                Log.d("0",accountManager.getPassword(mAccount))//ini password
                Log.d("0", mAccount.name)//ini username
            }
        }
    }
}