package com.example.academictaskorganizer_skripsi.view

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.components.MyItemDecoration
import com.example.academictaskorganizer_skripsi.database.*
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddTugasFragment : BaseFragment() {

    private lateinit var binding: FragmentAddTugasBinding
    private lateinit var SubjectDataSource: subjectDao
    private lateinit var addTugasFragmentViewModel: AddTugasFragmentViewModel

    private var subjectId by Delegates.notNull<Long>()
    private lateinit var selectedImageUri: Uri

    val TAG: String = this::class.java.getSimpleName()
    private val TARGET_FRAGMENT_REQUEST_CODE = 1

    private val YOUR_IMAGE_CODE = 2
    private val EXTRA_GREETING_MESSAGE: String = "message"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas, container, false)

        val dataSource = AppDatabase.getInstance(application).getTugasDao
        SubjectDataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = AddTugasFragmentViewModelFactory(application, dataSource)
        addTugasFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(AddTugasFragmentViewModel::class.java)


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
                addTugasFragmentViewModel.doneLoadSubjectDialog()
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

        addTugasFragmentViewModel.string.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextSubject.setText(it)
                addTugasFragmentViewModel.onSubjectNameChanged()
            }
        })

        addTugasFragmentViewModel.toDoListId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //ini buat nge update to do list ya kalau di click
                addTugasFragmentViewModel.afterClickToDoList()
            }
        })

        addTugasFragmentViewModel.imageId.observe(viewLifecycleOwner, Observer {
            it?.let{
                //action not defined

                addTugasFragmentViewModel.afterClickGambar()
            }
        })

        addTugasFragmentViewModel.addToDoList.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val mToDoList = ToDoList(toDoListName = "Test tambah", isFinished = false, deadline = 0L)
                addTugasFragmentViewModel.addToDoListItem(mToDoList)
                addTugasFragmentViewModel.afterAddToDoListClicked()
            }
        })

        addTugasFragmentViewModel.addImage.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select a picture"), YOUR_IMAGE_CODE)

                //problem with selectedImageURi
                val mImage = ImageForTugas(imageName = selectedImageUri.toString())
                addTugasFragmentViewModel.addImageItem(mImage)
                addTugasFragmentViewModel.afterAddToDoListClicked()
            }
        })

        val toDoListAdapter = ToDoListAdapter(ToDoListListener { toDoListId ->
            addTugasFragmentViewModel.onToDoListClicked(toDoListId)
        })
        binding.ToDoListRecyclerView.adapter = toDoListAdapter


        val gambarAdapter = ImageForTugasAdapter(ImageForTugasListener { imageId ->
            addTugasFragmentViewModel.onGambarClicked(imageId)
        })
        binding.GambarRecyclerView.adapter = gambarAdapter

        addTugasFragmentViewModel.toDoList.observe(viewLifecycleOwner, Observer {
            it?.let {
                toDoListAdapter.updateList(it)
            }
        })

        addTugasFragmentViewModel.imageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                gambarAdapter.updateList(it)
            }
        })


//        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        val manager = LinearLayoutManager(activity)
        val manager2 = LinearLayoutManager(activity)
        binding.ToDoListRecyclerView.addItemDecoration(MyItemDecoration(16))
        binding.ToDoListRecyclerView.layoutManager = manager
        binding.GambarRecyclerView.layoutManager = manager2
        binding.addTugasFragmentViewModel = addTugasFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addtugas_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                addTugasFragmentViewModel.onAddTugasKuliahClicked2()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
                addTugasFragmentViewModel.convertSubjectIdToSubjectName(greeting)
            }
        }

        if (requestCode == YOUR_IMAGE_CODE){
            if (resultCode == RESULT_OK)
                if (data != null) {
                    selectedImageUri = data.data!!
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

}
