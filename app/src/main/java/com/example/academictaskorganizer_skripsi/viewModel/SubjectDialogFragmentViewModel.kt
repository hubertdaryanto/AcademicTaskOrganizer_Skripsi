package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academictaskorganizer_skripsi.database.subjectDao
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubjectDialogFragmentViewModel(application: Application, dataSource: tugasDatabaseDao): ViewModel() {
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
            database.deleteSubjectById(id)
        }
    }
}