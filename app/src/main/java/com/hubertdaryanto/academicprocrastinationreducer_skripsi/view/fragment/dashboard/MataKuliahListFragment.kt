package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.dashboard

import android.app.AlertDialog
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentMataKuliahListBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.ViewTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.SubjectTugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment.EditSubjectTugasKuliahDialogFragment
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.SubjectTugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertLongToDateTimeFormatted
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.MataKuliahListFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard.MataKuliahListFragmentViewModelFactory

class MataKuliahListFragment : Fragment() {
    private lateinit var binding: FragmentMataKuliahListBinding
    private lateinit var mataKuliahListFragmentViewModel: MataKuliahListFragmentViewModel
    private lateinit var mHandlerForUpdateCurrentTime: Handler
    private lateinit var mRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        setHasOptionsMenu(true)
        val application = requireNotNull(this.activity).application

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mata_kuliah_list, container,false)

        shared_data.fromFragment = this.toString()

        val subjectDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = MataKuliahListFragmentViewModelFactory(application, subjectDataSource)

        mataKuliahListFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            MataKuliahListFragmentViewModel::class.java)

        mataKuliahListFragmentViewModel.loadSubjectTugasKuliah()
        val adapter = SubjectTugasKuliahAdapter(SubjectTugasKuliahListener { subjectId ->
            mataKuliahListFragmentViewModel.onSubjectTugasKuliahClicked(subjectId)
        }, object :
            SubjectTugasKuliahInterface {

            override fun onRemoveItem(id: Long) {
                AlertDialog.Builder(context).apply {
                    setTitle(context.getString(R.string.delete_subject_title_confirmation))
                    setMessage(context.getString(R.string.delete_subject_subtitle_confirmation))
                    setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                        mataKuliahListFragmentViewModel.removeSubjectTugasKuliah(id)
                    }
                    setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

                    }
                }.create().show()
            }

            override fun onEditItem(id: Long) {
                val dialog = EditSubjectTugasKuliahDialogFragment(id)
                dialog.show(parentFragmentManager, "EditSubjectDialogFragment")
            }
        })
        binding.subjectListInDashboard.adapter = adapter

        mataKuliahListFragmentViewModel.subjectTugasKuliah.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        mataKuliahListFragmentViewModel.selectSubject.observe(viewLifecycleOwner, Observer {
            it?.let{
                val intent = Intent(this.activity, ViewTugasKuliahActivity::class.java)
                intent.putExtra("subjectId", it)
                this.startActivity(intent)
                mataKuliahListFragmentViewModel.afterSubjectTugasKuliahClicked()
            }
        })

//        mataKuliahListFragmentViewModel.editSubjectTugasKuliahMode.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                if (it)
//                {
//                    //show delete button, and when clicked it will show edit mata kuliah dialog
//                }
//                else
//                {
//                    //hide delete button, and when clicked it will go to activity as usual
//                }
//                requireActivity().invalidateOptionsMenu()
//        }
//        })

        binding.mataKuliahListFragmentViewModel = mataKuliahListFragmentViewModel
        binding.lifecycleOwner = this

        val manager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(binding.subjectListInDashboard.context,
            manager.orientation)
        binding.subjectListInDashboard.addItemDecoration(dividerItemDecoration)

        binding.subjectListInDashboard.layoutManager = manager
        // Inflate the layout for this fragment

        mHandlerForUpdateCurrentTime = Handler()
        mRunnable = Runnable {
            mHandlerForUpdateCurrentTime.postDelayed(mRunnable, 1000)
            binding.realTimeClockinMataKuliahList.text = "Waktu saat ini: " + convertLongToDateTimeFormatted(System.currentTimeMillis())
        }
        mHandlerForUpdateCurrentTime.post(mRunnable)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mHandlerForUpdateCurrentTime.post(mRunnable)
    }

    override fun onStop() {
        super.onStop()
        mHandlerForUpdateCurrentTime.removeCallbacks(mRunnable)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//
//        if (mataKuliahListFragmentViewModel.editSubjectTugasKuliahMode.value == true)
//        {
//            inflater.inflate(R.menu.mata_kuliah_list_menu_2, menu)
//            var edit = menu.findItem(R.id.action_finish_edit_mata_kuliah)
//            View_utilities.menuIconColor(edit, Color.BLACK)
//        }
//        else
//        {
//            inflater.inflate(R.menu.mata_kuliah_list_menu, menu)
//            var edit = menu.findItem(R.id.action_edit_mata_kuliah)
//            View_utilities.menuIconColor(edit, Color.BLACK)
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId)
//        {
//            R.id.action_edit_mata_kuliah -> {
//                mataKuliahListFragmentViewModel.onEditSubjectTugasKuliahClicked()
//                return true
//            }
//            R.id.action_finish_edit_mata_kuliah -> {
//                mataKuliahListFragmentViewModel.onFinishEditSubjectTugasKuliahClicked()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

}