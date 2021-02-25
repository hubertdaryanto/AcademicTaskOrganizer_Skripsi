package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.EditTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.ViewTugasKuliahCompletionHistoryActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.TugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListFinishedInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertLongToDateTimeFormatted
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.HomeFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.HomeFragmentViewModelFactory
import org.stephenbrewer.arch.recyclerview.GridLayoutManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var mHandlerForUpdateCurrentTime: Handler
    private lateinit var mRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.cardView.setOnClickListener {
            goToTugasKuliahCompletionHistory()
        }

        val application = requireNotNull(this.activity).application

        val tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        val tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        val tugasKuliahCompletionHistoryDataSource = AppDatabase.getInstance(application).getTugasKuliahCompletionHistoryDao
        val viewModelFactory = HomeFragmentViewModelFactory(tugasKuliahDataSource, tugasKuliahToDoListDataSource, tugasKuliahCompletionHistoryDataSource, application)

        homeFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)


        homeFragmentViewModel.loadTugasKuliah()
        homeFragmentViewModel.countTugasKuliahCompletedHistoryData()
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
            if (it != null)
            {
                binding.tugasKuliahNearDeadlineEmptyText.visibility = View.GONE
                val date = homeFragmentViewModel.getTugasKuliahAndDate()
                adapter.addHeaderAndSubmitList(date)
            }
            else
            {
                binding.tugasKuliahNearDeadlineEmptyText.visibility = View.VISIBLE
            }
        })

        homeFragmentViewModel.navigateToEditTugasKuliah.observe(
            viewLifecycleOwner,
            Observer { tugas ->
                tugas?.let {
                    val intent = Intent(requireActivity(), EditTugasKuliahActivity::class.java)
                    intent.putExtra("tugasKuliahId", it)
                    this.startActivity(intent)

                    homeFragmentViewModel.onEditTugasKuliahNavigated()
                }
            })


        homeFragmentViewModel.tugasKuliahCompletionHistory_total.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                binding.textView14.setText(it.toString())
            }
        })

        homeFragmentViewModel.tugasKuliahCompletionHistory_beforeTarget.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                binding.textView15.setText(it.toString())
            }
        })

        homeFragmentViewModel.tugasKuliahCompletionHistory_afterTarget.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                binding.textView16.setText(it.toString())
            }
        })

        homeFragmentViewModel.tugasKuliahCompletionHistory_afterDeadline.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                binding.textView17.setText(it.toString())
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

        mHandlerForUpdateCurrentTime = Handler()
        mRunnable = Runnable {
            mHandlerForUpdateCurrentTime.postDelayed(mRunnable, 1000)
            binding.realTimeClockinHome.text = "Waktu saat ini: " + convertLongToDateTimeFormatted(System.currentTimeMillis())
        }
        mHandlerForUpdateCurrentTime.post(mRunnable)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        homeFragmentViewModel.loadTugasKuliah()
        homeFragmentViewModel.countTugasKuliahCompletedHistoryData()
        mHandlerForUpdateCurrentTime.post(mRunnable)
    }

    override fun onStop() {
        super.onStop()
        mHandlerForUpdateCurrentTime.removeCallbacks(mRunnable)
    }

    private fun goToTugasKuliahCompletionHistory() {
        val intent = Intent(requireActivity(), ViewTugasKuliahCompletionHistoryActivity::class.java)
        this.startActivity(intent)
//        homeFragmentViewModel.onTaskCompletionHistoryNavigated()
    }
}