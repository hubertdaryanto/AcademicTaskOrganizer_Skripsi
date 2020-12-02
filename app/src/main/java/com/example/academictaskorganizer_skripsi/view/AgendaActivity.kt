package com.example.academictaskorganizer_skripsi.view

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the placeholder account

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        ReceiveBtn.setOnClickListener {
//            mAccount = loadSavedLoginInfo()
//
//        }


        //to change some view to visible, use this code: "<<xml view id name>>.visibility = View.VISIBLE"
        val navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

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