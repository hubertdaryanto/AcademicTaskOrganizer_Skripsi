package com.example.academictaskorganizer_skripsi.services

import android.accounts.Account
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class SyncAdapter @JvmOverloads constructor(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean = false, val mContentResolver: ContentResolver = context.contentResolver): AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    var DATA_JSON_STRING = ""
    var data_json_string = ""
    var countData = 0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {


        try {
            val url = URL("http://192.168.64.3/login_volleyAPI/readTugas.php")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream = httpURLConnection.inputStream
            val bufferedReader = BufferedReader(inputStream as InputStreamReader)
            val stringBuilder = StringBuilder()

            while ((bufferedReader.readLine()) != null)
            {
                stringBuilder.append(DATA_JSON_STRING + "\n")
            }

            bufferedReader.close()
            inputStream.close()
            httpURLConnection.disconnect()
            //tambah ke database

//            readDataFromServer()
        }
        catch (e: MalformedURLException)
        {

        }
        catch (e: IOException)
        {

        }


    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun readDataFromServer()
//    {
//        var tugasTitle = ""
//        var fromBinusmayaId: Int = -1
//        if(checkNetworkConnection())
//        {
//            try {
//                val o = JSONObject(data_json_string)
//                val serverResponse = o.getJSONArray("servver_response")
//
//                while (countData < serverResponse.length()){
//                    val jsonObject = serverResponse.getJSONObject(countData)
//                    tugasTitle = jsonObject.getString("name")
//                    fromBinusmayaId = jsonObject.getInt("id")
//                    TugasDatabase(context).getTugasDao().insertAll(tugas(tugasTitle, fromBinusmayaId))
//                    countData++
//                }
//            }
//            catch (e: JSONException)
//            {
//
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun checkNetworkConnection(context: Context):Boolean
//    {
//        val connectivityManager: ConnectivityManager = getSystemService(context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return (connectivityManager.activeNetwork != null)
//    }
}