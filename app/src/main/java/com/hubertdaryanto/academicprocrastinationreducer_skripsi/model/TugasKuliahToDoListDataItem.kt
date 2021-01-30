package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

sealed class TugasKuliahToDoListDataItem {
    abstract val id: Long
    abstract val name: String
    abstract val isFinished: Boolean
    data class TugasKuliahToDoListItem(val tugasKuliahToDoList: TugasKuliahToDoList): TugasKuliahToDoListDataItem(){
        override val id = tugasKuliahToDoList.tugasKuliahToDoListId
        override val name = tugasKuliahToDoList.tugasKuliahToDoListName
        override val isFinished = tugasKuliahToDoList.isFinished
    }
    //gak ada bedanya kalau name sama isfinished di comment atau gak

}