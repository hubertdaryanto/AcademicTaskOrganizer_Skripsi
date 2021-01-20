package com.example.academictaskorganizer_skripsi.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.academictaskorganizer_skripsi.view.TaskCompletionHistoryListItemType
import com.example.academictaskorganizer_skripsi.view.TugasKuliahListItemType
import java.io.Serializable

@Entity(tableName = "TugasKuliah"
    ,foreignKeys = [
//        ForeignKey(entity = ToDoList::class, parentColumns = arrayOf("toDoListId"), childColumns = arrayOf("tugasToDoListId"), onDelete = CASCADE, onUpdate = CASCADE),
        ForeignKey(entity = Subject::class, parentColumns = arrayOf("subjectId"), childColumns = arrayOf("tugasSubjectId"), onDelete = CASCADE, onUpdate = CASCADE)
//        ,
//        ForeignKey(entity = ImageForTugas::class, parentColumns = arrayOf("imageId"), childColumns = arrayOf("tugasImageId"), onDelete = CASCADE, onUpdate = CASCADE)
                   ]
//    ,
//    indices = [Index("tugasKuliahId")]
 )
data class TugasKuliah(
    @ColumnInfo(name = "tugasSubjectId")
    var tugasSubjectId: Long,
    @ColumnInfo(name = "tugasKuliahName")
    var tugasKuliahName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
//    @ColumnInfo(name = "tugasToDoListId")
//    var tugasToDoListId: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean,
    @ColumnInfo(name = "notes")
    var notes: String,
    var finishCommitment: Long,
    var updatedAt: Long
//    ,
//    @ColumnInfo(name = "tugasImageId")
//    var tugasImageId: Long
//    var fromBinusmayaId: Long = -1
): TugasKuliahListItemType, Serializable
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tugasKuliahId")
    var tugasKuliahId: Long = 0
    override fun getType(): Int {
        if (isFinished)
        {
            return ITEM_VIEW_TYPE_ITEM_FINISHED
        }
        else
        {
            return ITEM_VIEW_TYPE_ITEM
        }
    }

    companion object{
        const val KEY_ID = "id"
    }
}

@Entity(tableName = "Image", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = CASCADE, onUpdate = CASCADE)
])
data class ImageForTugas(
    @ColumnInfo(name = "bindToTugasKuliahId")
    var bindToTugasKuliahId: Long,
    @ColumnInfo(name = "imageName")
    var imageName: String
)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageId")
    var imageId: Long = 0
}

@Entity(tableName = "TaskCompletionHistory", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = CASCADE, onUpdate = CASCADE)
])
data class TaskCompletionHistory(
    @ColumnInfo(name = "bindToTugasKuliahId")
    var bindToTugasKuliahId: Long,
    @ColumnInfo(name = "type")
    var activityType: String
): TaskCompletionHistoryListItemType
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskCompletionHistoryId")
    var taskCompletionHistoryId: Long = System.currentTimeMillis()
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_ITEM
    }
}

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
        parentColumn = "tugasKuliahId",
        entityColumn = "bindToTugasKuliahId"
    )
    val toDoList: List<ToDoList>
)

data class TugasKuliahWithImageForTugas(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "bindToTugasKuliahId"
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