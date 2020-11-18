package com.example.academictaskorganizer_skripsi.View

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import com.example.academictaskorganizer_skripsi.Database.AppDatabase
import com.example.academictaskorganizer_skripsi.Database.TugasKuliah
import com.example.academictaskorganizer_skripsi.R
import kotlinx.android.synthetic.main.fragment_add_tugas.*
import kotlinx.coroutines.launch
import java.util.*

class TugasFragmentEditor : BaseFragment() {

    private var TugasKuliah: TugasKuliah? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_tugas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            TugasKuliah = TugasFragmentEditorArgs.fromBundle(it).tugasKuliah
            editTextTugas.setText(TugasKuliah?.TugasKuliahName)
        }


        inputTextTugas.setOnClickListener { view ->
            updateTugasKuliahTextBox(view)
        }


        button_save.setOnClickListener {view ->
            val tugasTitle = editTextTugas.text.toString().trim()
            val tugasSubjectId = 0
            val tugasDeadline = Date(1605837600000)// 20 November 2020, 09:00 WIB . Convert di https://currentmillis.com/
            val tugasToDoListId: Int = 0
            val tugasNotes = editCatatan.text.toString().trim()
            val tugasGambar = 0
            var fromBinusmayaId: Int = -1

            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            if (tugasTitle.isEmpty())
            {
                editTextTugas.error = "tugas required"
                editTextTugas.requestFocus()
                return@setOnClickListener
            }
            launch {

                context?.let {
                    val mTugas = TugasKuliah(tugasSubjectId, tugasTitle, tugasDeadline, tugasToDoListId, false,  tugasNotes, tugasGambar, fromBinusmayaId)

                    if (TugasKuliah == null)
                    {
                        AppDatabase(it).getTugasDao().insertTugas(mTugas)
                        it.toast("Note Saved")
                    }
                    else
                    {
                        fromBinusmayaId = TugasKuliah!!.fromBinusmayaId!!.toInt()
                        mTugas.TugasKuliahId = TugasKuliah!!.TugasKuliahId
                        AppDatabase(it).getTugasDao().updateTugas(mTugas)
                        it.toast("Note Updated")
                    }


                    val action = TugasFragmentEditorDirections.actionSaveTugas()
                    Navigation.findNavController(view).navigate(action)
                }

            }



        }
    }


    private fun deleteTugas()
    {

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    AppDatabase(context).getTugasDao().deleteTugas(TugasKuliah!!)
                    val action = TugasFragmentEditorDirections.actionSaveTugas()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    private fun updateTugasKuliahTextBox (view: View) {
        editTextTugas.visibility = View.VISIBLE
        editTextTugas.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextTugas, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.delete -> if(TugasKuliah != null && TugasKuliah!!.fromBinusmayaId!!.toInt() == -1) deleteTugas() else context?.toast("Cannot delete")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tugaseditor_menu, menu)
    }
}