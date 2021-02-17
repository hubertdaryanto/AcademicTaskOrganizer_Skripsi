package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditTugasKuliahFragmentViewModel(application: Application, tugasKuliahDataSource: tugasKuliahDao, tugasKuliahToDoListDataSource: tugasKuliahToDoListDao, tugasKuliahImageDataSource: tugasKuliahImageDao, subjectTugasKuliahDataSource: subjectTugasKuliahDao, tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao): ViewModel()  {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val tugasKuliahToDoListDatabase = tugasKuliahToDoListDataSource
    val tugasKuliahImageDatabase = tugasKuliahImageDataSource
    val subjectTugasKuliahDatabase = subjectTugasKuliahDataSource
    val tugasKuliahCompletionHistoryDatabase = tugasKuliahCompletionHistoryDataSource

    var tugasKuliahToDoListIdToBeDeleted: ArrayList<Long> = arrayListOf()
    var tugasKuliahImageIdToBeDeleted: ArrayList<Long> = arrayListOf()

    var _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    var _tugasKuliahBefore = MutableLiveData<TugasKuliah>()
    val tugasKuliahBefore: LiveData<TugasKuliah>
        get() = _tugasKuliahBefore

    val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList

    private val _tugasKuliahImageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val tugasKuliahImageList: LiveData<MutableList<TugasKuliahImage>>
        get() = _tugasKuliahImageList

    val _tugasKuliahToDoListBefore = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoListBefore: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoListBefore

    private val _tugasKuliahImageListBefore = MutableLiveData<MutableList<TugasKuliahImage>>()
    val tugasKuliahImageListBefore: LiveData<MutableList<TugasKuliahImage>>
        get() = _tugasKuliahImageListBefore
    /** Coroutine variables */

    /**
     * viewModelJob allows us to caancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    private val _subjectTugasKuliahText = MutableLiveData<String>()
    val subjectTugasKuliahText: LiveData<String>
        get() = _subjectTugasKuliahText

    private val _subjectTugasKuliahTextBefore = MutableLiveData<String>()
    val subjectTugasKuliahTextBefore: LiveData<String>
        get() = _subjectTugasKuliahTextBefore

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
            _tugasKuliah.value = tugasKuliahDatabase.loadTugasKuliahById(id)
            _tugasKuliahBefore.value = tugasKuliahDatabase.loadTugasKuliahById(id)
            _tugasKuliahToDoList.value = tugasKuliahToDoListDatabase.loadToDoListsByTugasKuliahId(id)
            _tugasKuliahImageList.value = tugasKuliahImageDatabase.loadImagesByTugasKuliahId(id)
            _tugasKuliahToDoListBefore.value = tugasKuliahToDoListDatabase.loadToDoListsByTugasKuliahId(id)
            _tugasKuliahImageListBefore.value = tugasKuliahImageDatabase.loadImagesByTugasKuliahId(id)
            _subjectTugasKuliahTextBefore.value = tugasKuliah.value?.tugasKuliahSubjectId?.let { subjectTugasKuliahDatabase.loadSubjectTugasKuliahNameById(it) }
        }
    }

    fun onShowSubjectTugasKuliahDialogClicked()
    {
        _showSubjectTugasKuliahDialog.value = true
    }

    fun doneLoadSubjectTugasKuliahDialog()
    {
        _showSubjectTugasKuliahDialog.value = null
    }

    fun onAddTugasKuliahToDoListClicked()
    {
        _addTugasKuliahToDoList.value = true
    }

    fun afterAddTugasKuliahToDoListClicked()
    {
        _addTugasKuliahToDoList.value = null
    }

    fun onAddTugasKuliahImageClicked()
    {
        _addTugasKuliahImage.value = true
    }

    fun afterAddTugasKuliahImageClicked()
    {
        _addTugasKuliahImage.value = null
    }



    fun addTugasKuliahToDoListItem(tugasKuliahToDoList: TugasKuliahToDoList)
    {
        _tugasKuliahToDoList.addNewItem(tugasKuliahToDoList)
        _tugasKuliahToDoList.notifyObserver()
    }

    fun removeTugasKuliahToDoListItem(id: Long)
    {
        val realId: Long? = _tugasKuliahToDoList.value?.get(id.toInt())?.tugasKuliahToDoListId
        _tugasKuliahToDoList.removeItemAt(id.toInt())
        _tugasKuliahToDoList.notifyObserver()
        if (realId != null) {
            tugasKuliahToDoListIdToBeDeleted.add(realId)
        }
//        uiScope.launch {
//            if (realId != null) {
//                database.deleteToDoList(realId.toLong())
//            }
//        }
    }

    fun removeTugasKuliahImageItem(id: Long)
    {
        val realId = _tugasKuliahImageList.value?.get(id.toInt())?.tugasKuliahImageId
        _tugasKuliahImageList.removeItemAt(id.toInt())
        _tugasKuliahImageList.notifyObserver()
        if (realId != null) {
            tugasKuliahImageIdToBeDeleted.add(realId)
        }
//        uiScope.launch {
//            if (realId != null) {
//                database.deleteImage(realId.toLong())
//            }
//        }
    }

    fun addTugasKuliahImageItem(imageKuliahImage: TugasKuliahImage){
        _tugasKuliahImageList.addNewItem(imageKuliahImage)
        _tugasKuliahImageList.notifyObserver()
    }

    fun onTimePickerClicked() {
        _showTimePicker.value = true
    }

    fun onDatePickerClicked() {
        _showDatePicker.value = true
    }

    fun onTimePicker2Clicked() {
        _showTimePicker2.value = true
    }

    fun onDatePicker2Clicked() {
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


    fun onSaveTugasKuliahClicked()
    {
        _editTugasKuliahNavigation.value = true
    }

    fun updateTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        uiScope.launch {
            var mTaskCompletionHistory: TugasKuliahCompletionHistory? = tugasKuliahCompletionHistoryDatabase.getTugasKuliahCompletionHistoryByTugasKuliahId(tugasKuliah.tugasKuliahId)
            if (mTaskCompletionHistory == null) {
                mTaskCompletionHistory =
                    TugasKuliahCompletionHistory(
                        bindToTugasKuliahId = tugasKuliah.tugasKuliahId,
                        activityType = "Tugas Kuliah Selesai"
                    )
            }
            else
            {
                tugasKuliahCompletionHistoryDatabase.deleteTugasKuliahCompletionHistory(mTaskCompletionHistory)
            }
            if (!tugasKuliah.isFinished) {
                AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah)
                tugasKuliah.updatedAt = System.currentTimeMillis()
                AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(context, tugasKuliah)
                //is different because the id is different, maybe if it loaded first, it will be deleted
            } else {
                AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah)
                mTaskCompletionHistory.activityType = "Tugas Kuliah Selesai"
                mTaskCompletionHistory.tugasKuliahCompletionHistoryId = System.currentTimeMillis()
                tugasKuliahCompletionHistoryDatabase.insertTugasKuliahCompletionHistory(mTaskCompletionHistory)
            }
           tugasKuliahDatabase.updateTugasKuliah(tugasKuliah)
            if (tugasKuliahToDoListIdToBeDeleted.count() != 0)
            {
                for (i in tugasKuliahToDoListIdToBeDeleted)
                {
                    tugasKuliahToDoListDatabase.deleteTugasKuliahToDoListById(i)
                }
            }

            if (tugasKuliahImageIdToBeDeleted.count() != 0)
            {
                for (i in tugasKuliahImageIdToBeDeleted)
                {
                    tugasKuliahImageDatabase.deleteTugasKuliahImageById(i)
                }
            }

            if (tugasKuliahToDoList.value != null)
            {
                tugasKuliahToDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    tugasKuliahToDoListDatabase.insertTugasKuliahToDoList(it)
                }
            }

            if (tugasKuliahImageList.value != null)
            {
                tugasKuliahImageList.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliah.tugasKuliahId
                    tugasKuliahImageDatabase.insertTugasKuliahImage(it)
                }
            }

        }
    }

    fun deleteTugasKuliah(context: Context)
    {

        uiScope.launch {
            AlarmScheduler.removeAlarmsForTugasKuliahReminder(context, tugasKuliah.value!!)
            tugasKuliahDatabase.deleteTugas(tugasKuliah.value!!)
            //to do list sama image yang terkait harus di remove juga
        }

    }

    fun doneNavigating() {
        _editTugasKuliahNavigation.value = null
    }

    fun convertSubjectIdToSubjectName(id: Long)
    {
        uiScope.launch {
            _subjectTugasKuliahText.value = subjectTugasKuliahDatabase.loadSubjectTugasKuliahNameById(id)
        }
    }

    fun onSubjectTugasKuliahNameChanged()
    {
        _subjectTugasKuliahText.value = null
    }

    fun onTugasKuliahToDoListClicked(toDoListId: Long)
    {
        _tugasKuliahToDoListId.value = toDoListId
    }

    fun onTugasKuliahImageClicked(imageId: Long)
    {
        _tugasKuliahImageId.value = imageId
    }

    fun afterClickTugasKuliahToDoList()
    {
        _tugasKuliahToDoListId.value = null
    }

    fun afterClickTugasKuliahImage()
    {
        _tugasKuliahImageId.value = null
    }

//    fun updateToDoList(id: Long, data: String, isFinished: Boolean)
//    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
//    }

    fun updateTugasKuliahToDoListName(id: Long, data: String)
    {
        tugasKuliahToDoList.value?.get(id.toInt())?.tugasKuliahToDoListName = data
//        toDoList.value?.get(id.toInt())?.isFinished = isFinished
    }

    fun updateTugasKuliahToDoListIsFinished(id: Long, isFinished: Boolean)
    {
//        toDoList.value?.get(id.toInt())?.toDoListName = data
        _tugasKuliahToDoList.value?.get(id.toInt())?.isFinished = isFinished
        _tugasKuliahToDoList.notifyObserver()
    }
}