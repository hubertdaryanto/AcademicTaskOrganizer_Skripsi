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

    object AddToDoList: TugasKuliahToDoListDataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
        override val name: String
            get() = ""
        override val isFinished: Boolean
            get() = false
    }
    //gak ada bedanya kalau name sama isfinished di comment atau gak

}