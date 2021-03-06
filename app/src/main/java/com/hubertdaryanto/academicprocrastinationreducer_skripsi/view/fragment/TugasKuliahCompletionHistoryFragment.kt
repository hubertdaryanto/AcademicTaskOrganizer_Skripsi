package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentTugasKuliahCompletionHistoryBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahCompletionHistoryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.TugasKuliahCompletionHistoryAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.View_utilities
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahCompletionHistoryListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.TugasKuliahCompletionHistoryFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.TugasKuliahCompletionHistoryFragmentViewModelFactory

class TugasKuliahCompletionHistoryFragment: Fragment() {
    private lateinit var binding: FragmentTugasKuliahCompletionHistoryBinding
    private lateinit var tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao
    private lateinit var tugasKuliahCompletionHistoryFragmentViewModel: TugasKuliahCompletionHistoryFragmentViewModel
    val filterList = arrayOf("7 Hari", "30 Hari", "Kapan saja")
    var filterSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tugas_kuliah_completion_history, container, false)

        val application = requireNotNull(this.activity).application

        tugasKuliahCompletionHistoryDataSource = AppDatabase.getInstance(application).getTugasKuliahCompletionHistoryDao
        val viewModelFactory = TugasKuliahCompletionHistoryFragmentViewModelFactory(tugasKuliahCompletionHistoryDataSource, application)

        tugasKuliahCompletionHistoryFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            TugasKuliahCompletionHistoryFragmentViewModel::class.java)

        tugasKuliahCompletionHistoryFragmentViewModel.loadTugasKuliahCompletionHistory(filterSelected)
        val adapter = TugasKuliahCompletionHistoryAdapter(TugasKuliahCompletionHistoryListener { tugasKuliahId ->
            tugasKuliahCompletionHistoryFragmentViewModel.onTugasKuliahCompletionHistoryClicked(
                tugasKuliahId
            )
        })


        binding.taskCompletionHistoryList.adapter = adapter

        tugasKuliahCompletionHistoryFragmentViewModel.tugasKuliahCompletionHistories.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.count() == 0)
                {
                    binding.textView3.visibility = View.VISIBLE
                }
                else
                {
                    binding.textView3.visibility = View.GONE
                    val date = tugasKuliahCompletionHistoryFragmentViewModel.getTugasKuliahCompletionHistoryDate()
                    adapter.addHeaderAndSubmitList(date)
                }
            }
        })

        tugasKuliahCompletionHistoryFragmentViewModel.navigateToViewTugasKuliahCompletionHistoryDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
//                this.findNavController().navigate(
//                    TugasKuliahCompletionHistoryFragmentDirections.actionTaskCompletionHistoryFragmentToEditTugasFragment(
//                        it
//                    )
//                )
//                this.findNavController().navigate(TaskCompletionHistoryDirections.actionTaskCompletionHistoryFragmentToTaskCompletionHistoryDetailFragment(it))
                tugasKuliahCompletionHistoryFragmentViewModel.onTugasKuliahCompletionHistoryNavigated()
            }
        })

        binding.tugasKuliahCompletionHistoryFragmentViewModel = tugasKuliahCompletionHistoryFragmentViewModel
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
        binding.taskCompletionHistoryList.layoutManager = manager

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tugas_kuliah_completion_history_menu, menu)
        var action_filter = menu.findItem(R.id.action_filter_tugas_kuliah_completion_history)
        View_utilities.menuIconColor(action_filter, Color.BLACK)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.action_filter_tugas_kuliah_completion_history -> {
                //todo: filter 7 / 30 / kapan saja
                showFilterOptionsDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun showFilterOptionsDialog()
    {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Filter")

        builder.setSingleChoiceItems(filterList, filterSelected)
        {
            dialog, which ->
            filterSelected = which
            tugasKuliahCompletionHistoryFragmentViewModel.loadTugasKuliahCompletionHistory(which)
            dialog.dismiss()
        }

        builder.setPositiveButton("Batal", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog = builder.create()
        alertDialog.show()
    }

}