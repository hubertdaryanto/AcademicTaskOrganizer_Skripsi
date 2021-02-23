package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentEditTugasKuliahBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.ImageForTugasKuliahAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter.TugasKuliahToDoListAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.RangeTimePickerDialog
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.RecyclerViewItemDecoration
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.components.View_utilities
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.EditTugasKuliahFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.EditTugasKuliahFragmentViewModelFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditTugasKuliahFragment: Fragment() {
    private lateinit var binding: FragmentEditTugasKuliahBinding
    private lateinit var tugasKuliahDataSource: tugasKuliahDao
    private lateinit var tugasKuliahToDoListDataSource: tugasKuliahToDoListDao
    private lateinit var tugasKuliahImageDataSource: tugasKuliahImageDao
    private lateinit var subjectTugasKuliahDataSource: subjectTugasKuliahDao
    private lateinit var tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao
    private lateinit var editTugasKuliahFragmentViewModel: EditTugasKuliahFragmentViewModel

    private var subjectTugasKuliahId = 0L


//    val TAG: String = this::class.java.simpleName
    private val TARGET_FRAGMENT_REQUEST_CODE = 1

    private val YOUR_IMAGE_CODE = 2
    private val EXTRA_GREETING_MESSAGE: String = "message"

    private lateinit var mTugas: TugasKuliah

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!binding.editTextTugas.text?.toString()
                    .equals(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.tugasKuliahName) || !binding.editTextSubject.text?.toString()
                    .equals(editTugasKuliahFragmentViewModel.subjectTugasKuliahTextBefore.value) || !binding.editDeadline.text?.toString()
                    .equals(SimpleDateFormat("dd - MM - yyyy").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.deadline)) || !binding.editJam.text?.toString()
                    .equals(SimpleDateFormat("H:mm").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.deadline)) || !binding.editDeadline2.text?.toString()
                    .equals(SimpleDateFormat("dd - MM - yyyy").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.finishCommitment)) || !binding.editJam2.text?.toString()
                    .equals(SimpleDateFormat("H:mm").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.finishCommitment)) || !binding.editCatatan.text?.toString()
                    .equals(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.notes) || editTugasKuliahFragmentViewModel.tugasKuliahToDoList.value != editTugasKuliahFragmentViewModel.tugasKuliahToDoListBefore.value || editTugasKuliahFragmentViewModel.tugasKuliahImageList.value != editTugasKuliahFragmentViewModel.tugasKuliahImageListBefore.value || binding.SelesaiCheckBox.isChecked != editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.isFinished)
            {
                backAndUpButtonHandlerWhenHaveSomeDataChanged()
            }
            else
            {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_edit_tugas_kuliah, container, false)

        binding.editTextSubject.inputType = InputType.TYPE_NULL
        binding.editTextSubject.isFocusable = false

        binding.editDeadline.inputType = InputType.TYPE_NULL
        binding.editDeadline.isFocusable = false

        binding.editJam.inputType = InputType.TYPE_NULL
        binding.editJam.isFocusable = false

        binding.editDeadline2.inputType = InputType.TYPE_NULL
        binding.editDeadline2.isFocusable = false

        binding.editJam2.inputType = InputType.TYPE_NULL
        binding.editJam2.isFocusable = false



        tugasKuliahDataSource = AppDatabase.getInstance(application).getTugasKuliahDao
        tugasKuliahToDoListDataSource = AppDatabase.getInstance(application).getTugasKuliahToDoListDao
        tugasKuliahImageDataSource = AppDatabase.getInstance(application).getTugasKuliahImageDao
        subjectTugasKuliahDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        tugasKuliahCompletionHistoryDataSource = AppDatabase.getInstance(application).getTugasKuliahCompletionHistoryDao
        val viewModelFactory = EditTugasKuliahFragmentViewModelFactory(application, tugasKuliahDataSource, tugasKuliahToDoListDataSource, tugasKuliahImageDataSource, subjectTugasKuliahDataSource, tugasKuliahCompletionHistoryDataSource)
        editTugasKuliahFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            EditTugasKuliahFragmentViewModel::class.java
        )

        arguments?.let {
            val tugasKuliahId: Long = it.get("tugasKuliahId") as Long
            editTugasKuliahFragmentViewModel.loadTugasKuliah(tugasKuliahId)
        }


        editTugasKuliahFragmentViewModel.tugasKuliah.observe(viewLifecycleOwner, Observer {
            it?.let {
                mTugas = it
                val cal = Calendar.getInstance()

                cal.timeInMillis = mTugas.finishCommitment

                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                finish_commitment_components.TimeSelected = cal.timeInMillis

                finish_commitment_components.timeRangeCanBeSelected = (mTugas.deadline - System.currentTimeMillis()) * 0.25

                finish_commitment_components.day = ((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 86400000
                finish_commitment_components.hour = (((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / 3600000) % 24
                finish_commitment_components.minute = (((mTugas.deadline - finish_commitment_components.timeRangeCanBeSelected).toLong() - finish_commitment_components.TimeSelected) / (60000)) % 60

                val cal2 = Calendar.getInstance()

                cal2.timeInMillis = mTugas.deadline

                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 1)
                deadline_components.TimeSelected = cal2.timeInMillis

                val cal3 = Calendar.getInstance()
                cal3.set(Calendar.HOUR_OF_DAY, 0)
                cal3.set(Calendar.MINUTE, 0)
                deadline_components.day = (deadline_components.TimeSelected - cal3.timeInMillis) / 86400000
                deadline_components.hour = ((deadline_components.TimeSelected - cal3.timeInMillis) / 3600000) % 24
                deadline_components.minute = ((deadline_components.TimeSelected - cal3.timeInMillis) / (60000)) % 60

//                binding.editTextSubject.setText(it.tugasSubjectId.toString())
                editTugasKuliahFragmentViewModel.convertSubjectIdToSubjectName(it.tugasKuliahSubjectId)
                binding.editTextTugas.setText(it.tugasKuliahName)
                binding.editDeadline.setText(SimpleDateFormat("dd - MM - yyyy").format(it.deadline))
                binding.editJam.setText(SimpleDateFormat("H:mm").format(it.deadline))
                binding.editDeadline2.setText(SimpleDateFormat("dd - MM - yyyy").format(it.finishCommitment))
                binding.editJam2.setText(SimpleDateFormat("H:mm").format(it.finishCommitment))
                binding.SelesaiCheckBox.isChecked = it.isFinished
                binding.editCatatan.setText(it.notes)
            }
        })

        editTugasKuliahFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()


                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        binding.editJam.setText(SimpleDateFormat("H:mm").format(cal.time))
                        binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam)
                    }
                val timePickerDialog: RangeTimePickerDialog =
                    RangeTimePickerDialog(
                        context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                            Calendar.MINUTE
                        ), true
                    )

                if (deadline_components.day < 1)
                {

                    timePickerDialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                    timePickerDialog.setMin(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                }
                else
                {
                    timePickerDialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                    timePickerDialog.setMin(0, 0)
                }
                timePickerDialog.show()
                editTugasKuliahFragmentViewModel.doneLoadTimePicker()
            }
        })

        editTugasKuliahFragmentViewModel.showTimePicker2.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        binding.editJam2.setText(SimpleDateFormat("H:mm").format(cal.time))
                        binding.inputJam2.hint = context?.getString(R.string.tugaskuliah_hint_jam_commitment)
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
                editTugasKuliahFragmentViewModel.doneLoadTimePicker2()
            }
        })

        editTugasKuliahFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        cal.set(Calendar.HOUR_OF_DAY, 9)
                        cal.set(Calendar.MINUTE, 1)
                        deadline_components.TimeSelected = cal.timeInMillis
                        val cal2 = Calendar.getInstance()
                        cal2.set(Calendar.HOUR_OF_DAY, 9)
                        cal2.set(Calendar.MINUTE, 0)
                        deadline_components.day = (deadline_components.TimeSelected - cal2.timeInMillis) / 86400000
                        deadline_components.hour = ((deadline_components.TimeSelected - cal2.timeInMillis) / 3600000) % 24
                        deadline_components.minute = ((deadline_components.TimeSelected - cal2.timeInMillis) / (60000)) % 60
                        if (deadline_components.day < 1)
                        {
                            val cal3 = Calendar.getInstance()
                            binding.editJam.text = null
                            deadline_components.hour = cal3.get(Calendar.HOUR_OF_DAY).toLong()
                            deadline_components.minute = cal3.get(Calendar.MINUTE).toLong()
                            if (cal3.get(Calendar.HOUR_OF_DAY) >= 9)
                            {
                                if (cal3.get(Calendar.MINUTE) < 10)
                                {
                                    binding.inputJam.hint = "Jam Tenggat Waktu (" + cal3.get(Calendar.HOUR_OF_DAY) + ":0" + cal3.get(Calendar.MINUTE) + ", Min " + cal3.get(Calendar.HOUR_OF_DAY) + ":0" + cal3.get(Calendar.MINUTE) + ")"
                                }
                                else
                                {
                                    binding.inputJam.hint = "Jam Tenggat Waktu (" + cal3.get(Calendar.HOUR_OF_DAY) + ":" + cal3.get(Calendar.MINUTE) + ", Min " + cal3.get(Calendar.HOUR_OF_DAY) + ":" + cal3.get(Calendar.MINUTE) + ")"
                                }

                            }
                            else
                            {
                                binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_editJam)
                            }
                        }
                        else
                        {
                            if (binding.editJam.text.isNullOrBlank())
                            {
                                binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_editJam)
                            }
                            else
                            {
                                binding.inputJam.hint = context?.getString(R.string.tugaskuliah_hint_jam)
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
                    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                    datePickerDialog.show()
                }
                editTugasKuliahFragmentViewModel.doneLoadDatePicker()
            }
        })

        editTugasKuliahFragmentViewModel.showDatePicker2.observe(viewLifecycleOwner, Observer {
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
                            binding.editJam2.text = null
                            if (finish_commitment_components.hour < 9) {
                                if (finish_commitment_components.minute < 10) {
                                    binding.inputJam2.hint =
                                        "Jam Target Selesai (" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                } else {
                                    binding.inputJam2.hint =
                                        "Jam Target Selesai (" + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                }
                            }
                            else
                            {
                                if (deadline_components.day == finish_commitment_components.day)
                                {

                                    if (finish_commitment_components.minute < 10)
                                    {
                                        binding.inputJam2.hint = "Jam Target Selesai ("+ finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                    }
                                    else
                                    {
                                        binding.inputJam2.hint = "Jam Target Selesai ("+ finish_commitment_components.hour + ":" + finish_commitment_components.minute + ", Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                    }
                                }
                                else
                                {
                                    if (finish_commitment_components.minute < 10)
                                    {
                                        binding.inputJam2.hint = "Jam Target Selesai (9:00, Max " + finish_commitment_components.hour + ":0" + finish_commitment_components.minute + ")"
                                    }
                                    else
                                    {
                                        binding.inputJam2.hint = "Jam Target Selesai (9:00, Max " + finish_commitment_components.hour + ":" + finish_commitment_components.minute + ")"
                                    }
                                }

                            }
                        }
                        else
                        {
                            val cal4 = Calendar.getInstance()


                            if (cal.get(Calendar.DATE) == cal4.get(Calendar.DATE))
                            {
                                binding.editJam2.text = null
                                if (cal4.get(Calendar.MINUTE) < 10)
                                {
                                    binding.inputJam2.hint = "Jam Target Selesai ("+ cal4.get(Calendar.HOUR_OF_DAY) + ":0" + cal4.get(Calendar.MINUTE) + ", Min " + cal4.get(Calendar.HOUR_OF_DAY) + ":0" + cal4.get(Calendar.MINUTE) + ")"
                                }
                                else
                                {
                                    binding.inputJam2.hint = "Jam Target Selesai ("+ cal4.get(Calendar.HOUR_OF_DAY) + ":" + cal4.get(Calendar.MINUTE) + ", Min " + cal4.get(Calendar.HOUR_OF_DAY) + ":" + cal4.get(Calendar.MINUTE) + ")"
                                }
                            }
                            else
                            {
                                if (binding.editJam2.text.isNullOrBlank())
                                {
                                    binding.inputJam2.hint = context?.getString(R.string.tugaskuliah_hint_jam_commitment) + " (9:00)"
                                }
                                else
                                {
                                    binding.inputJam2.hint = context?.getString(R.string.tugaskuliah_hint_jam)
                                }
                            }
                        }
                        binding.editDeadline2.setText(SimpleDateFormat("dd - MM - yyyy").format(cal.time))
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
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark)
                }
                editTugasKuliahFragmentViewModel.doneLoadDatePicker2()
            }
        })

        editTugasKuliahFragmentViewModel.showSubjectTugasKuliahDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val dialog = ChooseSubjectTugasKuliahDialogFragment()
                dialog.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
                dialog.show(parentFragmentManager, dialog.TAG)
                editTugasKuliahFragmentViewModel.doneLoadSubjectTugasKuliahDialog()
            }
        })

        editTugasKuliahFragmentViewModel.editTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
                    if (TextUtils.isEmpty(binding.editTextSubject.text))
                    {
                        binding.editTextSubject.error = context?.getString(R.string.subject_error)
                        Toast.makeText(context,binding.editTextSubject.error, Toast.LENGTH_LONG).show()
                    }
                    else if (TextUtils.isEmpty(binding.editTextTugas.text))
                    {
                        binding.editTextTugas.error = context?.getString(R.string.tugas_error)
                        Toast.makeText(context,binding.editTextTugas.error, Toast.LENGTH_LONG).show()
                    }
                    else if (TextUtils.isEmpty(binding.editDeadline.text))
                    {
                        binding.editDeadline.error = context?.getString(R.string.deadline_error)
                        Toast.makeText(context,binding.editDeadline.error, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        editTugasKuliahFragmentViewModel._tugasKuliah.value!!.tugasKuliahName = binding.editTextTugas.text.toString().trim()
                        if (subjectTugasKuliahId != 0L)
                        {
                            editTugasKuliahFragmentViewModel._tugasKuliah.value!!.tugasKuliahSubjectId = subjectTugasKuliahId
                        }

                        // Convert Long to Date atau sebaliknya di https://currentmillis.com/
                        var clock = "9:00"
                        if (deadline_components.day < 1)
                        {
                            if (deadline_components.hour < 9)
                            {
                                clock = "9:00"
                            }
                            else
                            {
                                if (deadline_components.minute < 10)
                                {
                                    clock = "" + deadline_components.hour + ":0" + deadline_components.minute
                                }
                                else
                                {
                                    clock = "" + deadline_components.hour + ":" + deadline_components.minute
                                }
                            }
                        }
                        else
                        {
                            clock = "9:00"
                        }
                        var clock2 = "9:00"
                        if (finish_commitment_components.day < 1)
                        {
                            if (finish_commitment_components.hour < 9)
                            {
                                if (finish_commitment_components.minute < 10)
                                {
                                    clock2 = "" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute
                                }
                                else
                                {
                                    clock2 = "" + finish_commitment_components.hour + ":" + finish_commitment_components.minute
                                }
                            }
                            else
                            {
                                if (deadline_components.day == finish_commitment_components.day)
                                {
                                    if (finish_commitment_components.minute < 10)
                                    {
                                        clock2 = "" + finish_commitment_components.hour + ":0" + finish_commitment_components.minute
                                    }
                                    else
                                    {
                                        clock2 = "" + finish_commitment_components.hour + ":" + finish_commitment_components.minute
                                    }
                                }
                                else
                                {
                                    clock2 = "9:00"
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
                                    clock2 = "" + cal.get(Calendar.HOUR_OF_DAY) + ":0" + cal.get(Calendar.MINUTE)
                                }
                                else
                                {
                                    clock2 = "" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
                                }
                            }
                            else
                            {
                                clock2 = "9:00"
                            }

                        }
                        if (binding.editJam.text.toString() != "")
                        {
                            clock = binding.editJam.text.toString()
                        }

                        if (binding.editDeadline.text.toString() != "")
                        {
                            editTugasKuliahFragmentViewModel._tugasKuliah.value!!.deadline = convertDateAndTimeToLong(
                                binding.editDeadline.text.toString(),
                                clock
                            )
                        }

                        if (binding.editJam2.text.toString() != "")
                        {
                            clock2 = binding.editJam2.text.toString()
                        }

                        if (binding.editDeadline2.text.toString() != "")
                        {
                            editTugasKuliahFragmentViewModel._tugasKuliah.value!!.finishCommitment = convertDateAndTimeToLong(
                                binding.editDeadline2.text.toString(),
                                clock2
                            )
                        }

                        editTugasKuliahFragmentViewModel._tugasKuliah.value!!.notes = binding.editCatatan.text.toString().trim()

                        val inputMethodManager =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                        editTugasKuliahFragmentViewModel.updateTugasKuliah(requireContext(), editTugasKuliahFragmentViewModel.tugasKuliah.value!!)
                        Toast.makeText(context,"Tugas Kuliah " + mTugas.tugasKuliahName + " diperbarui.", Toast.LENGTH_LONG).show()
                        this.findNavController().popBackStack()
                        editTugasKuliahFragmentViewModel.doneNavigating()
                    }
                }
            })

        editTugasKuliahFragmentViewModel.subjectTugasKuliahText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextSubject.setText(it)
                editTugasKuliahFragmentViewModel.onSubjectTugasKuliahNameChanged()
            }
        })

        editTugasKuliahFragmentViewModel.tugasKuliahToDoListId.observe(viewLifecycleOwner, Observer {
            it?.let {
                editTugasKuliahFragmentViewModel.afterClickTugasKuliahToDoList()
            }
        })

        editTugasKuliahFragmentViewModel.tugasKuliahImageId.observe(viewLifecycleOwner, Observer {
            it?.let {
                editTugasKuliahFragmentViewModel.afterClickTugasKuliahImage()
            }
        })

        editTugasKuliahFragmentViewModel.addTugasKuliahToDoList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                addToDoList()
                editTugasKuliahFragmentViewModel.afterAddTugasKuliahToDoListClicked()
            }
        })

        editTugasKuliahFragmentViewModel.addTugasKuliahImage.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(
                    Intent.createChooser(intent, "Pilih Gambar"),
                    YOUR_IMAGE_CODE
                )


            }
        })

        val toDoListAdapter = TugasKuliahToDoListAdapter(TugasKuliahToDoListListener { toDoListId ->
            editTugasKuliahFragmentViewModel.onTugasKuliahToDoListClicked(toDoListId)
        }
            , object :
                TugasKuliahToDoListInterface {
                override fun onUpdateText(id: Long, data: String) {
                    editTugasKuliahFragmentViewModel.updateTugasKuliahToDoListName(id, data)
                }

                override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
                    editTugasKuliahFragmentViewModel.updateTugasKuliahToDoListIsFinished(id, isFinished)
                }

                override fun onRemoveItem(id: Long) {

                    if (editTugasKuliahFragmentViewModel._tugasKuliahToDoList.value?.get(id.toInt())?.tugasKuliahToDoListName?.isEmpty()!!)
                    {
                        editTugasKuliahFragmentViewModel.removeTugasKuliahToDoListItem(id)
                    }
                    else
                    {
                        AlertDialog.Builder(context).apply {
                            setTitle(context.getString(R.string.delete_todolist_confirmation_title))
                            setMessage(context.getString(R.string.delete_todolist_confirmation_subtitle))
                            setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                                editTugasKuliahFragmentViewModel.removeTugasKuliahToDoListItem(id)
                            }
                            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
                            }
                        }.create().show()
                    }

                }

                override fun onRemoveEmptyItem(id: Long) {
                    editTugasKuliahFragmentViewModel.removeTugasKuliahToDoListItem(id)
                }

                override fun onEnterPressed(id: Long) {
                    addToDoList()
                }
            }
        )
        binding.ToDoListRecyclerView.adapter = toDoListAdapter


        val gambarAdapter = ImageForTugasKuliahAdapter(TugasKuliahImageListener { imageId ->
            editTugasKuliahFragmentViewModel.onTugasKuliahImageClicked(imageId)
        }, object : TugasKuliahImageInterface {
            override fun onRemoveItem(id: Long) {
                AlertDialog.Builder(context).apply {
                    setTitle(context.getString(R.string.delete_image_confirmation_title))
                    setMessage(context.getString(R.string.delete_image_confirmation_subtitle))
                    setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                        editTugasKuliahFragmentViewModel.removeTugasKuliahImageItem(id)
                    }
                    setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

                    }
                }.create().show()

            }

        })
        binding.GambarRecyclerView.adapter = gambarAdapter

        editTugasKuliahFragmentViewModel.tugasKuliahToDoList.observe(viewLifecycleOwner, Observer {
            it?.let {

                if (it.count() == 0)
                {
                    binding.SelesaiCheckBox.visibility = View.VISIBLE
                    binding.SelesaiTextView.visibility = View.VISIBLE
                }
                else
                {
                    for (i in it)
                    {
                        if (i.isFinished == false && it.count() != 0)
                        {
                            binding.SelesaiCheckBox.visibility = View.GONE
                            binding.SelesaiCheckBox.isChecked = false
                            binding.SelesaiTextView.visibility = View.GONE
                            editTugasKuliahFragmentViewModel.updateIsFinishedStatus(binding.SelesaiCheckBox.isChecked)
                            break
                        }
                        else
                        {
                            binding.SelesaiCheckBox.visibility = View.VISIBLE
                            binding.SelesaiCheckBox.isChecked = true
                            binding.SelesaiTextView.visibility = View.VISIBLE
                            editTugasKuliahFragmentViewModel.updateIsFinishedStatus(binding.SelesaiCheckBox.isChecked)
                        }
                    }
                }
                toDoListAdapter.updateList(it)
            }
        })

        editTugasKuliahFragmentViewModel.tugasKuliahImageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                gambarAdapter.updateList(it)
            }
        })

        editTugasKuliahFragmentViewModel.onIsFinishedClicked.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                editTugasKuliahFragmentViewModel.updateIsFinishedStatus(binding.SelesaiCheckBox.isChecked)
                editTugasKuliahFragmentViewModel.afterIsFinishedClicked()
            }
        })

        val manager = LinearLayoutManager(activity)
