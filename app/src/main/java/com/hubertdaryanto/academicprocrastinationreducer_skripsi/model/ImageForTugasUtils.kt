package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

object ImageForTugasUtils {
    fun openImageInGallery(context: Context, uri: Uri)
    {

        if (Build.VERSION.SDK_INT in 24..28)
        {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")
            context.startActivity(intent)
        }
        else
        {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")
            context.startActivity(intent)
        }
    }
}