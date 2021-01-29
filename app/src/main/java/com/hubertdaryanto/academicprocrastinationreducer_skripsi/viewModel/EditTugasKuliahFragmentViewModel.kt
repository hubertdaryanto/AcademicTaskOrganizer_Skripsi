package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.addNewItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.notifyObserver
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.removeItemAt
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditTugasKuliahFragmentViewModel(application: Application, dataSource: allQueryDao): ViewModel()  {
    val database = dataSource

    var toDoListIdToBeDeleted: ArrayList<Long> = arrayListOf()
    var imageIdToBeDeleted: ArrayList<Long> = arrayListOf()

    var _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    var _tugasKuliahBefore = MutableLiveData<TugasKuliah>()
    val tugasKuliahBefore: LiveData<TugasKuliah>
        get() = _tugasKuliahBefore

    val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoList

    private val _imageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val imageListKuliahImage: LiveData<MutableList<TugasKuliahImage>>
        get() = _imageList

    val _toDoListBefore = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoListBefore: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoListBefore

    private val _imageListBefore = MutableLiveData<MutableList<TugasKuliahImage>>()
    val imageListBeforeKuliahImage: LiveData<MutableList<TugasKuliahImage>>
        get() = _imageListBefore
    /** Coroutine variables */

    /**
     * viewModelJob allows us to caancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    private val _subjectText = MutableLiveData<String>()
    val SubjectText: LiveData<String>
        get() = _subjectText

    private val _subjectTextBefore = MutableLiveData<String>()
    val SubjectTextBefore: LiveData<String>
        get() = _subjectTextBefore

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _editTugasKuliahNavigation = MutableLiveData<Boolean?>()
    val editTugasKuliahNavigation: LiveData<Boolean?>
        get() = _editTugasKuliahNavigation

    private val _showTimePicker = MutableLiveData<Boolean?>()
    val showTimePicker: LiveData<Boolean?>
        get() = _showTimePicker

    private val _showDatePicker = MutableLiveData<Boolean?>()
    val showDatePicker: LiveData<Boolean?>
        get() = _showDatePicker

    private val _showTimePicker2 = MutableLiveData<Boolean?>()
    val showTimePicker2: LiveData<Boolean?>
        get() = _showTimePicker2

    private val _showDatePicker2 = MutableLiveData<Boolean?>()
    val showDatePicker2: LiveData<Boolean?>
        get() = _showDatePicker2

    private val _showSubjectDialog = MutableLiveData<Boolean?>()
    val showSubjectDialog: LiveData<Boolean?>
        get() = _showSubjectDialog

    private val _addToDoList = MutableLiveData<Boolean?>()
    val addToDoList: LiveData<Boolean?>
        get() = _addToDoList

    private val _addImage = MutableLiveData<Boolean?>()
    val addImage: LiveData<Boolean?>
        get() = _addImage


    //can be useful when edit tugas
    private val _toDoListId = MutableLiveData<Long?>()
    val toDoListId: LiveData<Long?>
        get() = _toDoListId

    private val _imageId = MutableLiveData<Long?>()
    val imageId: LiveData<Long?>
        get() = _imageId

    private val _onIsFinishedClicked = MutableLiveData<Boolean?>()
    val onIsFinishedClicked: LiveData<Boolean?>
        get() = _onIsFinishedClicked

    fun onIsFinishedCheckBoxClicked(){
        _onIsFinishedClicked.value = true
    }

    fun updateIsFinishedStatus(bool: Boolean)
    {
        _tugasKuliah.value!!.isFinished = bool
    }

    fun afterIsFinishedClicked()
    {
        _onIsFinishedClicked.value = null
    }

    fun loadTugasKuliah(id: Long)
    {
        uiScope.launch {
            _tugasKuliah.value = database.loadTugasKuliahById(id)
            _tugasKuliahBefore.value = database.loadTugasKuliahById(id)
            _toDoList.value = database.loadToDoListsByTugasKuliahId(id)
            _imageList.value = database.loadImagesByTugasKuliahId(id)
            _toDoListBefore.value = database.loadToDoListsByTugasKuliahId(id)
            _imageListBefore.value = database.loadImagesByTugasKuliahId(id)
            _subjectTextBefore.value = tugasKuliah.value?.tugasSubjectId?.let { database.loadSubjectName(it) }
        }
    }

    fun onShowSubjectDialogClicked()
    {
        _showSubjectDialog.value = true
    }

    fun doneLoadSubjectDialog()
    {
        _showSubjectDialog.value = null
    }

    fun onAddToDoListClicked()
    {
        _addToDoList.value = true
    }

    fun afterAddToDoListClicked()
    {
        _addToDoList.value = null
    }

    fun onAddImageClicked()
    {
        _addImage.value = true
    }

    fun afterAddImageClicked()
    {
        _addImage.value = null
    }



    fun addToDoListItem(tugasKuliahToDoList: TugasKuliahToDoList)
    {
        _toDoList.addNewItem(tugasKuliahToDoList)
        _toDoList.notifyObserver()
    }

    fun removeToDoListItem(id: Long)
    {
        val realId: Long? = _toDoList.value?.get(id.toInt())?.toDoListId
        _toDoList.removeItemAt(id.toInt())
        _toDoList.notifyObserver()
        if (realId != null) {
            toDoListIdToBeDeleted.add(realId)
        }
//        uiScope.launch {
//            if (realId != null) {
//                database.deleteToDoList(realId.toLong())
//            }
//        }
    }

    fun removeImageItem(id: Long)
    {
        val realId = _imageList.value?.get(id.toInt())?.imageId
        _imageList.removeItemAt(id.toInt())
        _imageList.notifyObserver()
        if (realId != null) {
            imageIdToBeDeleted.add(realId)
        }
//        uiScope.launch {
//            if (realId != null) {
//                database.deleteImage(realId.toLong())
//            }
//        }
    }

    fun addImageItem(imageKuliahImage: TugasKuliahImage){
        _imageList.addNewItem(imageKuliahImage)
        _imageList.notifyObserver()
    }

    fun onTimePickerClicked() {
        _showTimePicker.value = true
    }

    fun onDatePickerClicked() {
        _showDatePicker.value = true
    }

    fun onTimePickerClicked2() {
        _showTimePicker2.value = true
    }

    fun onDatePickerClicked2() {
        _showDatePicker2.value = true
    }

    fun doneLoadTimePicker()
    {
        _showTimePicker.value = null
    }

    fun doneLoadDatePicker()
    {
        _showDatePicker.value = null
    }

    fun doneLoadTimePicker2()
    {
        _showTimePicker2.value = null
    }

    fun doneLoadDatePicker2()
    {
        _showDatePicker2.value = null
    }


    fun onAddTugasKuliahClicked2()
    {
        _editTugasKuliahNavigation.value = true
    }

    fun updateTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        uiScope.launch {
            var mTaskCompletionHistory = database.getTaskCompletionHistoryByTugasKuliahId(tugasKuliah.tugasKuliahId)
            if (mTaskCompletionHistory == null) {
                mTaskCompletionHistory =
                    TugasKuliahCompletionHistory(
                        bindToTugasKuliahId = tugasKuliah.tugasKuliahId,
                        activityType = "Tugas Kuliah Selesai"
                    )
            }
            if (!tugasKuliah.isFinished) {
                AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah)
                tugasKuliah.updatedAt = System.currentTimeMillis()
                AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(context, tugasKuliah)
                database.deleteTaskCompletionHistory(mTaskCompletionHistory)
                //is different because the id is different, maybe if it loaded first, it will be deleted
            } else {
                AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah)
                mTaskCompletionHistory.activityType = "Tugas Kuliah Selesai"
                mTaskCompletionHistory.tugasKuliahCompletionHistoryId = System.currentTimeMillis()
                database.insertTaskCompletionHistory(mTaskCompletionHistory)
            }
           database.updateTugas(tugasKuliah)
            if (toDoListIdToBeDeleted.count() != 0)
            {
                for (i in toDoListIdToBeDeleted)
                {
                    database.deleteToDoList(i)
                }
            }

            if (imageIdToBeDeleted.count() != 0)
            {
                for (i in imageIdToBeDeleted)
                {
                    database.deleteImage(i)
                }
            }

            if (tugasKuliahToDoList.value != null)
            {
                tugasKuliahToDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    database.insertToDoList(it)
                }
            }

            if (imageListKuliahImage.value != null)
            {
                imageListKuliahImage.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    database.insertImage(it)
                }
            }

        }
    }

    fun deleteTugasKuliah(context: Context)
    {

        uiScope.launch {
            AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah.value!!)
            database.deleteTugas(tugasKuliah.value!!)
            //to do list sama image yang terkait harus di remove juga
        }

    }

    fun doneNavigating() {
        _editTugasKuliahNavigation.value = null
    }

    fun convertSubjectIdToSubjectName(id: Long){
        uiScope.launch {
            _subjectText.value = database.loadSubjectName(id)
        }
    }

    fun onSubjectNameChanged()
    {
        _subjectText.value = null
    }

    fun onToDoListClicked(toDoListId: Long) {
        _toDoListId.value = toDoListId
    }

    fun onGambarClicked(imageId: Long){
        _imageId.value = imageId
    }

    fun afterClickToDoList()
    {
        _toDoListId.value = null
    }

    fun afterClickGambar()
    {
        _imageId.value = null
    }

//    fun updateToDoList(id: Long, data: String, isFinished: Boolean)
//    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
//    }

    fun updateToDoListName(id: Long, data: String)
    {
        tugasKuliahToDoList.value?.get(id.toInt())?.toDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
    }

    fun updateToDoListIsFinished(id: Long, isFinished: Boolean)
    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
        _toDoList.value?.get(id.toInt())?.isFinished = isFinished
        _toDoList.notifyObserver()
    }
}