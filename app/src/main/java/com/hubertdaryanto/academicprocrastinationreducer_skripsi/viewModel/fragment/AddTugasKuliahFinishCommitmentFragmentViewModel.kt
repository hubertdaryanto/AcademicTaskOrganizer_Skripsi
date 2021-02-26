package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

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

class AddTugasKuliahFinishCommitmentFragmentViewModel(application: Application, tugasKuliahDataSource: tugasKuliahDao, tugasKuliahImageDataSource: tugasKuliahImageDao, tugasKuliahToDoListDataSource: tugasKuliahToDoListDao): ViewModel() {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val tugasKuliahImageDatabase = tugasKuliahImageDataSource
    val tugasKuliahToDoListDatabase = tugasKuliahToDoListDataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList

    private val _tugasKuliahImageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val tugasKuliahImageList: LiveData<MutableList<TugasKuliahImage>>
        get() = _tugasKuliahImageList

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
            _tugasKuliahToDoList.value = shared_data.mTugasKuliahToDoList
        }
        if (shared_data.mTugasKuliahImages != null)
        {
            _tugasKuliahImageList.value = shared_data.mTugasKuliahImages
        }
        uiScope.launch {
            tugasKuliah.updatedAt = System.currentTimeMillis()
            var tugasKuliaId = tugasKuliahDatabase.insertTugasKuliah(tugasKuliah)
            tugasKuliah.tugasKuliahId = tugasKuliaId
            AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(context, tugasKuliah)
            if (tugasKuliahToDoList.value != null)
            {
                tugasKuliahToDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliaId
                    tugasKuliahToDoListDatabase.insertTugasKuliahToDoList(it)
                }
            }

            if (tugasKuliahImageList.value != null)
            {
                tugasKuliahImageList.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliaId
                    tugasKuliahImageDatabase.insertTugasKuliahImage(it)
                }
            }
        }
    }

    fun addSubjectAndTugasKuliah(context: Context, tugasKuliah: TugasKuliah, subjectName: String)
    {
        val subjectTugasKuliahDatabase = AppDatabase.getInstance(context).getSubjectTugasKuliahDao

        val mSubject = SubjectTugasKuliah(subjectName)

        if (onboarding_data.mTugasKuliahToDoList != null)
        {
            _tugasKuliahToDoList.value = onboarding_data.mTugasKuliahToDoList
        }
        if (onboarding_data.mTugasKuliahImages != null)
        {
            _tugasKuliahImageList.value = onboarding_data.mTugasKuliahImages
        }
        uiScope.launch {
            subjectTugasKuliahDatabase.insertSubject(mSubject)
            tugasKuliah.updatedAt = System.currentTimeMillis()
            var tugasKuliaId = tugasKuliahDatabase.insertTugasKuliah(tugasKuliah)
            tugasKuliah.tugasKuliahId = tugasKuliaId
            AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(context, tugasKuliah)
            if (tugasKuliahToDoList.value != null)
            {
                tugasKuliahToDoList.value!!.toList().forEach {

                    it.bindToTugasKuliahId = tugasKuliaId
                    tugasKuliahToDoListDatabase.insertTugasKuliahToDoList(it)
                }
            }

            if (tugasKuliahImageList.value != null)
            {
                tugasKuliahImageList.value!!.toList().forEach{
                    it.bindToTugasKuliahId = tugasKuliaId
                    tugasKuliahImageDatabase.insertTugasKuliahImage(it)
                }
            }
        }
    }
}