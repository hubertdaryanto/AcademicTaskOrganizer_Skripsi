package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubjectTugasKuliahDialogFragmentViewModel(application: Application, subjectTugasKuliahDataSource: subjectTugasKuliahDao): ViewModel() {
    val subjectTugasKuliahDatabase = subjectTugasKuliahDataSource
    private var viewModelJob = Job()
    private val uiScpoe = CoroutineScope(Dispatchers.Main + viewModelJob)

    var subject = subjectTugasKuliahDatabase.getSubjectTugasKuliahByNameForeground()

    private val _showAddSubjectDialog = MutableLiveData<Boolean?>()
    val showAddSubjectDialog: LiveData<Boolean?>
        get() = _showAddSubjectDialog

    private val _dismiss = MutableLiveData<Boolean?>()
    val dismiss: LiveData<Boolean?>
        get() = _dismiss

    private val _selectSubject = MutableLiveData<Long?>()
    val selectSubject: LiveData<Long?>
        get() = _selectSubject

    fun onShowAddSubjectTugasKuliahDialogClicked()
    {
        _showAddSubjectDialog.value = true
    }

    fun doneLoadAddSubjectTugasKuliahDialog()
    {
        _showAddSubjectDialog.value = null
    }

    fun onSubjectTugasKuliahClicked(id: Long)
    {
        _selectSubject.value = id
    }

    fun afterSubjectTugasKuliahClicked()
    {
        _selectSubject.value = null
    }

    fun dismiss()
    {
        _dismiss.value = true
    }

    fun afterdismiss()
    {
        _dismiss.value = null
    }

    fun removeSubjectTugasKuliah(id: Long)
    {
        uiScpoe.launch {
            if (shared_data.mSubjectId == id)
            {
                shared_data.mSubjectAtAddTugasFragment = null
            }
            subjectTugasKuliahDatabase.deleteSubjectTugasKuliahById(id)
        }
    }
}