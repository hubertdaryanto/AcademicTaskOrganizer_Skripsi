package com.example.academictaskorganizer_skripsi.view

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academictaskorganizer_skripsi.view.EditTugasFragmentArgs
import com.example.academictaskorganizer_skripsi.view.EditTugasFragmentDirections
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.components.MyItemDecoration
import com.example.academictaskorganizer_skripsi.database.*
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.databinding.FragmentEditTugasBinding
import com.example.academictaskorganizer_skripsi.viewModel.EditTugasFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.EditTugasFragmentViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class EditTugasFragment: BaseFragment() {
    //    private var updatedToDoListIsFinished: Boolean = false
//    private var updatedToDoListName: String = ""
    private lateinit var binding: FragmentEditTugasBinding
    private lateinit var SubjectDataSource: subjectDao
    private lateinit var editTugasFragmentViewModel: EditTugasFragmentViewModel

    private var subjectId by Delegates.notNull<Long>()
    private lateinit var selectedImageUri: Uri
    private lateinit var imagePath: String

//    private lateinit var mTugas: TugasKuliah

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
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_edit_tugas, container, false)



        val dataSource = AppDatabase.getInstance(application).getTugasDao
        SubjectDataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = EditTugasFragmentViewModelFactory(application, dataSource)
        editTugasFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            EditTugasFragmentViewModel::class.java
        )

        arguments?.let {
            val tugasId = EditTugasFragmentArgs.fromBundle(it).tugasKuliahKey
            editTugasFragmentViewModel.loadTugasKuliah(tugasId)
        }

        editTugasFragmentViewModel.tugasKuliah.observe(viewLifecycleOwner, Observer {
            it?.let {
//                binding.editTextSubject.setText(it.tugasSubjectId.toString())
                editTugasFragmentViewModel.convertSubjectIdToSubjectName(it.tugasSubjectId)
                binding.editTextTugas.setText(it.tugasKuliahName)
                binding.editDeadline.setText(SimpleDateFormat("dd-MM-yyyy").format(it.deadline))
                binding.editJam.setText(SimpleDateFormat("H:mm").format(it.deadline))
                binding.SelesaiCheckBox.isChecked = it.isFinished
                binding.editCatatan.setText(it.notes)
            }
        })

        editTugasFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
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
                editTugasFragmentViewModel.doneLoadTimePicker()
            }
        })

        editTugasFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
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
                editTugasFragmentViewModel.doneLoadDatePicker()
            }
        })

        editTugasFragmentViewModel.showSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val dialog = SubjectDialogFragment()
                dialog.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
                dialog.show(parentFragmentManager, dialog.TAG)
                editTugasFragmentViewModel.doneLoadSubjectDialog()
            }
        })

        editTugasFragmentViewModel.editTugasKuliahNavigation.observe(viewLifecycleOwner,
            Observer {
                if (it == true) {
//                    mTugas.tugasKuliahName = binding.editTextTugas.text.toString().trim()
//                    mTugas.tugasSubjectId = subjectId
//                    // Convert Long to Date atau sebaliknya di https://currentmillis.com/
//                    mTugas.deadline = convertDateAndTimeToLong(
//                        binding.editDeadline.text.toString(),
//                        binding.editJam.text.toString()
//                    )
//                    mTugas.notes = binding.editCatatan.text.toString().trim()

                    val inputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                    editTugasFragmentViewModel.updateTugasKuliah(editTugasFragmentViewModel.tugasKuliah.value!!)
                    context?.toast("Tugas Updated")

                    this.findNavController()
                        .navigate(EditTugasFragmentDirections.actionSaveTugas())
                    editTugasFragmentViewModel.doneNavigating()
                }


            })

        editTugasFragmentViewModel.SubjectText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextSubject.setText(it)
                editTugasFragmentViewModel.onSubjectNameChanged()
            }
        })

        editTugasFragmentViewModel.toDoListId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //ini buat nge update to do list ya kalau di click, gakjadi kyknya wkwkwkkw
                editTugasFragmentViewModel.afterClickToDoList()
            }
        })

        editTugasFragmentViewModel.imageId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //action not defined

                editTugasFragmentViewModel.afterClickGambar()
            }
        })

        editTugasFragmentViewModel.addToDoList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val mToDoList = ToDoList(
                    toDoListName = "",
                    bindToTugasKuliahId = editTugasFragmentViewModel.tugasKuliah.value!!.tugasKuliahId,
                    isFinished = false,
                    deadline = 0L
                )
                editTugasFragmentViewModel.addToDoListItem(mToDoList)
                editTugasFragmentViewModel.afterAddToDoListClicked()
            }
        })

        editTugasFragmentViewModel.addImage.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(
                    Intent.createChooser(intent, "Select a picture"),
                    YOUR_IMAGE_CODE
                )

                //problem with selectedImageURi

            }
        })

        val toDoListAdapter = ToDoListAdapter(ToDoListListener { toDoListId ->
            editTugasFragmentViewModel.onToDoListClicked(toDoListId)
//            addTugasFragmentViewModel.updateToDoList(toDoListId, updatedToDoListName, updatedToDoListIsFinished)
            //instead di update pas click to do list nya, mending langsung update setelah salah satu parameter diedit
        }
//            , Collections.unmodifiableList(addTugasFragmentViewModel.toDoList.value)
            , object : ToDoListInterface{
                override fun onUpdateText(id: Long, data: String) {
//                updatedToDoListName = data
//                TODO("Coba implement update To Do List disini")
                    editTugasFragmentViewModel.updateToDoListName(id, data)
                }

                override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
//                    updatedToDoListIsFinished = isFinished
                    editTugasFragmentViewModel.updateToDoListIsFinished(id, isFinished)
                }

                override fun onRemoveItem(id: Long) {
                    editTugasFragmentViewModel.removeToDoListItem(id)
                }
            }
        )
        binding.ToDoListRecyclerView.adapter = toDoListAdapter


        val gambarAdapter = ImageForTugasAdapter(ImageForTugasListener { imageId ->
            editTugasFragmentViewModel.onGambarClicked(imageId)
        } , object : ImageInterface{
            override fun onRemoveItem(id: Long) {
                editTugasFragmentViewModel.removeImageItem(id)
            }

        })
        binding.GambarRecyclerView.adapter = gambarAdapter

        editTugasFragmentViewModel.toDoList.observe(viewLifecycleOwner, Observer {
            it?.let {
                toDoListAdapter.updateList(it)
            }
        })

        editTugasFragmentViewModel.imageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                gambarAdapter.updateList(it)
            }
        })

        val manager = LinearLayoutManager(activity)
