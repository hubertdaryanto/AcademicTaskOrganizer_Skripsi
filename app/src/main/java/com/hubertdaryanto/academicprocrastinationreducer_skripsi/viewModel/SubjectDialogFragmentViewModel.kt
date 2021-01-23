package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.allQueryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.components.shared_data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubjectDialogFragmentViewModel(application: Application, dataSource: allQueryDao): ViewModel() {
    val database = dataSource
    private var viewModelJob = Job()
    private val uiScpoe = CoroutineScope(Dispatchers.Main + viewModelJob)

    var subject = database.getSubjectByNameForeground()

    private val _showAddSubjectDialog = MutableLiveData<Boolean?>()
    val showAddSubjectDialog: LiveData<Boolean?>
        get() = _showAddSubjectDialog

    private val _dismiss = MutableLiveData<Boolean?>()
    val dismiss: LiveData<Boolean?>
        get() = _dismiss

    private val _selectSubject = MutableLiveData<Long?>()
    val selectSubject: LiveData<Long?>
        get() = _selectSubject

    fun onShowAddSubjectDialogClicked() {
        _showAddSubjectDialog.value = true
    }

    fun doneLoadAddSubjectDialog()
    {
        _showAddSubjectDialog.value = null
    }

    fun onSubjectClicked(id: Long) {
        _selectSubject.value = id
    }

    fun afterSubjectClicked()
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

    fun removeSubject(id: Long)
    {
        uiScpoe.launch {
            if (shared_data.mSubjectId == id)
            {
                shared_data.mSubjectAtAddTugasFragment = null
            }
            database.deleteSubjectById(id)
        }
    }
}