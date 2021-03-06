package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentAddTugasKuliahFinishCommitmentBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.RangeTimePickerDialog
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.View_utilities
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.AddTugasKuliahFinishCommitmentFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.AddTugasKuliahFinishCommitmentFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddTugasKuliahFinishCommitmentFragment: Fragment() {
    private lateinit var binding: FragmentAddTugasKuliahFinishCommitmentBinding
    lateinit var addTugasKuliahFinishCommitmentFragmentViewModel: AddTugasKuliahFinishCommitmentFragmentViewModel
    private lateinit var mTugas: TugasKuliah

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (shared_data.isFromOnboarding)
        {
            mTugas = onboarding_data.mTugas
        }
        else
        {
            mTugas = shared_data.mTugas
        }
        setHasOptionsMenu(true)
        val application = requireNotNull(this.activity).application
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas_kuliah_finish_commitment, container, false)
        binding.textView.text = "Kapan Anda akan menyelesaikan " +  mTugas.tugasKuliahName + " (Maksimal 75% dari waktu saat ini sebelum tenggat waktu)"
        binding.textView2.text = "Anda akan diperingatkan bahwa anda menunda menyelesaikan " +  mTugas.tugasKuliahName + " apabila sudah melewati waktu yang sudah ditentukan dibawah."



        binding.editDeadline.inputType = InputType.TYPE_NULL
        binding.editDeadline.isFocusable = false

        binding.editJam.inputType = InputType.TYPE_NULL
        binding.editJam.isFocusable = false

        val tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        val tugasKuliahImageDataSource = AppDatabase.getInstance(application).getTugasKuliahImageDao
        val tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        val viewModelFactory = AddTugasKuliahFinishCommitmentFragmentViewModelFactory(application, tugasKuliahDataSource, tugasKuliahImageDataSource, tugasKuliahToDoListDataSource)
        addTugasKuliahFinishCommitmentFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddTugasKuliahFinishCommitmentFragmentViewModel::class.java
        )

        finish_commitment_components.timeRangeCanBeSelected = (mTugas.deadline - System.currentTimeMillis()) * 0.25

        finish_commitment_components.day = ((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 86400000

        finish_commitment_components.hour = ((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 3600000
        finish_commitment_components.minute = (((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / finish_commitment_components.hour) % 60


        addTugasKuliahFinishCommitmentFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                var cal = Calendar.getInstance()


                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        binding.editJam.setText(SimpleDateFormat("H:mm").format(cal.time))
                        binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam_commitment)
                    }
                val timePickerDialog: RangeTimePickerDialog =
                    RangeTimePickerDialog(
                        context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                            Calendar.MINUTE
                        ), true
                    )

                if (finish_commitment_components.day < 1)
                {
                    if (deadline_components.day == finish_commitment_components.day)
                    {
                        timePickerDialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                        timePickerDialog.setMin(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                        timePickerDialog.setMax(finish_commitment_components.hour.toInt(), finish_commitment_components.minute.toInt())
                    }
                    else
                    {
                        timePickerDialog.updateTime(finish_commitment_components.hour.toInt(), finish_commitment_components.minute.toInt())
                        timePickerDialog.setMax(finish_commitment_components.hour.toInt(), finish_commitment_components.minute.toInt())
                    }

                }
                else
                {
                    val cal4 = Calendar.getInstance()
                    cal4.timeInMillis = finish_commitment_components.TimeSelected

                    if (cal.get(Calendar.DATE) == cal4.get(Calendar.DATE))
                    {
                        timePickerDialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                        timePickerDialog.setMin(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                    }
                    else
                    {
                        //bebas
                    }

                }
                timePickerDialog.show()
                addTugasKuliahFinishCommitmentFragmentViewModel.doneLoadTimePicker()
            }
        })

        addTugasKuliahFinishCommitmentFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 0)
                        finish_commitment_components.TimeSelected = cal.timeInMillis
                        finish_commitment_components.day = ((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 86400000
                        finish_commitment_components.hour = (((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 3600000) % 24
                        finish_commitment_components.minute = (((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / (60000)) % 60
                        if (finish_commitment_components.day < 1) {
                            binding.editJam.text = null
                            if (finish_commitment_components.hour < 9) {
                                if (finish_commitment_components.minute < 10) {
                                    binding.inputJam.hint = "Jam Target Selesai (" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                } else {
                                    binding.inputJam.hint = "Jam Target Selesai (" + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                }
                            }
                            else
                            {
                                if (deadline_components.day == finish_commitment_components.day)
                                {

                                    if (finish_commitment_components.minute < 10)
                                    {
                                        binding.inputJam.hint = "Jam Target Selesai ("+ finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                    }
                                    else
                                    {
                                        binding.inputJam.hint = "Jam Target Selesai ("+ finish_commitment_components.hour + ":" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                    }
                                }
                                else
                                {
                                    if (finish_commitment_components.minute < 10)
                                    {
                                        binding.inputJam.hint = "Jam Target Selesai (9:00, Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                    }
                                    else
                                    {
                                        binding.inputJam.hint = "Jam Target Selesai (9:00, Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                    }
                                }

                            }
                        }
                        else
                        {
                            val cal4 = Calendar.getInstance()


                            if (cal.get(Calendar.DATE) == cal4.get(Calendar.DATE))
                            {
                                binding.editJam.text = null
                                if (cal4.get(Calendar.MINUTE) < 10)
                                {
                                    binding.inputJam.hint = "Jam Target Selesai ("+ cal4.get(Calendar.HOUR_OF_DAY) + ":0" + cal4.get(Calendar.MINUTE) + ", Min " + cal4.get(Calendar.HOUR_OF_DAY) + ":0" + cal4.get(Calendar.MINUTE) + ")"
                                }
                                else
                                {
                                    binding.inputJam.hint = "Jam Target Selesai ("+ cal4.get(Calendar.HOUR_OF_DAY) + ":" + cal4.get(Calendar.MINUTE) + ", Min " + cal4.get(Calendar.HOUR_OF_DAY) + ":" + cal4.get(Calendar.MINUTE) + ")"
                                }
                            }
                            else
                            {
                                if (binding.editJam.text.isNullOrBlank())
                                {
                                    binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam_commitment) + " (9:00)"
                                }
                                else
                                {
                                    binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam)
                                }
                            }
                        }
                        binding.editDeadline.setText(SimpleDateFormat("dd - MM - yyyy").format(cal.time))
                    }


                context?.let { it1 ->
                    val datePickerDialog: DatePickerDialog = DatePickerDialog(
                        it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                            Calendar.MONTH
                        ), cal.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.datePicker.maxDate = (mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong()
                    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                    datePickerDialog.show()
                }
                addTugasKuliahFinishCommitmentFragmentViewModel.doneLoadDatePicker()
            }
        })

        addTugasKuliahFinishCommitmentFragmentViewModel.addTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
                    if (TextUtils.isEmpty(binding.editDeadline.text))
                    {
                        binding.editDeadline.error = context?.getString(R.string.finish_commitment_error)
                        Toast.makeText(context,binding.editDeadline.error, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        // Convert Long to Date atau sebaliknya di https://currentmillis.com/
                        var clock = "9:00"
                        if (finish_commitment_components.day < 1)
                        {
                            if (finish_commitment_components.hour < 9)
                            {
                                if (finish_commitment_components.minute < 10)
                                {
                                    clock = "" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute
                                }
                                else
                                {
                                    clock = "" + finish_commitment_components.hour + ":" + finish_commitment_components.minute
                                }
                            }
                            else
                            {
                                if (deadline_components.day == finish_commitment_components.day)
                                {
                                    if (finish_commitment_components.minute < 10)
                                    {
                                        clock = "" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute
                                    }
                                    else
                                    {
                                        clock = "" + finish_commitment_components.hour + ":" + finish_commitment_components.minute
                                    }
                                }
                                else
                                {
                                    clock = "9:00"
                                }
                            }
                        }
                        else
                        {
                            val cal = Calendar.getInstance()
                            val cal2 = Calendar.getInstance()

                            cal2.timeInMillis = finish_commitment_components.TimeSelected

                            if (cal.get(Calendar.DATE) == cal2.get(Calendar.DATE))
                            {
                                if (cal.get(Calendar.MINUTE) < 10)
                                {
                                    clock = "" + cal.get(Calendar.HOUR_OF_DAY) + ":0" + cal.get(Calendar.MINUTE)
                                }
                                else
                                {
                                    clock = "" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
                                }
                            }
                            else
                            {
                                clock = "9:00"
                            }

                        }

                        if (binding.editJam.text.toString() != "")
                        {
                            clock = binding.editJam.text.toString()
                        }

                        if (binding.editDeadline.text.toString() != "")
                        {
                            mTugas.finishCommitment = View_utilities.convertDateAndTimeToLong(
                                binding.editDeadline.text.toString(),
                                clock
                            )
                        }


                        val inputMethodManager =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                        if (shared_data.isFromOnboarding)
                        {
                            onboarding_data.mSubjectName?.let { it1 ->
                                addTugasKuliahFinishCommitmentFragmentViewModel.addSubjectAndTugasKuliah(requireContext(), mTugas,
                                    it1
                                )
                            }
                            onboarding_data.approvalForGoToFifthScreen = true
                        }
                        else
                        {
                            addTugasKuliahFinishCommitmentFragmentViewModel.addTugasKuliah(requireContext(), mTugas)
                            requireActivity().finish()
                        }

                        Toast.makeText(context,"Tugas Kuliah " + mTugas.tugasKuliahName + " disimpan.", Toast.LENGTH_LONG).show()

                        addTugasKuliahFinishCommitmentFragmentViewModel.doneNavigating()
                    }
                }
            })

        binding.addTugasKuliahFinishCommitmentFragmentViewModel = addTugasKuliahFinishCommitmentFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addtugas_menu, menu)
        var action_save = menu.findItem(R.id.actionSaveTugas)
        action_save.setIcon(R.drawable.ic_baseline_save_24)
        View_utilities.menuIconColor(action_save, Color.BLACK)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                addTugasKuliahFinishCommitmentFragmentViewModel.onAddTugasKuliahClicked2()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}