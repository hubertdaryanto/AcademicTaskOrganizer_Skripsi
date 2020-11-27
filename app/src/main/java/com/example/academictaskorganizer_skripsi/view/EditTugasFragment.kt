package com.example.academictaskorganizer_skripsi.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.databinding.FragmentAddTugasBinding
import com.example.academictaskorganizer_skripsi.databinding.FragmentEditTugasBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.*

class EditTugasFragment: BaseFragment() {
    private var TugasKuliah: TugasKuliah? = null
    private lateinit var binding: FragmentEditTugasBinding
    private lateinit var inputTextSubject: TextInputLayout
    private lateinit var inputTextTugas: TextInputLayout
    private lateinit var inputTextDeadline: TextInputLayout
    private lateinit var inputTextJam: TextInputLayout
    private lateinit var inputTextCatatan: TextInputLayout
    private lateinit var editTextSubject: EditText
    private lateinit var editTextTugas: EditText
    private lateinit var editTextDeadline: EditText
    private lateinit var editTextJam: EditText
    private lateinit var editTextCatatan: EditText
    private lateinit var buttonSave: FloatingActionButton
    private lateinit var buttonAddToDoList: ImageButton
    private lateinit var buttonAddGambar: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_tugas,container,false)
        var view = binding.root
        inputTextSubject = binding.inputTextSubject
        inputTextTugas = binding.inputTextTugas
        inputTextDeadline = binding.inputDeadline
        inputTextJam = binding.inputJam
        inputTextCatatan = binding.inputCatatan
        editTextSubject = binding.editTextSubject
        editTextTugas = binding.editTextTugas
        editTextDeadline = binding.editDeadline
        editTextJam = binding.editJam
        editTextCatatan = binding.editCatatan

        buttonSave = binding.buttonUpdate
        buttonAddToDoList = binding.addToDoListButton
        buttonAddGambar = binding.addGambarButton
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
//
//
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
    }


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

    private fun deleteIndividualGambar(view: View){
        //function buat delete gambar
    }

    private fun updateTugasKuliahTextBox (view: View) {
        editTextTugas.visibility = View.VISIBLE
        editTextTugas.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextTugas, 0)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId)
//        {
//            R.id.delete -> if(TugasKuliah != null && TugasKuliah!!.fromBinusmayaId!!.toInt() == -1) deleteTugas() else context?.toast("Cannot delete")
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.tugaseditor_menu, menu)
//    }
}