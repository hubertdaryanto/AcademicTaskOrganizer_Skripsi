package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academictaskorganizer_skripsi.database.TugasKuliah

class AddTugasFragmentViewModel(application: Application): ViewModel() {
    private val _addTugasKuliah = MutableLiveData<TugasKuliah>()
    val addTugasKuliah: LiveData<TugasKuliah>
        get() = _addTugasKuliah


    fun addTugasKuliah(tugasKuliah: TugasKuliah)
    {
        _addTugasKuliah.value = tugasKuliah
    }
}