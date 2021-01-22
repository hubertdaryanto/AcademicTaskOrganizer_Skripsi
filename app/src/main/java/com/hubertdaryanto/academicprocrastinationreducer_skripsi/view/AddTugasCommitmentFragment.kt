package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.CustomDialog.RangeTimePickerDialog
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentAddTugasCommitmentBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.AddTugasCommitmentFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.AddTugasCommitmentFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddTugasCommitmentFragment: Fragment() {
    private lateinit var binding: FragmentAddTugasCommitmentBinding
    private lateinit var addTugasCommitmentFragmentViewModel: AddTugasCommitmentFragmentViewModel



//    private var mTugas: TugasKuliah = arguments?.get("TugasKuliahTransfer") as TugasKuliah
    private var mTugas: TugasKuliah = shared_data.mTugas

    private var TimeRangeCanBeSelected = (mTugas.deadline - System.currentTimeMillis()) * 0.25//up to 25% time remaining

    private var TimeSelected: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas_commitment, container, false)

        binding.editDeadline.inputType = InputType.TYPE_NULL
        binding.editDeadline.isFocusable = false

        binding.editJam.inputType = InputType.TYPE_NULL
        binding.editJam.isFocusable = false
        binding.inputJam.hint = "Jam Target Selesai (9:00)"

        val dataSource = AppDatabase.getInstance(application).getAllQueryListDao
        val viewModelFactory = AddTugasCommitmentFragmentViewModelFactory(application, dataSource)
        addTugasCommitmentFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddTugasCommitmentFragmentViewModel::class.java
        )

        var day = ((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / 86400000

        var hour = ((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / 3600000
        var minute = (((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / hour) % 60


        addTugasCommitmentFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var cal = Calendar.getInstance()


                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        binding.editJam.setText(SimpleDateFormat("H:mm").format(cal.time))
                        binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam_commitment)
                    }
                val timePickerDialog: RangeTimePickerDialog = RangeTimePickerDialog(
                    context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                        Calendar.MINUTE
                    ), true
                )

                if (day < 1)
                {
                    timePickerDialog.updateTime(hour.toInt(), minute.toInt())
                    timePickerDialog.setMax(hour.toInt(), minute.toInt())

                }
                timePickerDialog.show()
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark)
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark)
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.colorPrimaryDark)
                addTugasCommitmentFragmentViewModel.doneLoadTimePicker()
            }
        })

        addTugasCommitmentFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 0)
                        TimeSelected = cal.timeInMillis
                        day = ((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / 86400000
                        hour = ((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / 3600000
                        minute = (((mTugas.deadline - TimeRangeCanBeSelected).toLong() - TimeSelected) / hour) % 60
                        binding.editDeadline.setText(SimpleDateFormat("dd - MM - yyyy").format(cal.time))
                        if (day < 1)
                        {
                            if (hour < 9)
                            {
                                if (minute < 10)
                                {
                                    binding.inputJam.hint = "Jam Target Selesai (" + hour + ":0" + minute + ", Max " + hour + ":0" + minute + ")"
                                }
                                else
                                {
                                    binding.inputJam.hint = "Jam Target Selesai (" + hour + ":" + minute + ", Max " + hour + ":" + minute + ")"
                                }

                            }
                            else
                            {
                                if (minute < 10)
                                {
                                    binding.inputJam.hint = "Jam Target Selesai (9:00, Max " + hour + ":0" + minute + ")"
                                }
                                else
                                {
                                    binding.inputJam.hint = "Jam Target Selesai (9:00, Max " + hour + ":" + minute + ")"
                                }
                            }
                        }
                        else
                        {
                            binding.inputJam.hint = "Jam Target Selesai (9:00)"
                        }
                    }


                context?.let { it1 ->
                    val datePickerDialog: DatePickerDialog = DatePickerDialog(
                        it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                            Calendar.MONTH
                        ), cal.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.datePicker.maxDate = (mTugas.deadline - TimeRangeCanBeSelected).toLong()
                    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                    datePickerDialog.show()
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark)
                }
                addTugasCommitmentFragmentViewModel.doneLoadDatePicker()
            }
        })

        addTugasCommitmentFragmentViewModel.addTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
                    if (TextUtils.isEmpty(binding.editDeadline.text))
                    {
                        binding.editDeadline.setError(context?.getString(R.string.deadline_error))
                        Toast.makeText(context,binding.editDeadline.error, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        // Convert Long to Date atau sebaliknya di https://currentmillis.com/

                        var clock = "9:00"
                        if (day < 1)
                        {
                            clock = "" + hour + ":" + minute
                        }
                        else
                        {
                            clock = "9:00"
                        }

                        if (binding.editJam.text.toString() != "")
                        {
                            clock = binding.editJam.text.toString()
                        }

                        if (binding.editDeadline.text.toString() != "")
                        {
                            mTugas.finishCommitment = view_utilities.convertDateAndTimeToLong(
                                binding.editDeadline.text.toString(),
                                clock
                            )
                        }

//                        mTugas.notes = binding.editCatatan.text.toString().trim()

                        val inputMethodManager =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                        addTugasCommitmentFragmentViewModel.addTugasKuliah(requireContext(), mTugas)
                        context?.getString(R.string.inserted_tugas_kuliah_message)?.let { it1 ->
                            context?.toast(
                                it1
                            )
                        }
                        this.findNavController().popBackStack(R.id.homeFragment, false)//cari cara buat langusng ke home fragment
                        addTugasCommitmentFragmentViewModel.doneNavigating()
                    }


                }


            })

        binding.addTugasCommitmentFragmentViewModel = addTugasCommitmentFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addtugas_menu, menu)
        var action_save = menu.findItem(R.id.actionSaveTugas)
        action_save.setIcon(R.drawable.ic_baseline_save_24)
        view_utilities.menuIconColor(action_save, Color.BLACK)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                addTugasCommitmentFragmentViewModel.onAddTugasKuliahClicked2()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}