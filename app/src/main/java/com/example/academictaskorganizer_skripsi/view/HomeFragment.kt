package com.example.academictaskorganizer_skripsi.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import org.stephenbrewer.arch.recyclerview.GridLayoutManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.GridLayoutManager
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
        setHasOptionsMenu(true)
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).getAllQueryListDao
        val viewModelFactory = HomeFragmentViewModelFactory(dataSource, application)

        val homeFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)


        val adapter = TugasAdapter(TugasKuliahListener { tugasKuliahId ->
            homeFragmentViewModel.onTugasKuliahClicked(tugasKuliahId)
        })
        binding.tugasList.adapter = adapter


        homeFragmentViewModel.tugas.observe(viewLifecycleOwner, Observer{
            it?.let {
                val date = homeFragmentViewModel.getTugasKuliahDate()
                adapter.addHeaderAndSubmitList(date)
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
                    this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditTugasFragment(it))
                    homeFragmentViewModel.onEditTugasKuliahNavigated()
                }
            })

        binding.homeFragmentViewModel = homeFragmentViewModel
        binding.setLifecycleOwner(this)

        val manager = GridLayoutManager(activity, 1)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0 -> return 1
                    else -> return 1
                }
            }
        }
        binding.tugasList.layoutManager = manager

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tugaskuliahsaya_menu, menu)
        var actionViewTaskCompletionHistory = menu.findItem(R.id.actionTaskCompletionHistory)
//        var action_delete = menu.findItem(R.id.actionViewTaskCompletionHistory)
//        action_save.setIcon(R.drawable.ic_baseline_save_24)
//        action_delete.setIcon(R.drawable.ic_baseline_delete_forever_24)
        view_utilities.menuIconColor(actionViewTaskCompletionHistory, Color.BLACK)
//        menuIconColor(action_delete, Color.BLACK)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionTaskCompletionHistory -> {
                goToTaskCompletionHistory()
                //insert the action here. Hint: Buat ke tampilan task completion history.
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun goToTaskCompletionHistory() {
        this.findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToTaskCompletionHistoryFragment())
//        homeFragmentViewModel.onTaskCompletionHistoryNavigated()
    }
}