//        manager.reverseLayout = true
//        manager.stackFromEnd = true
        val manager2 = LinearLayoutManager(activity)
        binding.ToDoListRecyclerView.addItemDecoration(
            RecyclerViewItemDecoration(
                0
            )
        )
        binding.ToDoListRecyclerView.layoutManager = manager
        binding.GambarRecyclerView.layoutManager = manager2
        binding.editTugasKuliahFragmentViewModel = editTugasKuliahFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun addToDoList() {
        val mToDoList =
            TugasKuliahToDoList(
                tugasKuliahToDoListName = "",
                bindToTugasKuliahId = editTugasKuliahFragmentViewModel.tugasKuliah.value!!.tugasKuliahId,
                isFinished = false,
                deadline = 0L
            )
        editTugasKuliahFragmentViewModel.addTugasKuliahToDoListItem(mToDoList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edittugas_menu, menu)
        var action_save = menu.findItem(R.id.actionSaveTugas)
        var action_delete = menu.findItem(R.id.actionDeleteTugas)
        action_save.setIcon(R.drawable.ic_baseline_save_24)
        action_delete.setIcon(R.drawable.ic_baseline_delete_forever_24)
        View_utilities.menuIconColor(action_save, Color.BLACK)
        View_utilities.menuIconColor(action_delete, Color.BLACK)
    }

