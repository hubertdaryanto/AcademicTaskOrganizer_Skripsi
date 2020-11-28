package com.example.academictaskorganizer_skripsi.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.database.subjectDao
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddTugasFragment : BaseFragment() {

    private lateinit var binding: FragmentAddTugasBinding
    private lateinit var SubjectDataSource: subjectDao

    private var subjectId by Delegates.notNull<Long>()

    val TAG: String = this::class.java.getSimpleName()
    private val TARGET_FRAGMENT_REQUEST_CODE = 1
    private val EXTRA_GREETING_MESSAGE: String = "message"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas, container, false)

        val dataSource = AppDatabase.getInstance(application).getTugasDao
        SubjectDataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = AddTugasFragmentViewModelFactory(application, dataSource)

        val addTugasFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddTugasFragmentViewModel::class.java)


        addTugasFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        binding.editJam.setText(SimpleDateFormat("H:mm").format(cal.time))
                    }
                TimePickerDialog(
                    context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                        Calendar.MINUTE
                    ), true
                ).show()
                addTugasFragmentViewModel.doneLoadTimePicker()
            }
        })

        addTugasFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        binding.editDeadline.setText(SimpleDateFormat("dd-MM-yyyy").format(cal.time))
                    }
                context?.let { it1 ->
                    DatePickerDialog(
                        it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                            Calendar.MONTH
                        ), cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                addTugasFragmentViewModel.doneLoadDatePicker()
            }
        })

        addTugasFragmentViewModel.showSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val dialog = SubjectDialogFragment()
                dialog.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
                dialog.show(parentFragmentManager, dialog.TAG)
            }
        })

        addTugasFragmentViewModel.addTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
                    val tugasTitle = binding.editTextTugas.text.toString().trim()
                    val tugasSubjectId: Long = subjectId
                    // Convert Long to Date atau sebaliknya di https://currentmillis.com/
                    val tugasDeadline: Long = convertDateAndTimeToLong(
                        binding.editDeadline.text.toString(),
                        binding.editJam.text.toString()
                    )
                    val tugasToDoListId: Long = 0
                    val tugasNotes = binding.editCatatan.text.toString().trim()
                    val tugasGambar: Long = 0
//                    var fromBinusmayaId: Long = -1

                    val inputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

//            if (tugasTitle.isEmpty())
//            {
//                editTextTugas.error = "tugas required"
//                editTextTugas.requestFocus()
//                return@setOnClickListener
//            }
//                launch {
////
//                    context?.let {
                    val mTugas = TugasKuliah(
                        subjectId = tugasSubjectId,
                        tugasKuliahName = tugasTitle,
                        deadline = tugasDeadline,
                        toDoListId = tugasToDoListId,
                        isFinished = false,
                        notes = tugasNotes,
                        imageId = tugasGambar
//                        fromBinusmayaId
                    )
                    addTugasFragmentViewModel.addTugasKuliah(mTugas)
                    context?.toast("Note Saved")


//                        val action = AddTugasFragmentDirections.actionAddTugasFragmentToHomeFragment()
//                        this.find.navigate(action)

//                    }
//                }
                    this.findNavController()
                        .navigate(AddTugasFragmentDirections.actionAddTugasFragmentToHomeFragment())
                    addTugasFragmentViewModel.doneNavigating()
                }


            })

        binding.addTugasFragmentViewModel = addTugasFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
    }


    private fun updateTugasKuliahTextBox(view: View) {
        binding.editTextTugas.visibility = View.VISIBLE
        binding.editTextTugas.requestFocus()
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextTugas, 0)
    }


    private fun convertDateAndTimeToLong(date: String, time: String): Long {
        val formatter = SimpleDateFormat("dd-MM-yyyy H:mm")
        val date = formatter.parse(date + " " + time)
        return date.time
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            val greeting = data?.getLongExtra(EXTRA_GREETING_MESSAGE, 0L)
            if (greeting != null) {
                subjectId = greeting
                binding.editTextSubject.setText(convertSubjectIdToSubjectName(greeting))
            }

        }
    }

    fun newIntent(message: Long?): Intent? {
        val intent = Intent()
        intent.putExtra(EXTRA_GREETING_MESSAGE, message)
        return intent
    }

    fun getInstance(): AddTugasFragment? {
        return this
    }

    fun convertSubjectIdToSubjectName(id: Long): String {
        var string = ""
        launch {
             string = SubjectDataSource.loadSubjectNameById(id)
        }
        return string
        //masih gak ke load ya
    }

}
