package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemToDoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ToDoListInterface{
    fun onUpdateText(id: Long, name: String)
    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
    fun onRemoveItem(id: Long)
    fun onRemoveEmptyItem(id: Long)
    fun onEnterPressed(id: Long)
}

class ToDoListAdapter(val clickListener: ToDoListListener
, var toDoListInterface: ToDoListInterface
): ListAdapter<ToDoListDataItem, RecyclerView.ViewHolder>(ToDoListDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, toDoListInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
       when (holder)
       {
           is ViewHolder ->
           {
               val item = getItem(position) as ToDoListDataItem.ToDoListItem
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
                           toDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
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

                   }

               }

               holder.binding.textViewToDoListNameDialog.setText(item.tugasKuliahToDoList.toDoListName)
               holder.binding.toDoListItemCheckBox.isChecked = item.tugasKuliahToDoList.isFinished
               holder.binding.textViewToDoListNameDialog.addTextChangedListener(textWatcher)
               holder.binding.textViewToDoListNameDialog.onFocusChangeListener = object: View.OnFocusChangeListener {
                   override fun onFocusChange(arg0: View, hasfocus: Boolean) {
                       if (hasfocus) {
                           Log.e("TAG", "e1 focused")
                       } else {
                           if (TextUtils.isEmpty(holder.binding.textViewToDoListNameDialog.text.toString().trim())) {
                               toDoListInterface.onRemoveEmptyItem(holder.adapterPosition.toLong())
                           }
                       }
                   }
               }
               holder.binding.textViewToDoListNameDialog.setOnKeyListener(    object : View.OnKeyListener {
                   override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                       if (event.action == KeyEvent.ACTION_DOWN
                           && keyCode == KeyEvent.KEYCODE_ENTER
                       ) {
                           toDoListInterface.onEnterPressed(holder.adapterPosition.toLong())
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
                       toDoListInterface.onUpdateCheckbox(holder.adapterPosition.toLong(), isChecked)
                   }
               })

               holder.binding.toDoListDeleteBtn.setOnClickListener {
                   toDoListInterface.onRemoveItem(holder.adapterPosition.toLong())
               }
               /// TODO: 27/01/2021 ketahuan kalau text box yang focused kosong dan menekan tombol delete, maka item akan keremove 2x. Fix this bug later. 
               holder.bind(item.tugasKuliahToDoList, clickListener)
           }
       }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    fun updateList(listTugasKuliah: List<TugasKuliahToDoList>?) {
        adapterScope.launch {
            val items = listTugasKuliah?.map {
                ToDoListDataItem.ToDoListItem(it)
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    fun removeAt(listTugasKuliah: List<TugasKuliahToDoList>?, position: Int) {

        adapterScope.launch {
            val items = listTugasKuliah?.map {
                ToDoListDataItem.ToDoListItem(it)
            }
            if (items != null) {
                items.drop(position)
                notifyItemRemoved(position)
            }

        }

    }

    class ViewHolder private constructor(val binding: ListItemToDoListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TugasKuliahToDoList, clickListener: ToDoListListener)
        {
            binding.toDoList = item
            binding.toDoListClickListener = clickListener
            binding.executePendingBindings()
        }



        companion object{
            fun from(parent: ViewGroup
                     , TDLI: ToDoListInterface
            ): ViewHolder{

//                val TDLR: ToDoListInterface? = null
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemToDoListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class ToDoListDiffCallback : DiffUtil.ItemCallback<ToDoListDataItem>() {
    override fun areItemsTheSame(oldItem: ToDoListDataItem, newItem: ToDoListDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ToDoListDataItem, newItem: ToDoListDataItem): Boolean {
        return oldItem == newItem
    }

}

class ToDoListListener(val clickListener: (ToDoListId: Long) -> Unit)
{
    fun onClick(tugasKuliahToDoList: TugasKuliahToDoList) = clickListener(tugasKuliahToDoList.toDoListId)
}


sealed class ToDoListDataItem {
    abstract val id: Long
    abstract val name: String
    abstract val isFinished: Boolean
    data class ToDoListItem(val tugasKuliahToDoList: TugasKuliahToDoList): ToDoListDataItem(){
        override val id = tugasKuliahToDoList.toDoListId
        override val name = tugasKuliahToDoList.toDoListName
        override val isFinished = tugasKuliahToDoList.isFinished
    }
    //gak ada bedanya kalau name sama isfinished di comment atau gak

}