package com.example.academictaskorganizer_skripsi.Components

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyConnection(context: Context) {

    private lateinit var requestQueue: RequestQueue
    private var context: Context

    companion object{
        private var vInstance: VolleyConnection? = null
        fun getInstance(context: Context) =
            vInstance ?: synchronized(this) {
                vInstance ?: VolleyConnection(context).also {
                    vInstance = it
                }
            }
    }

    init {
        this.context = context
        requestQueue = getReqestQueue()
    }

    private fun getReqestQueue(): RequestQueue
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }



}