//    private fun menuIconColor(menuItem: MenuItem, color: Int)
//    {
//        var drawable = menuItem.icon
//        if (drawable != null)
//        {
//            drawable.mutate()
//            drawable.setTint(color)
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                editTugasKuliahFragmentViewModel.onSaveTugasKuliahClicked()
                return true
            }
            R.id.actionDeleteTugas -> {deleteTugasKuliah()
                return true
            }
            android.R.id.home -> {
                if (!binding.editTextTugas.text?.toString()
                        .equals(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.tugasKuliahName) || !binding.editTextSubject.text?.toString()
                        .equals(editTugasKuliahFragmentViewModel.subjectTugasKuliahTextBefore.value) || !binding.editDeadline.text?.toString()
                        .equals(SimpleDateFormat("dd - MM - yyyy").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.deadline)) || !binding.editJam.text?.toString()
                        .equals(SimpleDateFormat("H:mm").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.deadline)) || !binding.editDeadline2.text?.toString()
                        .equals(SimpleDateFormat("dd - MM - yyyy").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.finishCommitment)) || !binding.editJam2.text?.toString()
                        .equals(SimpleDateFormat("H:mm").format(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.finishCommitment)) || !binding.editCatatan.text?.toString()
                        .equals(editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.notes) || editTugasKuliahFragmentViewModel.tugasKuliahToDoList.value != editTugasKuliahFragmentViewModel.tugasKuliahToDoListBefore.value || editTugasKuliahFragmentViewModel.tugasKuliahImageList.value != editTugasKuliahFragmentViewModel.tugasKuliahImageListBefore.value || binding.SelesaiCheckBox.isChecked != editTugasKuliahFragmentViewModel.tugasKuliahBefore.value?.isFinished)
                {
                    backAndUpButtonHandlerWhenHaveSomeDataChanged()
                }
                else
                {
                    requireActivity().onBackPressed()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun convertDateAndTimeToLong(date: String, time: String): Long {
        val formatter = SimpleDateFormat("dd - MM - yyyy H:mm")
        val date = formatter.parse(date + " " + time)
        return date.time
    }

    private fun backAndUpButtonHandlerWhenHaveSomeDataChanged() {
        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.back_confirmation_title))
            setMessage(context.getString(R.string.back_confirmation_subtitle))
            setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                activity?.finish()
            }
            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
            }
        }.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            val greeting = data?.getLongExtra(EXTRA_GREETING_MESSAGE, 0)
            if (greeting != null) {
                subjectTugasKuliahId = greeting
                editTugasKuliahFragmentViewModel.convertSubjectIdToSubjectName(greeting)
            }
        }

        if (requestCode == YOUR_IMAGE_CODE){
            if (resultCode == Activity.RESULT_OK)
                if (data != null) {
                    var selectedImageUri = data.data!!

                    val path: String = getRealPathFromURI(this.requireContext(), selectedImageUri)
                    val file: String = getFileName(this.requireContext(), selectedImageUri)
                    val mImage =
                        TugasKuliahImage(
                            bindToTugasKuliahId = editTugasKuliahFragmentViewModel.tugasKuliah.value!!.tugasKuliahId,
                            tugasKuliahImageName = path
                        )
                    editTugasKuliahFragmentViewModel.addTugasKuliahImageItem(mImage)
                    editTugasKuliahFragmentViewModel.afterAddTugasKuliahToDoListClicked()
                }
        }
    }

