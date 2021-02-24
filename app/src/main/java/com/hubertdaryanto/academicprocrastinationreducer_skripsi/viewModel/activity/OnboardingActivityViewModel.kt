package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class OnboardingActivityViewModel(tugasKuliahDataSource: tugasKuliahDao, subjectTugasKuliahDataSource: subjectTugasKuliahDao, application: Application): ViewModel() {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val subjectTugasKuliahDatabase = subjectTugasKuliahDataSource

    private val _tugasKuliah = MutableLiveData<TugasKuliah>()
    val tugasKuliah: LiveData<TugasKuliah>
        get() = _tugasKuliah

    private val _subjectTugasKuliah = MutableLiveData<SubjectTugasKuliah>()
    val subjectTugasKuliah: LiveData<SubjectTugasKuliah>
        get() = _subjectTugasKuliah

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList


    private val _imageList = MutableLiveData<MutableList<TugasKuliahImage>>()
    val imageListKuliahImage: LiveData<MutableList<TugasKuliahImage>>
        get() = _imageList

    private val _subjectText = MutableLiveData<String>()
    val subjectText: LiveData<String>
        get() = _subjectText

    fun addTugasKuliahandMataKuliah()
    {
        uiScope.launch {
            subjectTugasKuliahDatabase.insertSubject(subjectTugasKuliah.value!!)
            tugasKuliahDatabase.insertTugasKuliah(tugasKuliah.value!!)
        }
    }

    fun setSubjectText(s: String)
    {
        _subjectText.value = s
    }
}
