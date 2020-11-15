package com.example.academictaskorganizer_skripsi.View

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import com.example.academictaskorganizer_skripsi.Database.TugasDatabase
import com.example.academictaskorganizer_skripsi.Database.tugas
import com.example.academictaskorganizer_skripsi.R
import kotlinx.android.synthetic.main.fragment_add_tugas.*
import kotlinx.coroutines.launch

class TugasFragmentEditor : BaseFragment() {

    private var tugas: tugas? = null

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
            tugas = TugasFragmentEditorArgs.fromBundle(it).tugas
            editTextTugas.setText(tugas?.name)
        }

        button_save.setOnClickListener {view ->
            val tugasTitle = editTextTugas.text.toString().trim()
            var fromBinusmayaId: Int = -1

            if (tugasTitle.isEmpty())
            {
                editTextTugas.error = "tugas required"
                editTextTugas.requestFocus()
                return@setOnClickListener
            }
            launch {

                context?.let {
                    val mTugas = tugas(tugasTitle, fromBinusmayaId)

                    if (tugas == null)
                    {
                        TugasDatabase(it).getTugasDao().insertTugas(mTugas)
                        it.toast("Note Saved")
                    }
                    else
                    {
                        fromBinusmayaId = tugas!!.fromBinusmayaId!!.toInt()
                        mTugas.id = tugas!!.id
                        TugasDatabase(it).getTugasDao().updateTugas(mTugas)
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
                    TugasDatabase(context).getTugasDao().deleteTugas(tugas!!)
                    val action = TugasFragmentEditorDirections.actionSaveTugas()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.delete -> if(tugas != null && tugas!!.fromBinusmayaId!!.toInt() == -1) deleteTugas() else context?.toast("Cannot delete")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tugaseditor_menu, menu)
    }
}