//        manager.reverseLayout = true
//        manager.stackFromEnd = true
        val manager2 = LinearLayoutManager(activity)
        binding.ToDoListRecyclerView.addItemDecoration(MyItemDecoration(16))
        binding.ToDoListRecyclerView.layoutManager = manager
        binding.GambarRecyclerView.layoutManager = manager2
        binding.editTugasFragmentViewModel = editTugasFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edittugas_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                editTugasFragmentViewModel.onAddTugasKuliahClicked2()
                return true
            }
            R.id.actionDeleteTugas -> {deleteTugas()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


//    private fun updateTugasKuliahTextBox(view: View) {
//        binding.editTextTugas.visibility = View.VISIBLE
//        binding.editTextTugas.requestFocus()
//        val imm =
//            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(binding.editTextTugas, 0)
//    }


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
            val greeting = data?.getLongExtra(EXTRA_GREETING_MESSAGE, 0)
            if (greeting != null) {
                subjectId = greeting
                editTugasFragmentViewModel.convertSubjectIdToSubjectName(greeting)
            }
        }

        if (requestCode == YOUR_IMAGE_CODE){
            if (resultCode == Activity.RESULT_OK)
                if (data != null) {
                    selectedImageUri = data.data!!


//                    if (selectedImageUri.toString().contains("content:")) {
//                        imagePath  = getRealPathFromURI(selectedImageUri)
//                    } else if (selectedImageUri.toString().contains("file:")) {
//                        imagePath = selectedImageUri.getPath().toString();
//                    } else {
//                        imagePath = null.toString();
//                    }
                    val mImage = ImageForTugas(
                        bindToTugasKuliahId = editTugasFragmentViewModel.tugasKuliah.value!!.tugasKuliahId,
                        imageName = selectedImageUri.toString()
                    )
                    editTugasFragmentViewModel.addImageItem(mImage)
                    editTugasFragmentViewModel.afterAddToDoListClicked()
                }
        }
    }

    fun newIntent(message: Long?): Intent? {
        val intent = Intent()
        intent.putExtra(EXTRA_GREETING_MESSAGE, message)
        return intent
    }

    fun getInstance(): EditTugasFragment? {
        return this
    }

//    fun getRealPathFromURI(uri: Uri): String{
//        var cursor: Cursor? = null
//        return try {
//            val proj = arrayOf(MediaStore.Images.Media.DATA)
//            cursor = context?.getContentResolver()?.query(
//                selectedImageUri, proj, null, null,
//                null
//            )
//            val column_index: Int? = cursor
//                ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor?.moveToFirst()
//            if (column_index != null) {
//                cursor?.getString(column_index).toString()
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close()
//            }
//        }
//    }


    private fun deleteTugas()
    {

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
//                launch {
//                    AppDatabase().getTugasDao.deleteTugas(TugasKuliah!!)
//                    val action = TugasFragmentEditorDirections.actionSaveTugas()
//                    Navigation.findNavController(requireView()).navigate(action)
//                }
                editTugasFragmentViewModel.deleteTugasKuliah()
                val action = EditTugasFragmentDirections.actionSaveTugas()
                Navigation.findNavController(requireView()).navigate(action)
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    private fun setDeleteGambarListeners() {
//        val boxOneText = findViewById<TextView>(R.id.box_one_text)
//        val boxTwoText = findViewById<TextView>(R.id.box_two_text)
//        val boxThreeText = findViewById<TextView>(R.id.box_three_text)
//        val boxFourText = findViewById<TextView>(R.id.box_four_text)
//        val boxFiveText = findViewById<TextView>(R.id.box_five_text)
//
//        val rootConstraintLayout = findViewById<View>(R.id.constraint_layout)
//
//        val clickableViews: List<View> =
//            listOf(
//                boxOneText, boxTwoText, boxThreeText,
//                boxFourText, boxFiveText, rootConstraintLayout
//            )
//
//        for (item in clickableViews) {
//            item.setOnClickListener { deleteIndividualGambar(it) }
//        }

        //dicomment dulu, baru disesuaikan nanti

    }


//    private fun updateTugasKuliahTextBox (view: View) {
//        editTextTugas.visibility = View.VISIBLE
//        editTextTugas.requestFocus()
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(editTextTugas, 0)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId)
//        {
//            R.id.delete -> if(TugasKuliah != null && TugasKuliah!!.fromBinusmayaId!!.toInt() == -1) deleteTugas() else context?.toast("Cannot delete")
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.tugaseditor_menu, menu)
//    }
}