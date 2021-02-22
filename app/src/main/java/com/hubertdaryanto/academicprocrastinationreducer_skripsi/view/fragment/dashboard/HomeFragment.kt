package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentHomeBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.TugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListFinishedInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.HomeFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.HomeFragmentViewModelFactory
import org.stephenbrewer.arch.recyclerview.GridLayoutManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        val tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        val viewModelFactory = HomeFragmentViewModelFactory(tugasKuliahDataSource, tugasKuliahToDoListDataSource, application)

        homeFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)


        homeFragmentViewModel.loadTugasKuliah()
        val adapter = TugasKuliahAdapter(TugasKuliahListener { tugasKuliahId ->
            homeFragmentViewModel.onTugasKuliahClicked(tugasKuliahId)
        }, object : TugasKuliahToDoListFinishedInterface
        {

            override fun onFinished() {
                if (shared_data.toDoListFinished == true)
                {
                    homeFragmentViewModel.loadTugasKuliah()
                }
            }
        })
        binding.tugasKuliahNearDeadlineRecyclerView.adapter = adapter

        homeFragmentViewModel.tugasKuliah.observe(viewLifecycleOwner, Observer{
//            if (it.count() == 0)
//            {
//                binding.textView3.visibility = View.VISIBLE
//            }
//            else
//            {
//                binding.textView3.visibility = View.GONE
//            }
            val date = homeFragmentViewModel.getTugasKuliahAndDate()
            adapter.addHeaderAndSubmitList(date)
        })

        homeFragmentViewModel.navigateToEditTugasKuliah.observe(
            viewLifecycleOwner,
            Observer { tugas ->
                tugas?.let {
//                    this.findNavController().navigate(
//                        TugasMataKuliahListFragmentDirections.actionHomeFragmentToEditTugasFragment(
//                            it
//                        )
//                    )
                    homeFragmentViewModel.onEditTugasKuliahNavigated()
                }
            })

        binding.homeFragmentViewModel = homeFragmentViewModel
        // Inflate the layout for this fragment

        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 1)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0 -> return 1
                    else -> return 1
                }
            }
        }
        binding.tugasKuliahNearDeadlineRecyclerView.layoutManager = manager

        return binding.root
    }
}