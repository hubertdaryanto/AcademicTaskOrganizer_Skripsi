package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.app.Activity
import android.app.Activity.RESULT_OK
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
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.deadline_components
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.shared_data
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.FragmentAddTugasKuliahBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.AddTugasKuliahFragmentViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.AddTugasKuliahFragmentViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddTugasKuliahFragment : Fragment() {
    private lateinit var binding: FragmentAddTugasKuliahBinding
    private lateinit var subjectTugasKuliahDataSource: subjectTugasKuliahDao
    private lateinit var dataSource: allQueryDao
    private lateinit var addTugasKuliahFragmentViewModel: AddTugasKuliahFragmentViewModel

    private var subjectId by Delegates.notNull<Long>()
    private lateinit var selectedImageUri: Uri

    private var mTugas =
        TugasKuliah(
            tugasSubjectId = 0,
            tugasKuliahName = "",
            deadline = 0L,
            isFinished = false,
            notes = "",
            finishCommitment = 0,
            updatedAt = 0
        )

    val TAG: String = this::class.java.simpleName
    private val TARGET_FRAGMENT_REQUEST_CODE = 1

    private val YOUR_IMAGE_CODE = 2
    private val EXTRA_GREETING_MESSAGE: String = "message"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!binding.editTextTugas.text.isNullOrBlank() || !binding.editTextSubject.text.isNullOrBlank() || !binding.editDeadline.text.isNullOrBlank() || !binding.editJam.text.isNullOrBlank() || !binding.editCatatan.text.isNullOrBlank() || addTugasKuliahFragmentViewModel.tugasKuliahToDoList.value != null || addTugasKuliahFragmentViewModel.imageListKuliahImage.value != null)
            {
                backAndUpButtonHandlerWhenDataIsAvailable()
            }
            else
            {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
        val application = requireNotNull(this.activity).application
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas_kuliah, container, false)


        binding.editTextSubject.inputType = InputType.TYPE_NULL
        binding.editTextSubject.isFocusable = false

        binding.editDeadline.inputType = InputType.TYPE_NULL
        binding.editDeadline.isFocusable = false

        binding.editJam.inputType = InputType.TYPE_NULL
        binding.editJam.isFocusable = false

        dataSource = AppDatabase.getInstance(application).getAllQueryListDao
        subjectTugasKuliahDataSource = AppDatabase.getInstance(application).getSubjectTugasKuliahDao
        val viewModelFactory = AddTugasKuliahFragmentViewModelFactory(application, dataSource)
        addTugasKuliahFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddTugasKuliahFragmentViewModel::class.java
        )


        addTugasKuliahFragmentViewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
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
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark)
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark)
//                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.colorPrimaryDark)
                addTugasKuliahFragmentViewModel.doneLoadTimePicker()
            }
        })

        addTugasKuliahFragmentViewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 1)
                        deadline_components.TimeSelected = cal.timeInMillis
                        val cal2 = Calendar.getInstance()
                        cal2.set(Calendar.HOUR_OF_DAY, 0)
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
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark)
//                    datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark)
                }
                addTugasKuliahFragmentViewModel.doneLoadDatePicker()
            }
        })

        addTugasKuliahFragmentViewModel.showSubjectDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val dialog = ChooseSubjectTugasKuliahDialogFragment()
                dialog.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
                dialog.show(parentFragmentManager, dialog.TAG)
                addTugasKuliahFragmentViewModel.doneLoadSubjectDialog()
            }
        })

        addTugasKuliahFragmentViewModel.addTugasKuliahNavigation.observe(viewLifecycleOwner,
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
                        mTugas.tugasKuliahName = binding.editTextTugas.text.toString().trim()
                        mTugas.tugasSubjectId = subjectId
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
                        if (binding.editJam.text.toString() != "")
                        {
                            clock = binding.editJam.text.toString()
                        }

                        if (binding.editDeadline.text.toString() != "")
                        {
                            mTugas.deadline = convertDateAndTimeToLong(
                                binding.editDeadline.text.toString(),
                                clock
                            )
                        }

                        mTugas.notes = binding.editCatatan.text.toString().trim()

                        val inputMethodManager =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

                        addTugasKuliahFragmentViewModel.addTugasKuliah(requireContext(), mTugas)
//                        context?.getString(R.string.inserted_tugas_kuliah_message)?.let { it1 ->
//                            context?.toast(
//                                it1
//                            )
//                        }

                        shared_data.mTugas = mTugas

                        this.findNavController().navigate(AddTugasKuliahFragmentDirections.actionAddTugasFragmentToAddTugasCommitmentFragment())
                        addTugasKuliahFragmentViewModel.doneNavigating()
                    }


                }


            })

        addTugasKuliahFragmentViewModel.SubjectText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextSubject.setText(it)
                addTugasKuliahFragmentViewModel.onSubjectNameChanged()
            }
        })

        addTugasKuliahFragmentViewModel.toDoListId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //ini buat nge update to do list ya kalau di click, gakjadi kyknya wkwkwkkw
                addTugasKuliahFragmentViewModel.afterClickToDoList()
            }
        })

        addTugasKuliahFragmentViewModel.imageId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //action not defined

                addTugasKuliahFragmentViewModel.afterClickGambar()
            }
        })

        addTugasKuliahFragmentViewModel.addToDoList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                addToDoList()
                addTugasKuliahFragmentViewModel.afterAddToDoListClicked()
            }
        })

        addTugasKuliahFragmentViewModel.addImage.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(
                    Intent.createChooser(intent, "Pilih Gambar"),
                    YOUR_IMAGE_CODE
                )
                addTugasKuliahFragmentViewModel.afterAddImageClicked()
                //problem with selectedImageURi

            }
        })

        val toDoListAdapter = ToDoListAdapter(ToDoListListener { toDoListId ->
            addTugasKuliahFragmentViewModel.onToDoListClicked(toDoListId)
        }
            , object : ToDoListInterface{
            override fun onUpdateText(id: Long, data: String) {
                addTugasKuliahFragmentViewModel.updateToDoListName(id, data)
            }

                override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
                    addTugasKuliahFragmentViewModel.updateToDoListIsFinished(id, isFinished)
                }

                override fun onRemoveItem(id: Long) {
                    if (addTugasKuliahFragmentViewModel._toDoList.value?.get(id.toInt())?.toDoListName?.isEmpty()!!)
                    {
                        addTugasKuliahFragmentViewModel.removeToDoListItem(id)
                    }
                    else
                    {
                        AlertDialog.Builder(context).apply {
                            setTitle(context.getString(R.string.delete_todolist_confirmation_title))
                            setMessage(context.getString(R.string.delete_todolist_confirmation_subtitle))
                            setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                                addTugasKuliahFragmentViewModel.removeToDoListItem(id)
                            }
                            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
                            }
                        }.create().show()
                    }

                }

                override fun onEnterPressed(id: Long) {
                    addToDoList()
                }

                override fun onRemoveEmptyItem(id: Long) {
                    addTugasKuliahFragmentViewModel.removeToDoListItem(id)
                }
            }
        )
        binding.ToDoListRecyclerView.adapter = toDoListAdapter


        val gambarAdapter = ImageForTugasAdapter(ImageForTugasListener { imageId ->
            addTugasKuliahFragmentViewModel.onGambarClicked(imageId)
        } , object : ImageInterface{
            override fun onRemoveItem(id: Long) {

                AlertDialog.Builder(context).apply {
                    setTitle(context.getString(R.string.delete_image_confirmation_title))
                    setMessage(context.getString(R.string.delete_image_confirmation_subtitle))
                    setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                        addTugasKuliahFragmentViewModel.removeImageItem(id)
                    }
                    setNegativeButton(context.getString(R.string.tidak)) { _, _ ->

                    }
                }.create().show()

            }

        })
        binding.GambarRecyclerView.adapter = gambarAdapter

        addTugasKuliahFragmentViewModel.tugasKuliahToDoList.observe(viewLifecycleOwner, Observer {
            it?.let {
                toDoListAdapter.updateList(it)
            }
        })

        addTugasKuliahFragmentViewModel.imageListKuliahImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                gambarAdapter.updateList(it)
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
//        binding.ToDoListRecyclerView.isNestedScrollingEnabled = false
        binding.addTugasKuliahFragmentViewModel = addTugasKuliahFragmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun addToDoList() {
        val mToDoList =
            TugasKuliahToDoList(
                toDoListName = "",
                bindToTugasKuliahId = mTugas.tugasKuliahId,
                isFinished = false,
                deadline = 0L
            )
        addTugasKuliahFragmentViewModel.addToDoListItem(mToDoList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addtugas_menu, menu)
        var action_save = menu.findItem(R.id.actionSaveTugas)
        action_save.title = "Selanjutnya"
        action_save.setIcon(R.drawable.ic_arrow_forward_black_24dp)
        view_utilities.menuIconColor(action_save, Color.BLACK)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.actionSaveTugas -> {
                addTugasKuliahFragmentViewModel.onAddTugasKuliahClicked2()
                return true
            }
            android.R.id.home -> {
                if (!binding.editTextTugas.text.isNullOrBlank() || !binding.editTextSubject.text.isNullOrBlank() || !binding.editDeadline.text.isNullOrBlank() || !binding.editJam.text.isNullOrBlank() || !binding.editCatatan.text.isNullOrBlank() || addTugasKuliahFragmentViewModel.tugasKuliahToDoList.value != null || addTugasKuliahFragmentViewModel.imageListKuliahImage.value != null)
                {
                    backAndUpButtonHandlerWhenDataIsAvailable()
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

    private fun backAndUpButtonHandlerWhenDataIsAvailable() {
        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.back_confirmation_title))
            setMessage(context.getString(R.string.back_confirmation_subtitle))
            setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                this@AddTugasKuliahFragment.findNavController().popBackStack()
            }
            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
            }
        }.create().show()
    }


    private fun updateTugasKuliahTextBox(view: View) {
        binding.editTextTugas.visibility = View.VISIBLE
        binding.editTextTugas.requestFocus()
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextTugas, 0)
    }


    private fun convertDateAndTimeToLong(date: String, time: String): Long {
        val formatter = SimpleDateFormat("dd - MM - yyyy H:mm")
        val date = formatter.parse(date + " " + time)
        return date.time
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            binding.editTextSubject.setText(shared_data.mSubjectAtAddTugasFragment)
            val greeting = data?.getLongExtra(EXTRA_GREETING_MESSAGE, 0)
            val compare: Long = 0
            if (greeting != null) {
                if (!greeting.equals(compare)) {
                    subjectId = greeting
                    addTugasKuliahFragmentViewModel.convertSubjectIdToSubjectName(greeting)
                }
            }
        }

        if (requestCode == YOUR_IMAGE_CODE){
            if (resultCode == RESULT_OK)
                if (data != null) {

                    selectedImageUri = data.data!!
//                    if (selectedImageUri == null)
//                    context?.contentResolver?.takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    val path: String = getRealPathFromURI(this.requireContext(), selectedImageUri)
                    val file: String = getFileName(this.requireContext(), selectedImageUri)


//                    try {
//                        insertInPrivateStorage(this.requireContext(), file, path)
//                    } catch (e: FileNotFoundException){
//                        e.printStackTrace()
//                    }
//                    catch (e: IOException)
//                    {
//                        e.printStackTrace()
//                    } uncomment this one it's ready for save to app-specific file

//                    if (selectedImageUri.toString().contains("content:")) {
//                        imagePath  = getRealPathFromURI(selectedImageUri)
//                    } else if (selectedImageUri.toString().contains("file:")) {
//                        imagePath = selectedImageUri.getPath().toString();
//                    } else {
//                        imagePath = null.toString();
//                    }
                    val mImage =
                        TugasKuliahImage(
                            bindToTugasKuliahId = mTugas.tugasKuliahId,
                            imageName = path
                        )
                    addTugasKuliahFragmentViewModel.addImageItem(mImage)
                    addTugasKuliahFragmentViewModel.afterAddToDoListClicked()
                }
        }
    }

//    override fun onFocus() {
//        binding.editTextSubject.setText(shared_data.mSubjectAtAddTugasFragment)
//        super.onResume()
//    }



    private fun insertInPrivateStorage(context: Context, name: String, path: String)
    {
        var fos: FileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE)

        var file: File = File(context.filesDir, name)

        var bytes: ByteArray = getBytesFromFile(file)

        fos.write(bytes)
        fos.close()

        Toast.makeText(context, "File saved in :"+ context.filesDir + "/"+name, Toast.LENGTH_LONG).show()
    }

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
//        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var selection: String
//        var cursor: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
//        if (cursor != null)
//        {
//            var column_index: Int = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor?.moveToFirst()
//            return cursor.getString(column_index)
//        }
//        return ""

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

    fun getInstance(): AddTugasKuliahFragment? {
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

}
