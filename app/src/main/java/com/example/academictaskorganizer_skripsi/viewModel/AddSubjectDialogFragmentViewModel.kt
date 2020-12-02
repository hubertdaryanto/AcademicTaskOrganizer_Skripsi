package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academictaskorganizer_skripsi.database.Subject
import com.example.academictaskorganizer_skripsi.database.subjectDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddSubjectDialogFragmentViewModel(application: Application, dataSource: subjectDao): ViewModel() {
    val database = dataSource
    private var viewModelJob = Job()
    private val uiScpoe = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _addSubjectAndDismiss = MutableLiveData<Boolean?>()
    val addSubjectAndDismiss: LiveData<Boolean?>
        get() = _addSubjectAndDismiss

    private val _dismiss = MutableLiveData<Boolean?>()
    val dismiss: LiveData<Boolean?>
        get() = _dismiss

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

    fun addSubject(subject: Subject)
    {
        viewModelScope.launch {
            database.insertSubject(subject)
        }
    }
}
