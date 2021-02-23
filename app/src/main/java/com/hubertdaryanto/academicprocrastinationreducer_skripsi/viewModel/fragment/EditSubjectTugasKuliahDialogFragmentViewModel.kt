package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditSubjectTugasKuliahDialogFragmentViewModel(application: Application, dataSource: subjectTugasKuliahDao): ViewModel() {
    val database = dataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _subjectTugasKuliah = MutableLiveData<SubjectTugasKuliah>()
    val subjectTugasKuliah: LiveData<SubjectTugasKuliah>
        get() = _subjectTugasKuliah

    private val _addSubjectAndDismiss = MutableLiveData<Boolean?>()
    val addSubjectAndDismiss: LiveData<Boolean?>
        get() = _addSubjectAndDismiss

    private val _dismiss = MutableLiveData<Boolean?>()
    val dismiss: LiveData<Boolean?>
        get() = _dismiss

    fun loadSubject(id: Long)
    {
        uiScope.launch {
            _subjectTugasKuliah.value = database.getSubjectTugasKuliahById(id)
        }

    }

    fun dismiss()
    {
        _dismiss.value = true
    }

    fun afterdismiss()
    {
        _dismiss.value = null
    }

    fun onAddSubjectClicked()
    {
        _addSubjectAndDismiss.value = true
    }

    fun afterAddSubjectClicked()
    {
        _addSubjectAndDismiss.value = null
    }

    fun updateSubject(subjectTugasKuliah: SubjectTugasKuliah)
    {
        viewModelScope.launch {
            database.updateSubjectTugasKuliah(subjectTugasKuliah)
        }
    }
}
