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

class AddTugasFragmentViewModel(application: Application, dataSource: allQueryDao): ViewModel() {
    val database = dataSource

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
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

    private val _addTugasKuliahNavigation = MutableLiveData<Boolean?>()
    val addTugasKuliahNavigation: LiveData<Boolean?>
        get() = _addTugasKuliahNavigation

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
        _toDoList.removeItemAt(id.toInt())
        _toDoList.notifyObserver()
    }

    fun removeImageItem(id: Long)
    {
        _imageList.removeItemAt(id.toInt())
        _imageList.notifyObserver()
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

            _addTugasKuliahNavigation.value = true


    }

    fun addTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        uiScope.launch {
            tugasKuliah.updatedAt = System.currentTimeMillis()
            var tugasKuliaId = database.insertTugas(tugasKuliah)
            //insert to do list and image in here too
            tugasKuliah.tugasKuliahId = tugasKuliaId
            AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
            //to do list id masih 0 meskipun data ada 2, harusnya data pertama 0, data kedua 1
            if (toDoList.value != null)
            {
                toDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliaId
                    database.insertToDoList(it)
                }
            }

            if (imageList.value != null)
            {
                imageList.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliaId
                    database.insertImage(it)
                }
            }

            //how to insert multiple data to database in one time?
//            database.insertToDoLists(Collections.unmodifiableList(toDoList.value))
//            database.insertImages(Collections.unmodifiableList(imageList.value))

        }
    }

    private suspend fun insert(tugasKuliah: TugasKuliah) {
        withContext(Dispatchers.IO) {
            database.insertTugas(tugasKuliah)
        }
    }

    fun doneNavigating() {
        _addTugasKuliahNavigation.value = null
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