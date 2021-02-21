package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemToDoListBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoListDataItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListDiffCallback
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahToDoListListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TugasKuliahToDoListAdapter(val clickListenerTugasKuliahToDoList: TugasKuliahToDoListListener, var tugasKuliahToDoListInterface: TugasKuliahToDoListInterface
): ListAdapter<TugasKuliahToDoListDataItem, RecyclerView.ViewHolder>(TugasKuliahToDoListDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, tugasKuliahToDoListInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(true)
       when (holder)
       {
           is ViewHolder ->
           {

               val item = getItem(position) as TugasKuliahToDoListDataItem.TugasKuliahToDoListItem
               val textWatcher = object : TextWatcher{
                   override fun beforeTextChanged(
                       s: CharSequence?,
                       start: Int,
                       count: Int,
                       after: Int
                   ) {

                   }
                   override fun onTextChanged(
                       s: CharSequence?,
                       start: Int,
                       before: Int,
                       count: Int
                   ) {
                       if (s != null) {
//                           if (s.length>0 && s.subSequence(s.length-1, s.length).toString().equals("\n", true)) {
//                               toDoListInterface.onEnterPressed(holder.adapterPosition.toLong())
//                           }
//                           else
//                           {
//                               toDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
//                           }
                           tugasKuliahToDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
//                           object : View.OnKeyListener {
//                               override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
//                                   if (event.getAction() == KeyEvent.ACTION_DOWN
//                                       && keyCode == KeyEvent.KEYCODE_ENTER
//                                   ) {
//                                       toDoListInterface.onEnterPressed(holder.adapterPosition.toLong())
//                                   }
//                                   else
//                                   {
//                                       toDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
//                                   }
//                                   return false // very important
//                               }
//                           }


                       }
                   }

                   override fun afterTextChanged(s: Editable?) {
//                       if (TextUtils.isEmpty(s.toString().trim())) {
//                            toDoListInterface.onRemoveItem(holder.adapterPosition.toLong())
//                       }
                       if (!TextUtils.isEmpty(holder.binding.textViewToDoListNameDialog.text.toString().trim())) {
                           holder.binding.toDoListDeleteBtn.visibility = View.VISIBLE
                       }
                       else
                       {
                           holder.binding.toDoListDeleteBtn.visibility = View.INVISIBLE
                       }

                   }

               }

               holder.binding.textViewToDoListNameDialog.setText(item.tugasKuliahToDoList.tugasKuliahToDoListName)
               holder.binding.toDoListItemCheckBox.isChecked = item.tugasKuliahToDoList.isFinished
               holder.binding.textViewToDoListNameDialog.addTextChangedListener(textWatcher)
               if (!TextUtils.isEmpty(holder.binding.textViewToDoListNameDialog.text.toString().trim())) {
                   holder.binding.toDoListDeleteBtn.visibility = View.VISIBLE
               }
               else
               {
                   holder.binding.toDoListDeleteBtn.visibility = View.INVISIBLE
               }
               holder.binding.textViewToDoListNameDialog.onFocusChangeListener = object: View.OnFocusChangeListener {
                   override fun onFocusChange(arg0: View, hasfocus: Boolean) {
                       if (hasfocus) {
                           Log.e("TAG", "e1 focused")
                       } else {
                           if (TextUtils.isEmpty(holder.binding.textViewToDoListNameDialog.text.toString().trim())) {
                               tugasKuliahToDoListInterface.onRemoveEmptyItem(holder.adapterPosition.toLong())
                           }
                       }
                   }
               }
               holder.binding.textViewToDoListNameDialog.setOnKeyListener(    object : View.OnKeyListener {
                   override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                       if (event.action == KeyEvent.ACTION_DOWN
                           && keyCode == KeyEvent.KEYCODE_ENTER
                       ) {
                           tugasKuliahToDoListInterface.onEnterPressed(holder.adapterPosition.toLong())
                           holder.binding.textViewToDoListNameDialog.requestFocus()
                       }
//                       else
//                       {
//                           toDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
//                       }



                       return false // very important
                   }
               })
               holder.binding.toDoListItemCheckBox.setOnCheckedChangeListener(object :
                   CompoundButton.OnCheckedChangeListener {
                   override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                       tugasKuliahToDoListInterface.onUpdateCheckbox(holder.adapterPosition.toLong(), isChecked)
                   }
               })

               holder.binding.toDoListDeleteBtn.setOnClickListener {
                   if (!TextUtils.isEmpty(holder.binding.textViewToDoListNameDialog.text.toString().trim())) {
                       tugasKuliahToDoListInterface.onRemoveItem(holder.adapterPosition.toLong())
                   }

               }

               holder.bind(item.tugasKuliahToDoList, clickListenerTugasKuliahToDoList)
           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    fun updateList(listTugasKuliah: List<TugasKuliahToDoList>?) {
        adapterScope.launch {
            val items = listTugasKuliah?.map {
                TugasKuliahToDoListDataItem.TugasKuliahToDoListItem(it)
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    fun removeAt(listTugasKuliah: List<TugasKuliahToDoList>?, position: Int) {

        adapterScope.launch {
            val items = listTugasKuliah?.map {
                TugasKuliahToDoListDataItem.TugasKuliahToDoListItem(it)
            }
            if (items != null) {
                items.drop(position)
                notifyItemRemoved(position)
            }

        }

    }

    class ViewHolder private constructor(val binding: ListItemToDoListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TugasKuliahToDoList, clickListenerTugasKuliah: TugasKuliahToDoListListener)
        {
            binding.toDoList = item
            binding.toDoListClickListener = clickListenerTugasKuliah
            binding.executePendingBindings()
        }



        companion object{
            fun from(parent: ViewGroup
                     , TDLI: TugasKuliahToDoListInterface
            ): ViewHolder {

//                val TDLR: ToDoListInterface? = null
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemToDoListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}