//    private fun insertInPrivateStorage(context: Context, name: String, path: String)
//    {
//        var fos: FileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
//
//        var file: File = File(context.filesDir, name)
//
//        var bytes: ByteArray = getBytesFromFile(file)
//
//        fos.write(bytes)
//        fos.close()
//
//        Toast.makeText(context, "File saved in :"+ context.filesDir + "/"+name, Toast.LENGTH_LONG).show()
//    }

    private fun getBytesFromFile(file: File): ByteArray
    {
        var data: ByteArray = file.readBytes()
        if (data == null)
        {
            throw IOException("No data")
        }
        return data
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String
    {

        var realPath = String()
        uri.path?.let { path ->

            val databaseUri: Uri
            val selection: String?
            val selectionArgs: Array<String>?
            if (path.contains("/document/image:")) { // files selected from "Documents"
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
            }
            else if (path.contains("/document/primary:"))
            {
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
            }
            else { // files selected from all other sources, especially on Samsung devices
                databaseUri = uri
                selection = null
                selectionArgs = null
            }
            try {
                val column = "_data"
                val projection = arrayOf(column)
                val cursor = context.contentResolver.query(
                    databaseUri,
                    projection,
                    selection,
                    selectionArgs,
                    null
                )
                cursor?.let {
                    if (it.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(column)
                        realPath = cursor.getString(columnIndex)
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        return realPath
    }

    private fun getFileName(context: Context, uri: Uri): String{
        var result: String = ""
        if (uri.scheme.equals("content"))
        {
            var cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
            finally {
                cursor?.close()
            }
        }
        if (result == null)
        {
            result = uri.path.toString()
            var cut: Int = result.lastIndexOf("/")
            if (cut != -1)
            {
                result = result.substring(cut + 1)
            }
        }

        return result
    }

    fun newIntent(message: Long?): Intent? {
        val intent = Intent()
        intent.putExtra(EXTRA_GREETING_MESSAGE, message)
        return intent
    }

    fun getInstance(): EditTugasKuliahFragment? {
        return this
    }



    private fun deleteTugasKuliah()
    {

        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.delete_tugas_confirmation_title))
            setMessage(context.getString(R.string.delete_tugas_confirmation_subtitle))
            setPositiveButton(context.getString(R.string.ya)) { _, _ ->

                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                Toast.makeText(context,"Tugas Kuliah " + mTugas.tugasKuliahName + " dihapus.", Toast.LENGTH_LONG).show()
                editTugasKuliahFragmentViewModel.deleteTugasKuliah(context)
                activity?.finish()
            }
            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

            }
        }.create().show()
    }

}