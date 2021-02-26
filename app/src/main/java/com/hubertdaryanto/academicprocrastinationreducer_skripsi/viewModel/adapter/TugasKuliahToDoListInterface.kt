package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter

interface TugasKuliahToDoListInterface{
    fun onUpdateText(id: Long, name: String)
    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
    fun onRemoveItem(id: Long)
    fun onRemoveEmptyItem(id: Long)
    fun onEnterPressed(id: Long)
}