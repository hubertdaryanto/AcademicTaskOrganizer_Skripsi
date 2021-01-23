package com.hubertdaryanto.academicprocrastinationreducer_skripsi.database


//interface TugasKuliahDataStore{
//    suspend fun getSets(): MutableList<TugasKuliah>?
//    suspend fun addAll(sets: MutableList<TugasKuliah>?)
//
//}
//
//class TugasKuliahRepository(
//    private val setLocalDataStore: TugasKuliahDataStore
//) {
//    suspend fun getSets(): MutableList<TugasKuliah>?
//    {
//        val cache = setLocalDataStore?.getSets()
//        if (cache != null) return cache
//        //remote tidak ada
//    }
//}
//
//class TugasKuliahLocalDataStore: TugasKuliahDataStore{
//    private var caches = mutableListOf<TugasKuliah>()
//
//    override suspend fun getSets(): MutableList<TugasKuliah>? =
//        if (caches.isNotEmpty()) caches else null
//
//    override suspend fun addAll(sets: MutableList<TugasKuliah>?) {
//        sets?.let { caches = it }
//    }
//
//}