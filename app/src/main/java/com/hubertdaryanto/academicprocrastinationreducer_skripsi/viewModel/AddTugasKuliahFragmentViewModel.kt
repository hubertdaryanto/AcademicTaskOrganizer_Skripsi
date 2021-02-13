package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import kotlinx.coroutines.*

class AddTugasKuliahFragmentViewModel(application: Application, tugasKuliahDataSource: tugasKuliahDao, subjectTugasKuliahDataSource: subjectTugasKuliahDao): ViewModel() {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val subjectTugasKuliahDatabase = subjectTugasKuliahDataSource

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoList

    private val _imageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val imageListKuliahImage: LiveData<MutableList<TugasKuliahImage>>
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

    private val _showSubjectTugasKuliahDialog = MutableLiveData<Boolean?>()
    val showSubjectTugasKuliahDialog: LiveData<Boolean?>
        get() = _showSubjectTugasKuliahDialog

    private val _addTugasKuliahToDoList = MutableLiveData<Boolean?>()
    val addTugasKuliahToDoList: LiveData<Boolean?>
        get() = _addTugasKuliahToDoList

    private val _addTugasKuliahImage = MutableLiveData<Boolean?>()
    val addTugasKuliahImage: LiveData<Boolean?>
        get() = _addTugasKuliahImage


    //can be useful when edit tugas
    private val _tugasKuliahToDoListId = MutableLiveData<Long?>()
    val tugasKuliahToDoListId: LiveData<Long?>
        get() = _tugasKuliahToDoListId

    private val _tugasKuliahImageId = MutableLiveData<Long?>()
    val tugasKuliahImageId: LiveData<Long?>
        get() = _tugasKuliahImageId

    fun onShowSubjectDialogClicked()
    {
        _showSubjectTugasKuliahDialog.value = true
    }

    fun doneLoadSubjectDialog()
    {
        _showSubjectTugasKuliahDialog.value = null
    }

    fun onAddToDoListClicked()
    {
        _addTugasKuliahToDoList.value = true
    }

    fun afterAddToDoListClicked()
    {
        _addTugasKuliahToDoList.value = null
    }

    fun onAddImageClicked()
    {
        _addTugasKuliahImage.value = true
    }

    fun afterAddImageClicked()
    {
        _addTugasKuliahImage.value = null
    }



    fun addToDoListItem(tugasKuliahToDoList: TugasKuliahToDoList)
    {
        _toDoList.addNewItem(tugasKuliahToDoList)
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
        if (tugasKuliahToDoList.value != null)
        {
            shared_data.mTugasKuliahToDoList = tugasKuliahToDoList.value!!
        }
        if (imageListKuliahImage.value != null)
        {
            shared_data.mTugasKuliahImages = imageListKuliahImage.value!!
        }
//        uiScope.launch {
//            tugasKuliah.updatedAt = System.currentTimeMillis()
//            var tugasKuliaId = database.insertTugas(tugasKuliah)
//            //insert to do list and image in here too
//            tugasKuliah.tugasKuliahId = tugasKuliaId
//            AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
//            //to do list id masih 0 meskipun data ada 2, harusnya data pertama 0, data kedua 1
//            if (toDoList.value != null)
//            {
//                toDoList.value!!.toList().forEach {
//
//                    it.bindToTugasKuliahId = tugasKuliaId
//                    database.insertToDoList(it)
//                }
//            }
//
//            if (imageList.value != null)
//            {
//                imageList.value!!.toList().forEach{
//                    it.bindToTugasKuliahId = tugasKuliaId
//                    database.insertImage(it)
//                }
//            }
//
//            //how to insert multiple data to database in one time?
////            database.insertToDoLists(Collections.unmodifiableList(toDoList.value))
////            database.insertImages(Collections.unmodifiableList(imageList.value))
//
//        }
    }

    private suspend fun insert(tugasKuliah: TugasKuliah) {
        withContext(Dispatchers.IO) {
            tugasKuliahDatabase.insertTugasKuliah(tugasKuliah)
        }
    }

    fun doneNavigating() {
        _addTugasKuliahNavigation.value = null
    }

    fun convertSubjectIdToSubjectName(id: Long){
        uiScope.launch {
            shared_data.mSubjectId = id
            shared_data.mSubjectAtAddTugasFragment = subjectTugasKuliahDatabase.loadSubjectTugasKuliahNameById(id)
            _subjectText.value = shared_data.mSubjectAtAddTugasFragment
        }
    }

    fun onSubjectNameChanged()
    {
        _subjectText.value = null
    }

    fun onToDoListClicked(toDoListId: Long) {
        _tugasKuliahToDoListId.value = toDoListId
    }

    fun onGambarClicked(imageId: Long){
        _tugasKuliahImageId.value = imageId
    }

    fun afterClickToDoList()
    {
        _tugasKuliahToDoListId.value = null
    }

    fun afterClickGambar()
    {
        _tugasKuliahImageId.value = null
    }

//    fun updateToDoList(id: Long, data: String, isFinished: Boolean)
//    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
//    }

    fun updateToDoListName(id: Long, data: String)
    {
        tugasKuliahToDoList.value?.get(id.toInt())?.tugasKuliahToDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
    }

    fun updateToDoListIsFinished(id: Long, isFinished: Boolean)
    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
        tugasKuliahToDoList.value?.get(id.toInt())?.isFinished = isFinished
    }

}