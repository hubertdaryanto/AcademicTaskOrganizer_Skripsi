package com.example.academictaskorganizer_skripsi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.viewModel.HomeFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.HomeFragmentViewModelFactory
import com.example.academictaskorganizer_skripsi.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


class HomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

//    private lateinit var floatingActionButton: FloatingActionButton
//    private lateinit var recyclerViewTugas: RecyclerView
//    private lateinit var viewModel: TugasKuliahViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).getTugasDao
        val viewModelFactory = HomeFragmentViewModelFactory(dataSource, application)

        val homeFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)



        val adapter = TugasAdapter(TugasKuliahListener { tugasKuliahId ->
            homeFragmentViewModel.onTugasKuliahClicked(tugasKuliahId)
        })
        binding.tugasList.adapter = adapter


        homeFragmentViewModel.tugas.observe(viewLifecycleOwner, Observer{
            it?.let {
                adapter.submitList(it)
            }
        })




        homeFragmentViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Test",
                    Snackbar.LENGTH_SHORT
                ).show()
                homeFragmentViewModel.doneShowingSnackbar()
            }
        })

        homeFragmentViewModel.navigateToAddTugasKuliah.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTugasFragment())
                homeFragmentViewModel.onAddTugasKuliahNavigated()
            }
        })

        homeFragmentViewModel.navigateToEditTugasKuliah.observe(
            viewLifecycleOwner,
            Observer { tugas ->
                tugas?.let {
                    this.findNavController().navigate(HomeFragmentDirections.actionEditTugas(tugas))
                    homeFragmentViewModel.onEditTugasKuliahNavigated()
                }
            })

        binding.homeFragmentViewModel = homeFragmentViewModel
        binding.setLifecycleOwner(this)
//        var view = binding.root
//        floatingActionButton = binding.floatingActionButton
//        recyclerViewTugas = binding.recyclerViewTugas
//        viewModel = ViewModelProvider(this).get(TugasKuliahViewModel::class.java)
        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        recyclerViewTugas.setHasFixedSize(true)
//        recyclerViewTugas.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        launch {
//            context?.let {
//                val tugas = AppDatabase(it).getTugasDao().getAllSortedByDeadline()
//                recyclerViewTugas.adapter = TugasAdapter(tugas)
//            }
//
//        }
//        floatingActionButton.setOnClickListener {
//            val action = HomeFragmentDirections.actionEditTugas()
//            Navigation.findNavController(it).navigate(action)
//        }
//    }
}