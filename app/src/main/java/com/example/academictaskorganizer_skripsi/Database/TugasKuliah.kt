package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.util.*

@Entity(tableName = "TugasKuliah", foreignKeys = arrayOf(ForeignKey(entity = ToDoList::class, parentColumns = arrayOf("ToDoListId"), childColumns = arrayOf("ToDoListId"), onDelete = CASCADE, onUpdate = CASCADE),
    ForeignKey(entity = Subject::class, parentColumns = arrayOf("SubjectId"), childColumns = arrayOf("SubjectId"), onDelete = CASCADE, onUpdate = CASCADE), ForeignKey(entity = ImageForSubject::class, parentColumns = arrayOf("ImageId"), childColumns = arrayOf("ImageId"))))
data class TugasKuliah(
    @ColumnInfo(name = "SubjectId") var SubjectId: Int?,
    @ColumnInfo(name = "TugasKuliahName") var TugasKuliahName: String?,
    @ColumnInfo(name = "Deadline") var Deadline: Date?,
    @ColumnInfo(name = "ToDoListId") var ToDoListId: Int?,
    @ColumnInfo(name = "isFinished") var isFinished: Boolean?,
    @ColumnInfo(name = "notes") var notes: String?,
    @ColumnInfo(name = "ImageId") var ImageId: Int?,
    @ColumnInfo(name = "fromBinusmayaId") var fromBinusmayaId: Int?
):Serializable
{
    @ColumnInfo(name = "TugasKuliahId")
    @PrimaryKey(autoGenerate = true)
    var TugasKuliahId: Int = 0

    constructor() : this(0,"", Date(0),0,false,"",-1,-1)
    fun isSubjectAlreadyAvailable()
    {

    }
}

@Entity(tableName = "Image")
data class ImageForSubject(
    @ColumnInfo(name = "ImageName") val ImageName: String?
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var ImageId: Int = 0
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

//    @TypeConverter
//    fun fromIntToArrayInt(value: Int?): IntArray? {
//        return value?.let { IntArray(it) }
//    }
//
//    @TypeConverter
//    fun ArrayIntToInt(value: IntArray?): Int? {
//        if (value != null) {
//            return value.last()
//        }
//        else
//        {
//            return 0
//        }
//    }
//
//    @TypeConverter
//    fun ArrayStringToString(value: Array<String>?): String?
//    {
//        if (value != null) {
//            return value.last()
//        }
//        else
//        {
//            return ""
//        }
//    }
//
//    @TypeConverter
//    fun fromStringToArrayString(value: String?): Array<String>? {
//        return Array(1,{"\(value)"})
//    }
}