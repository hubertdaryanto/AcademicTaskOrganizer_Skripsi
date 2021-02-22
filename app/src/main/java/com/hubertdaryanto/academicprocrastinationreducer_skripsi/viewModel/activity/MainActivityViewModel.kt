package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListItemType
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertDeadlineToDateFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//high priority (saran dari dosen pembimbing)
//todo: (high) Add Onboarding feature (this will make a new view and classes and you must update it again in visio before thesis defense) -> For guide user to quick setup their list of mata kuliah, and first tugas kuliah
//todo: (high) make dashboard view (can replace "tugas kuliah saya" view as home page) (isi: jumlah tugas yang belum selesai, tugas kuliah yang selesai jauh2 hari sebelum deadline/terlambat/hampir terlambat, etc... silahkan rancang di visio) (inspirasi? Cek binusmaya)
//todo: (high) Make tugas kuliah colored when not passed finish commitment (can be no color or green), passed finish commitment (yellow) and passed the deadline (red or gray)
//todo: (high) Make tutorial in app (Inspiration? Look Aura App in iOS) -> For explain each component of the app (karena dospem gak langsung tau nama mata kuliah yang mana, deadline yang mana, nama tugas yang mana)
//todo: (high) Make splash screen view design meet the criteria of material design guidance for splash screen
//todo: (high) Redesign Home Page View

//medium priority
//todo: (medium) Fix press enter to add to do list method
//todo: (medium) Change app icon into proper topic instead of graduation gown
//todo: (medium) Move add to do list and image button to bottom of each adapter, so the flow of the app is top to bottom (consistent)
//todo: (medium) fix bug for add tugas kuliah commitment time (masa deadline tugas jam 15:00 max nya jam 14:59?)

//low priority
//todo: (low) Add built in image viewer
//todo: (low) Save added image into app storage, instead of using system device storage (only access it using string)

class MainActivityViewModel(tugasKuliahDataSource: tugasKuliahDao, tugasKuliahToDoListDataSource: tugasKuliahToDoListDao, application: Application): ViewModel() {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val tugasKuliahToDoListDatabase = tugasKuliahToDoListDataSource

    private val _tugasKuliah = MutableLiveData<MutableList<TugasKuliah>>()
    val tugasKuliah: LiveData<MutableList<TugasKuliah>>
        get() = _tugasKuliah
//    var tugas = database.getAllTugasKuliahSortedByDeadlineForeground()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList

    private val _navigateToEditTugasKuliah = MutableLiveData<Long>()
    val navigateToEditTugasKuliah: LiveData<Long>
        get() = _navigateToEditTugasKuliah

    private val _navigateToAddTugasKuliah = MutableLiveData<Boolean>()
    val navigateToAddTugasKuliah: LiveData<Boolean>
        get() = _navigateToAddTugasKuliah

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackbarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun loadTugasKuliah()
    {
        _tugasKuliah.value = arrayListOf()
        uiScope.launch {
            _tugasKuliah.value = tugasKuliahDatabase.getAllTugasKuliahUnfinishedSortedByDeadline()
        }
    }

//    fun clearTugasKuliah()
//    {
//
//    }

    fun loadToDoList(id: Long)
    {
        uiScope.launch {
            _tugasKuliahToDoList.value = tugasKuliahToDoListDatabase.loadToDoListsByTugasKuliahId(id)
        }
    }

    fun doneShowingSnackbar()
    {
        _showSnackbarEvent.value = null
    }

    fun getTugasKuliahAndDate(): List<TugasKuliahListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
        for (i in tugasKuliah.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.deadline)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
                count++
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        count.toString()
                    )
            }
            else
            {
                date = dateCursor
                tempdate = date
                var tugasKuliahDate =
                    TugasKuliahDate(
                        date,
                        "0"
                    )
                arrayList.add(tugasKuliahDate)
                temp = arrayList.count() - 1
                arrayList.add(i)
                count = 1
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        count.toString()
                    )
                //kalau misalkan i++ disini, maka jumlah nya belum ketahuan ada berapa pas munculin header nya
                //bisa sih pakai query load data from deadline xxxx ke xxxx pakai modulus per hari buat nentuin parameter query nya
            }
        }
        return arrayList
    }

    init {
        Log.i("AgendaActivityViewModel", "AgendaActivityViewModel created!")
    }

    fun onAddTugasKuliahClicked()
    {
        _navigateToAddTugasKuliah.value = true
    }

    fun onAddTugasKuliahNavigated()
    {
        _navigateToAddTugasKuliah.value = false
    }

    fun onTugasKuliahClicked(id: Long)
    {
        _navigateToEditTugasKuliah.value = id
    }

    fun onEditTugasKuliahNavigated()
    {
        _navigateToEditTugasKuliah.value = null
    }
}
