package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

sealed class TugasKuliahImageDataItem {
    abstract val id: Long
    abstract val name: String
    data class TugasKuliahImageItem(val TugasKuliahImage: TugasKuliahImage): TugasKuliahImageDataItem(){
        override val id = TugasKuliahImage.tugasKuliahImageId
        override val name = TugasKuliahImage.tugasKuliahImageName
    }

}