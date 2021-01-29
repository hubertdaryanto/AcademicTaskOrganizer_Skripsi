package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImage
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddTugasKuliahFinishCommitmentFragmentViewModel(application: Application, dataSource: allQueryDao): ViewModel() {
    val database = dataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoList

    private val _imageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val imageListKuliahImage: LiveData<MutableList<TugasKuliahImage>>
        get() = _imageList

    private val _addTugasKuliahNavigation = MutableLiveData<Boolean?>()
    val addTugasKuliahNavigation: LiveData<Boolean?>
        get() = _addTugasKuliahNavigation

    private val _showTimePicker = MutableLiveData<Boolean?>()
    val showTimePicker: LiveData<Boolean?>
        get() = _showTimePicker

    private val _showDatePicker = MutableLiveData<Boolean?>()
    val showDatePicker: LiveData<Boolean?>
        get() = _showDatePicker

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

    fun doneNavigating() {
        _addTugasKuliahNavigation.value = null
    }

    fun addTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        if (shared_data.mTugasKuliahToDoList != null)
        {
            _toDoList.value = shared_data.mTugasKuliahToDoList
        }
        if (shared_data.mTugasKuliahImages != null)
        {
            _imageList.value = shared_data.mTugasKuliahImages
        }
        uiScope.launch {
            tugasKuliah.updatedAt = System.currentTimeMillis()
            var tugasKuliaId = database.insertTugas(tugasKuliah)
            //insert to do list and image in here too
            tugasKuliah.tugasKuliahId = tugasKuliaId
            AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(context, tugasKuliah)
            //to do list id masih 0 meskipun data ada 2, harusnya data pertama 0, data kedua 1
            if (tugasKuliahToDoList.value != null)
            {
                tugasKuliahToDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliaId
                    database.insertToDoList(it)
                }
            }

            if (imageListKuliahImage.value != null)
            {
                imageListKuliahImage.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliaId
                    database.insertImage(it)
                }
            }

            //how to insert multiple data to database in one time?
//            database.insertToDoLists(Collections.unmodifiableList(toDoList.value))
//            database.insertImages(Collections.unmodifiableList(imageList.value))

        }
    }
}