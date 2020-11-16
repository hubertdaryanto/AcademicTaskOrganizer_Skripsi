package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(tableName = "TugasKuliah")
data class TugasKuliah(
    @ColumnInfo(name = "SubjectId") val SubjectId: Int?,
    @ColumnInfo(name = "TugasKuliahName") val TugasKuliahName: String?,
    @ColumnInfo(name = "Deadline") val Deadline: Date?,
    @ColumnInfo(name = "ToDoListId") val ToDoListId: IntArray?,
    @ColumnInfo(name = "isFinished") val isFinished: Boolean?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "images") val images: String?,
    @ColumnInfo(name = "fromBinusmayaId") val fromBinusmayaId: Int?
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var TugasKuliahId: Int = 0

    fun isSubjectAlreadyAvailable()
    {

    }
}

data class TugasKuliahAndSubject(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "TugasKuliahId",
        entityColumn = "SubjectId"
    )
    val subject: Subject
)

data class TugasKuliahWithToDoList(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "TugasKuliahId",
        entityColumn = "ToDoListId"
    )
    val toDoList: List<ToDoList>
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromIntToArrayInt(value: Int?): IntArray? {
        return value?.let { IntArray(it) }
    }

    @TypeConverter
    fun ArrayIntToInt(value: IntArray?): Int? {
        if (value != null) {
            return value.last()
        }
        else
        {
            return 0
        }
    }
}