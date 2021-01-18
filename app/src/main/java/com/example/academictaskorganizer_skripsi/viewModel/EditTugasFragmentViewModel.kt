package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academictaskorganizer_skripsi.components.addNewItem
import com.example.academictaskorganizer_skripsi.components.notifyObserver
import com.example.academictaskorganizer_skripsi.components.removeItemAt
import com.example.academictaskorganizer_skripsi.database.*
import com.example.academictaskorganizer_skripsi.services.AlarmScheduler
import kotlinx.coroutines.*

class EditTugasFragmentViewModel(application: Application, dataSource: allQueryDao): ViewModel()  {
    val database = dataSource

    var _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    val _toDoList = MutableLiveData<MutableList<ToDoList>>()
    val toDoList: LiveData<MutableList<ToDoList>>
        get() = _toDoList

    private val _imageList = MutableLiveData<MutableList<ImageForTugas>>()
    val imageList: LiveData<MutableList<ImageForTugas>>
        get() = _imageList
    /** Coroutine variables */

    /**
     * viewModelJob allows us to caancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    private val _subjectText = MutableLiveData<String>()
    val SubjectText: LiveData<String>
        get() = _subjectText

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
            _toDoList.value = database.loadToDoListsByTugasKuliahId(id)
            _imageList.value = database.loadImagesByTugasKuliahId(id)
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



    fun addToDoListItem(toDoList: ToDoList)
    {
        _toDoList.addNewItem(toDoList)
        _toDoList.notifyObserver()
    }

    fun removeToDoListItem(id: Long)
    {
        val realId = _toDoList.value?.get(id.toInt())?.toDoListId
        _toDoList.removeItemAt(id.toInt())
        _toDoList.notifyObserver()
        uiScope.launch {
            if (realId != null) {
                database.deleteToDoList(realId.toLong())
            }
        }
    }

    fun removeImageItem(id: Long)
    {
        val realId = _imageList.value?.get(id.toInt())?.imageId
        _imageList.removeItemAt(id.toInt())
        _imageList.notifyObserver()
        uiScope.launch {
            if (realId != null) {
                database.deleteImage(realId.toLong())
            }
        }
    }

    fun addImageItem(image: ImageForTugas){
        _imageList.addNewItem(image)
        _imageList.notifyObserver()
    }


    fun onTimePickerClicked() {
        _showTimePicker.value = true
    }

    fun onDatePickerClicked() {
        _showDatePicker.value = true
    }

    fun doneLoadTimePicker()
    {
        _showTimePicker.value = null
    }

    fun doneLoadDatePicker()
    {
        _showDatePicker.value = null
    }

    fun onAddTugasKuliahClicked2()
    {
        _editTugasKuliahNavigation.value = true
    }

    fun updateTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        uiScope.launch {
            if (!tugasKuliah.isFinished) {
                AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah)
                tugasKuliah.updatedAt = System.currentTimeMillis()
                AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
            } else {
                AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah)
                database.insertTaskCompletionHistory(TaskCompletionHistory(bindToTugasKuliahId = tugasKuliah.tugasKuliahId, activityType = "Tugas Kuliah Selesai"))
            }
           database.updateTugas(tugasKuliah)

            if (toDoList.value != null)
            {
                toDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    database.insertToDoList(it)
                }
            }

            if (imageList.value != null)
            {
                imageList.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    database.insertImage(it)
                }
            }

        }
    }

    fun deleteTugasKuliah(context: Context)
    {
        AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah.value!!)
        uiScope.launch {
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
        toDoList.value?.get(id.toInt())?.toDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
    }

    fun updateToDoListIsFinished(id: Long, isFinished: Boolean)
    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
        toDoList.value?.get(id.toInt())?.isFinished = isFinished
    }
}