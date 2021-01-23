package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import kotlinx.android.synthetic.main.binusmaya_login_activity.*
import org.json.JSONObject

class BinusmayaLoginActivity: AppCompatActivity() {
    private lateinit var mAccount: Account
    private lateinit var password: String
    private lateinit var username: String
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.binusmaya_login_activity)
        loginBtn.setOnClickListener {
            username = loginUserId.getText().toString()
            password = loginPassword.getText().toString()

            verifyUsernameAndPassword(username = username, password = password)

        }
    }

    /**
     * Create a new placeholder account for the sync adapter = tambahkan akun
     */
    private fun loginAndSaveLoginInfo(username: String, password: String): Account {
        val accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        return Account(username,
            ACCOUNT_TYPE
        ).also { newAccount ->
            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (accountManager.addAccountExplicitly(newAccount, password, null)) {
                /*
                 * If you don't set android:syncable="true" in
                 * in your <provider> element in the manifest,
                 * then call context.setIsSyncable(account, AUTHORITY, 1)
                 * here.
                 */

            } else {
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun verifyUsernameAndPassword(username: String, password: String)
    {
        if (checkNetworkConnection())
        {
            // Instantiate the cache
            val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
            val network = BasicNetwork(HurlStack())

// Instantiate the RequestQueue with the cache and network. Start the queue.
            val requestQueue = RequestQueue(cache, network).apply {
                start()
            }
            val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, "http://192.168.64.3/login_volleyAPI/checkLogin.php", Response.Listener<String>()
            {response ->
                // Do something with the response
                val jsonObject: JSONObject = JSONObject(response)
                val resp = jsonObject.getString("server_response")
                if (resp == "[{\"status\":\"OK\"}]")
                {
                    mAccount = loginAndSaveLoginInfo(username, password)
                    Toast.makeText(applicationContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {error ->
                // Handle error
            })
            {
                override fun getParams(): Map<String, String> {
                    val parameters = HashMap<String, String>()
                    parameters["username"] = username
                    parameters["password"] = password
                    return parameters
                }
            }
            requestQueue.add(stringRequest)
        }
        else
        {
            Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetworkConnection():Boolean
    {
        val connectivityManager: ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (connectivityManager.activeNetwork != null)
    }
}