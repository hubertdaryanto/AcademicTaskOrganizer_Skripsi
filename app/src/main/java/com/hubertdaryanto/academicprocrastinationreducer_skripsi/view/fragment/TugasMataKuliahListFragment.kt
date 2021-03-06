package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

//import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentTugasMataKuliahListBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.AddTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.EditTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.ViewTugasKuliahActivity
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.TugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.View_utilities
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListFinishedInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertLongToDateTimeFormatted
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.TugasMataKuliahListFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.TugasMataKuliahListFragmentViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.stephenbrewer.arch.recyclerview.GridLayoutManager


class TugasMataKuliahListFragment : Fragment() {
    private lateinit var binding: FragmentTugasMataKuliahListBinding
    private lateinit var tugasMataKuliahListFragmentViewModel: TugasMataKuliahListFragmentViewModel
    private lateinit var mHandlerForUpdateCurrentTime: Handler
    private lateinit var mRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tugas_mata_kuliah_list, container, false)


        val subjectId: Long = arguments?.get("subjectId") as Long

//        binding.floatingActionButton.setOnClickListener {
//            val intent = Intent(requireActivity(), AddTugasKuliahActivity::class.java)
//            intent.putExtra("subjectTugasKuliahId", subjectId)
//            this.startActivity(intent)
//        }
        val parentActivity = activity as ViewTugasKuliahActivity

        val application = requireNotNull(this.activity).application

        val tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        val tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        val subjectTugasKuliahDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = TugasMataKuliahListFragmentViewModelFactory(tugasKuliahDataSource, tugasKuliahToDoListDataSource, application)

        var job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        tugasMataKuliahListFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(TugasMataKuliahListFragmentViewModel::class.java)

//        homeFragmentViewModel.clearTugasKuliah()
        tugasMataKuliahListFragmentViewModel.loadTugasKuliah(subjectId)
        val adapter = TugasKuliahAdapter(TugasKuliahListener { tugasKuliahId ->
            tugasMataKuliahListFragmentViewModel.onTugasKuliahClicked(tugasKuliahId)
        }, object : TugasKuliahToDoListFinishedInterface
        {

            override fun onFinished() {
                if (shared_data.toDoListFinished == true)
                {
                    tugasMataKuliahListFragmentViewModel.loadTugasKuliah(subjectId)
                }
            }
        })
        binding.tugasList.adapter = adapter

//        if (homeFragmentViewModel.tugas.value == null)
//        {
//            binding.textView3.visibility = View.VISIBLE
//        }

        tugasMataKuliahListFragmentViewModel.tugasKuliah.observe(viewLifecycleOwner, Observer{
            if (it.count() == 0)
            {
                binding.textView3.visibility = View.VISIBLE
            }
            else
            {
                binding.textView3.visibility = View.GONE
            }
            val date = tugasMataKuliahListFragmentViewModel.getTugasKuliahAndDate()
            adapter.addHeaderAndSubmitList(date)
        })




        tugasMataKuliahListFragmentViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Test",
                    Snackbar.LENGTH_SHORT
                ).show()
                tugasMataKuliahListFragmentViewModel.doneShowingSnackbar()
            }
        })

        tugasMataKuliahListFragmentViewModel.navigateToAddTugasKuliah.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                val intent = Intent(requireActivity(), AddTugasKuliahActivity::class.java)
                intent.putExtra("subjectTugasKuliahId", subjectId)
                this.startActivity(intent)
                tugasMataKuliahListFragmentViewModel.onAddTugasKuliahNavigated()
            }
        })

        tugasMataKuliahListFragmentViewModel.navigateToEditTugasKuliah.observe(
            viewLifecycleOwner,
            Observer { tugas ->
                tugas?.let {
//                    this.findNavController().navigate(
//                        TugasMataKuliahListFragmentDirections.actionHomeFragmentToEditTugasFragment(
//                            it
//                        )
//                    )

                    val intent = Intent(requireActivity(), EditTugasKuliahActivity::class.java)
                    intent.putExtra("tugasKuliahId", it)
                    this.startActivity(intent)
                    tugasMataKuliahListFragmentViewModel.onEditTugasKuliahNavigated()
                }
            })

        binding.tugasMataKuliahListFragmentViewModel = tugasMataKuliahListFragmentViewModel
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
        binding.tugasList.layoutManager = manager

        mHandlerForUpdateCurrentTime = Handler()
        mRunnable = Runnable {
            mHandlerForUpdateCurrentTime.postDelayed(mRunnable, 1000)
            binding.realTimeClockinTugasMataKuliahList.text = "Waktu saat ini: " + convertLongToDateTimeFormatted(System.currentTimeMillis())
        }
        mHandlerForUpdateCurrentTime.post(mRunnable)



        uiScope.launch {
            val title: String = "Tugas " + subjectTugasKuliahDataSource.loadSubjectTugasKuliahNameById(subjectId) + " Saya"
            parentActivity.setTitle(title)
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tugaskuliahsaya_menu, menu)
        var actionViewTaskCompletionHistory = menu.findItem(R.id.actionTaskCompletionHistory)
//        var action_delete = menu.findItem(R.id.actionViewTaskCompletionHistory)
//        action_save.setIcon(R.drawable.ic_baseline_save_24)
//        action_delete.setIcon(R.drawable.ic_baseline_delete_forever_24)
        View_utilities.menuIconColor(actionViewTaskCompletionHistory, Color.BLACK)
//        menuIconColor(action_delete, Color.BLACK)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionTaskCompletionHistory -> {
                goToTugasKuliahCompletionHistory()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }

    override fun onResume() {
        super.onResume()
        mHandlerForUpdateCurrentTime.post(mRunnable)
    }

    override fun onStop() {
        super.onStop()
        mHandlerForUpdateCurrentTime.removeCallbacks(mRunnable)
    }

    private fun goToTugasKuliahCompletionHistory() {
//        this.findNavController()
//            .navigate(TugasMataKuliahListFragmentDirections.actionHomeFragmentToTaskCompletionHistoryFragment())
//        homeFragmentViewModel.onTaskCompletionHistoryNavigated()
    }
}