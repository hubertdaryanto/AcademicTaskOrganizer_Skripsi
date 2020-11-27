package com.example.academictaskorganizer_skripsi.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.util.*

@Entity(tableName = "TugasKuliah"
//    ,foreignKeys = arrayOf(ForeignKey(entity = ToDoList::class, parentColumns = arrayOf("ToDoListId"), childColumns = arrayOf("ToDoListId"), onDelete = CASCADE, onUpdate = CASCADE),
//    ForeignKey(entity = Subject::class, parentColumns = arrayOf("SubjectId"), childColumns = arrayOf("SubjectId"), onDelete = CASCADE, onUpdate = CASCADE), ForeignKey(entity = ImageForSubject::class, parentColumns = arrayOf("ImageId"), childColumns = arrayOf("ImageId")))
)
data class TugasKuliah(
    @PrimaryKey(autoGenerate = true)
    var tugasKuliahId: Long,
    @ColumnInfo(name = "subjectId")
    var subjectId: Long,
    @ColumnInfo(name = "tugasKuliahName")
    var tugasKuliahName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
    @ColumnInfo(name = "toDoListId")
    var toDoListId: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean,
    @ColumnInfo(name = "notes")
    var notes: String,

    var imageId: Long
//    var fromBinusmayaId: Long = -1
)

@Entity(tableName = "Image")
data class ImageForTugas(
    @PrimaryKey(autoGenerate = true)
    var imageId: Long,
    @ColumnInfo(name = "imageName")
    val imageName: String
)

data class TugasKuliahAndSubject(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "subjectId"
    )
    val subject: Subject
)

data class TugasKuliahWithToDoList(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "toDoListId"
    )
    val toDoList: List<ToDoList>
)

data class TugasKuliahWithImageForTugas(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "imageId"
    )
    val imageForTugas: List<ImageForTugas>
)

//class Converters {
//    @TypeConverter
//    fun fromTimestamp(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): Long? {
//        return date?.time?.toLong()
//    }
//
////    @TypeConverter
////    fun fromIntToArrayInt(value: Int?): IntArray? {
////        return value?.let { IntArray(it) }
////    }
////
////    @TypeConverter
////    fun ArrayIntToInt(value: IntArray?): Int? {
////        if (value != null) {
////            return value.last()
////        }
////        else
////        {
////            return 0
////        }
////    }
////
////    @TypeConverter
////    fun ArrayStringToString(value: Array<String>?): String?
////    {
////        if (value != null) {
////            return value.last()
////        }
////        else
////        {
////            return ""
////        }
////    }
////
////    @TypeConverter
////    fun fromStringToArrayString(value: String?): Array<String>? {
////        return Array(1,{"\(value)"})
////    }
//}