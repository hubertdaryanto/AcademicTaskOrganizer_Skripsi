package com.example.academictaskorganizer_skripsi.view

import android.app.Activity
import android.app.Activity.MODE_APPEND
import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggrayudi.storage.file.filePath
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.components.MyItemDecoration
import com.example.academictaskorganizer_skripsi.database.*
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.services.AlarmReceiver
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModel
import com.example.academictaskorganizer_skripsi.viewModel.AddTugasFragmentViewModelFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddTugasFragment : BaseFragment() {

//    private var updatedToDoListIsFinished: Boolean = false
//    private var updatedToDoListName: String = ""
    private lateinit var binding: FragmentAddTugasBinding
    private lateinit var SubjectDataSource: subjectDao
    private lateinit var addTugasFragmentViewModel: AddTugasFragmentViewModel

    private var subjectId by Delegates.notNull<Long>()
    private lateinit var selectedImageUri: Uri
    private lateinit var imagePath: String

    private var mTugas = TugasKuliah(
        tugasSubjectId = 0,
        tugasKuliahName = "",
        deadline = 0L,
//                        tugasToDoListId = tugasToDoListId,
        isFinished = false,
        notes = ""
//                        tugasImageId = tugasGambar
//                        fromBinusmayaId
    )

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


        binding.editTextSubject.inputType = InputType.TYPE_NULL
        binding.editTextSubject.isFocusable = false

        binding.editDeadline.inputType = InputType.TYPE_NULL
        binding.editDeadline.isFocusable = false

        binding.editJam.inputType = InputType.TYPE_NULL
        binding.editJam.isFocusable = false

        val dataSource = AppDatabase.getInstance(application).getTugasDao
        SubjectDataSource = AppDatabase.getInstance(application).getSubjectDao
        val viewModelFactory = AddTugasFragmentViewModelFactory(application, dataSource)
        addTugasFragmentViewModel = ViewModelProvider(this, viewModelFactory).get(
            AddTugasFragmentViewModel::class.java
        )


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
                        binding.editDeadline.setText(SimpleDateFormat("dd - MM - yyyy").format(cal.time))
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
                    if (TextUtils.isEmpty(binding.editTextSubject.text))
                    {
                        binding.editTextSubject.setError("Harap Pilih Subject")
                        Toast.makeText(context,binding.editTextSubject.error, Toast.LENGTH_LONG).show()
                    }
                    else if (TextUtils.isEmpty(binding.editTextTugas.text))
                    {
                        binding.editTextTugas.setError("Harap masukkan nama tugas")
                        Toast.makeText(context,binding.editTextTugas.error, Toast.LENGTH_LONG).show()
                    }
                    else if (TextUtils.isEmpty(binding.editDeadline.text))
                    {
                        binding.editDeadline.setError("Harap masukkan tanggal deadline")
                        Toast.makeText(context,binding.editDeadline.error, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        mTugas.tugasKuliahName = binding.editTextTugas.text.toString().trim()
                        mTugas.tugasSubjectId = subjectId
                        // Convert Long to Date atau sebaliknya di https://currentmillis.com/
                        var clock = "9:00"
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


                        //notify 24 hours before deadline
                        //not yet working, gak kepanggil soalnya
//                    val receiver = AlarmReceiver(mTugas.deadline - 86400, mTugas.tugasKuliahName, mTugas.notes)
//                    val pm = context?.packageManager
//                    pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
//                    AlarmReceiver().setAlarm(application.applicationContext, mTugas.deadline, uri)

                        addTugasFragmentViewModel.addTugasKuliah(this.context!!, mTugas)
                        context?.toast("Tugas Saved")

//                        this.findNavController()
//                            .navigate(AddTugasFragmentDirections.actionAddTugasFragmentToHomeFragment())
                        this.findNavController().popBackStack()
                        addTugasFragmentViewModel.doneNavigating()
                    }


                }


            })

        addTugasFragmentViewModel.SubjectText.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextSubject.setText(it)
                addTugasFragmentViewModel.onSubjectNameChanged()
            }
        })

        addTugasFragmentViewModel.toDoListId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //ini buat nge update to do list ya kalau di click, gakjadi kyknya wkwkwkkw
                addTugasFragmentViewModel.afterClickToDoList()
            }
        })

        addTugasFragmentViewModel.imageId.observe(viewLifecycleOwner, Observer {
            it?.let {
                //action not defined

                addTugasFragmentViewModel.afterClickGambar()
            }
        })

        addTugasFragmentViewModel.addToDoList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val mToDoList = ToDoList(
                    toDoListName = "",
                    bindToTugasKuliahId = mTugas.tugasKuliahId,
                    isFinished = false,
                    deadline = 0L
                )
                addTugasFragmentViewModel.addToDoListItem(mToDoList)
                addTugasFragmentViewModel.afterAddToDoListClicked()
            }
        })

        addTugasFragmentViewModel.addImage.observe(viewLifecycleOwner, Observer {
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
            addTugasFragmentViewModel.onToDoListClicked(toDoListId)
//            addTugasFragmentViewModel.updateToDoList(toDoListId, updatedToDoListName, updatedToDoListIsFinished)
            //instead di update pas click to do list nya, mending langsung update setelah salah satu parameter diedit
        }
//            , Collections.unmodifiableList(addTugasFragmentViewModel.toDoList.value)
            , object : ToDoListInterface{
            override fun onUpdateText(id: Long, data: String) {
//                updatedToDoListName = data
//                TODO("Coba implement update To Do List disini")
                addTugasFragmentViewModel.updateToDoListName(id, data)
            }

                override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
//                    updatedToDoListIsFinished = isFinished
                    addTugasFragmentViewModel.updateToDoListIsFinished(id, isFinished)
                }

                override fun onRemoveItem(id: Long) {
                    addTugasFragmentViewModel.removeToDoListItem(id)
                }
            }
        )
        binding.ToDoListRecyclerView.adapter = toDoListAdapter


        val gambarAdapter = ImageForTugasAdapter(ImageForTugasListener { imageId ->
            addTugasFragmentViewModel.onGambarClicked(imageId)
        } , object : ImageInterface{
            override fun onRemoveItem(id: Long) {
                addTugasFragmentViewModel.removeImageItem(id)
            }

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

        val manager = LinearLayoutManager(activity)
//        manager.reverseLayout = true
//        manager.stackFromEnd = true
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
        var action_save = menu.findItem(R.id.actionSaveTugas)
        action_save.setIcon(R.drawable.ic_baseline_save_24)
        menuIconColor(action_save, Color.BLACK)

    }

    private fun menuIconColor(menuItem: MenuItem, color: Int)
    {
        var drawable = menuItem.icon
        if (drawable != null)
        {
            drawable.mutate()
            drawable.setTint(color)
        }
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
        val formatter = SimpleDateFormat("dd - MM - yyyy H:mm")
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
                addTugasFragmentViewModel.convertSubjectIdToSubjectName(greeting)
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
                    val mImage = ImageForTugas(
                        bindToTugasKuliahId = mTugas.tugasKuliahId,
                        imageName = path
                    )
                    addTugasFragmentViewModel.addImageItem(mImage)
                    addTugasFragmentViewModel.afterAddToDoListClicked()
                }
        }
    }

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

    fun getInstance(): AddTugasFragment? {
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
