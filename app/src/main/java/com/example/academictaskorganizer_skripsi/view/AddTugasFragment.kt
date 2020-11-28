package com.example.academictaskorganizer_skripsi.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModelFactory
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTugasFragment : BaseFragment() {

    //    private var TugasKuliah: TugasKuliah? = null

    private lateinit var binding: FragmentAddTugasBinding
//    private lateinit var inputTextSubject: TextInputLayout
//    private lateinit var inputTextTugas: TextInputLayout
//    private lateinit var inputTextDeadline: TextInputLayout
//    private lateinit var inputTextJam: TextInputLayout
//    private lateinit var inputTextCatatan: TextInputLayout
//    private lateinit var editTextSubject: EditText
//    private lateinit var editTextTugas: EditText
//    private lateinit var editTextDeadline: EditText
//    private lateinit var editTextJam: EditText
//    private lateinit var editTextCatatan: EditText
//    private lateinit var buttonSave: FloatingActionButton
//    private lateinit var buttonAddToDoList: ImageButton
//    private lateinit var buttonAddGambar: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas, container, false)

        val dataSource = AppDatabase.getInstance(application).getTugasDao
        val viewModelFactory = AddTugasFragmentViewModelFactory(application, dataSource)

        val addTugasFragmentViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddTugasFragmentViewModel::class.java)


        addTugasFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    binding.editJam.setText(SimpleDateFormat("H:mm").format(cal.time))
                }
                TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                addTugasFragmentViewModel.doneLoadTimePicker()
            }
        })

        addTugasFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val cal = Calendar.getInstance()
                val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.editDeadline.setText(SimpleDateFormat("dd-MM-yyyy").format(cal.time))
                }
                context?.let { it1 -> DatePickerDialog(it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                addTugasFragmentViewModel.doneLoadDatePicker()
            }
        })

        addTugasFragmentViewModel.showSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val dialog = SubjectDialogFragment()
                dialog.show(parentFragmentManager, "SubjectDialogFragment")
            }
        })

        addTugasFragmentViewModel.addTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true)
                {
                    val tugasTitle = binding.editTextTugas.text.toString().trim()
                    val tugasSubjectId: Long = 0
//                    val tugasDeadline = 1605837600000L// 20 November 2020, 09:00 WIB . Convert di https://currentmillis.com/
                    val tugasDeadline: Long = convertDateAndTimeToLong(binding.editDeadline.text.toString(), binding.editJam.text.toString())
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
                    this.findNavController().navigate(AddTugasFragmentDirections.actionAddTugasFragmentToHomeFragment())
                    addTugasFragmentViewModel.doneNavigating()
                }



            })

        binding.addTugasFragmentViewModel = addTugasFragmentViewModel
//        inputTextSubject = binding.inputTextSubject
//        inputTextTugas = binding.inputTextTugas
//        inputTextDeadline = binding.inputDeadline
//        inputTextJam = binding.inputJam
//        inputTextCatatan = binding.inputCatatan
//        editTextSubject = binding.editTextSubject
//        editTextTugas = binding.editTextTugas
//        editTextDeadline = binding.editDeadline
//        editTextJam = binding.editJam
//        editTextCatatan = binding.editCatatan
//
//        buttonSave = binding.buttonSave
//        buttonAddToDoList = binding.addToDoListButton
//        buttonAddGambar = binding.addGambarButton
        binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)

//        setDeleteGambarListeners()
//        arguments?.let {
//            TugasKuliah = TugasFragmentEditorArgs.fromBundle(it).tugasKuliah
//            editTextTugas.setText(TugasKuliah?.TugasKuliahName)
//        }
//
//
//        inputTextTugas.setOnClickListener { view ->
//            updateTugasKuliahTextBox(view)
//        }


//        buttonSave.setOnClickListener {view ->
//            val tugasTitle = editTextTugas.text.toString().trim()
//            val tugasSubjectId: Int = 0
//            val tugasDeadline = Date(1605837600000)// 20 November 2020, 09:00 WIB . Convert di https://currentmillis.com/
//            val tugasToDoListId: Int = 0
//            val tugasNotes = editTextCatatan.text.toString().trim()
//            val tugasGambar = 0
//            var fromBinusmayaId: Int = -1
//
//            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//
//            if (tugasTitle.isEmpty())
//            {
//                editTextTugas.error = "tugas required"
//                editTextTugas.requestFocus()
//                return@setOnClickListener
//            }
//            launch {
//
//                context?.let {
//                    val mTugas = TugasKuliah(0L, tugasSubjectId, tugasTitle, tugasDeadline, tugasToDoListId, false,  tugasNotes, tugasGambar, fromBinusmayaId)
//
//                    if (TugasKuliah == null)
//                    {
////                        AppDatabase(it).getTugasDao().insertTugas(mTugas)
//                        it.toast("Note Saved")
//                    }
//                    else
//                    {
//                        fromBinusmayaId = TugasKuliah!!.fromBinusmayaId!!.toInt()
//                        mTugas.tugasKuliahId = TugasKuliah!!.tugasKuliahId
////                        AppDatabase(it).getTugasDao().updateTugas(mTugas)
//                        it.toast("Note Updated")
//                    }
//
//
//                    val action = TugasFragmentEditorDirections.actionSaveTugas()
//                    Navigation.findNavController(view).navigate(action)
//                }
//
//            }
//
//
//
//        }
//    }


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
}
