package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academictaskorganizer_skripsi.components.addNewItem
import com.example.academictaskorganizer_skripsi.components.notifyObserver
import com.example.academictaskorganizer_skripsi.database.*
import kotlinx.coroutines.*

class AddTugasFragmentViewModel(application: Application, dataSource: tugasDatabaseDao): ViewModel() {
    val database = dataSource

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    private val _toDoList = MutableLiveData<MutableList<ToDoList>>()
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

    private val _string = MutableLiveData<String>()
    val string: LiveData<String>
        get() = _string

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
    private val _toDoListId = MutableLiveData<Int?>()
    val toDoListId: LiveData<Int?>
        get() = _toDoListId

    private val _imageId = MutableLiveData<Int?>()
    val imageId: LiveData<Int?>
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
//        viewModelScope.launch {

            _addTugasKuliahNavigation.value = true
//        }

    }

    fun addTugasKuliah(tugasKuliah: TugasKuliah)
    {
        viewModelScope.launch {
            database.insertTugas(tugasKuliah)
            //insert to do list and image in here too

            //to do list id masih 0 meskipun data ada 2, harusnya data pertama 0, data kedua 1
            for (i in toDoList.value!!)
            {
                database.insertToDoList(i)
            }

            for (i in imageList.value!!)
            {
                database.insertImages(i)
            }
            //how to insert multiple data to database in one time?

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

    fun convertSubjectIdToSubjectName(id: Int){
        viewModelScope.launch {
            _string.value = database.loadSubjectName(id)
        }
        //ke load tapi telat
    }

    fun onSubjectNameChanged()
    {
        _string.value = null
    }

    fun onToDoListClicked(toDoListId: Int) {
        _toDoListId.value = toDoListId
    }

    fun onGambarClicked(imageId: Int){
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

}