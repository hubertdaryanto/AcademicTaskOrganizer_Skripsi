package com.example.academictaskorganizer_skripsi.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class tugas(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "fromBinusmayaId") val fromBinusmayaId: Int?
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun isSubjectAlreadyAvailable()
    {

    }
}