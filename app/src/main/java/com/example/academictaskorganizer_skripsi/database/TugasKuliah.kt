package com.example.academictaskorganizer_skripsi.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "TugasKuliah"
    ,foreignKeys = [ForeignKey(entity = ToDoList::class, parentColumns = arrayOf("toDoListId"), childColumns = arrayOf("tugasToDoListId"), onDelete = CASCADE, onUpdate = CASCADE),
        ForeignKey(entity = Subject::class, parentColumns = arrayOf("subjectId"), childColumns = arrayOf("tugasSubjectId"), onDelete = CASCADE, onUpdate = CASCADE),
        ForeignKey(entity = ImageForTugas::class, parentColumns = arrayOf("imageId"), childColumns = arrayOf("tugasImageId"), onDelete = CASCADE, onUpdate = CASCADE)
                   ],
    indices = [Index("tugasKuliahId")]
 )
data class TugasKuliah(
    @ColumnInfo(name = "tugasKuliahId")
    @PrimaryKey(autoGenerate = true)
    val tugasKuliahId: Long = 0L,
    @ColumnInfo(name = "tugasSubjectId")
    var tugasSubjectId: Long,
    @ColumnInfo(name = "tugasKuliahName")
    var tugasKuliahName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
    @ColumnInfo(name = "tugasToDoListId")
    var tugasToDoListId: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean,
    @ColumnInfo(name = "notes")
    var notes: String,
    @ColumnInfo(name = "tugasImageId")
    var tugasImageId: Long
//    var fromBinusmayaId: Long = -1
)

@Entity(tableName = "Image")
data class ImageForTugas(
    @ColumnInfo(name = "imageId")
    @PrimaryKey(autoGenerate = true)
    var imageId: Long = 0L,
    @ColumnInfo(name = "imageName")
    val imageName: String
)

data class SubjectAndTugasKuliah(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "tugasSubjectId"
    )
    val tugasKuliah: List<TugasKuliah>
)

data class TugasKuliahWithToDoList(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasToDoListId",
        entityColumn = "toDoListId"
    )
    val toDoList: List<ToDoList>
)

data class TugasKuliahWithImageForTugas(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasImageId",
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