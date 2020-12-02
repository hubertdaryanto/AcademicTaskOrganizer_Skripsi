package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.academictaskorganizer_skripsi.database.ImageForTugas
import com.example.academictaskorganizer_skripsi.database.ToDoList
import com.example.academictaskorganizer_skripsi.databinding.ListItemToDoListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ToDoListInterface{
    fun onUpdateText(id: Long, name: String)
//    fun onUpdateText(id: Long, name: String)
//    fun onUpdateId(id: Long)
    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
    fun onRemoveItem(id: Long)
//    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
//        : String{
//            return data
//        }
}

class ToDoListAdapter(val clickListener: ToDoListListener
//, var list: List<ToDoList>?
, var toDoListInterface: ToDoListInterface
): ListAdapter<ToDoListDataItem, RecyclerView.ViewHolder>(ToDoListDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

//    private var toDoListData: List<ToDoList> = ArrayList<ToDoList>()

    var textToSend = ""
//    private var mAdapterCallback: AdapterCallback()
//    private var TDLI: ToDoListInterface? = null



//    class RecyclerListViewAdapter(mContext: Context, listItem: List<ToDoList>, tdli: ToDoListInterface)
//    {
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, toDoListInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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
                       //bisa implement disini, tapi bikin kinerja device berat banget
                       toDoListInterface.onUpdateText(holder.adapterPosition.toLong(), s.toString())
                   }

                   override fun afterTextChanged(s: Editable?) {
//                        if (TDLI != null) {
//                            TDLI.onUpdateText(s.toString())
//                        }
                   }

               }
               holder.binding.textViewToDoListNameDialog.setText(item.toDoList.toDoListName)
               holder.binding.toDoListItemCheckBox.isChecked = item.toDoList.isFinished

               //status sekarang: item paling terakhir malah merujuk ke item ke 0 kalau abis tambah gak diedit lagi
               //kalau insert 1, terus insert 1 lagi abis ketik to do pertama, dipastikan gagal memenuhi kriteria

               //kalau insert lebih dari 2, dua item diatas item baru aman, gak ada perubahan

               holder.binding.textViewToDoListNameDialog.addTextChangedListener(textWatcher)
//               holder.binding.textViewToDoListNameDialog.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//                   override fun onFocusChange(v: View?, hasFocus: Boolean) {
////                       TODO("Not yet implemented")
//                       if (hasFocus)
//                       {
//                           toDoListInterface.onUpdateId(holder.adapterPosition.toLong())
//                       }
//                   }
//
//               })
//               holder.binding.toDoListItemCheckBox.setOnClickListener {
//                   toDoListInterface.onUpdateId(holder.adapterPosition.toLong())
//               }

               holder.binding.toDoListItemCheckBox.setOnCheckedChangeListener(object :
                   CompoundButton.OnCheckedChangeListener {
                   override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                       toDoListInterface.onUpdateCheckbox(holder.adapterPosition.toLong(), isChecked)
                   }

               })

               holder.binding.toDoListDeleteBtn.setOnClickListener {
                   toDoListInterface.onRemoveItem(holder.adapterPosition.toLong())
               }
               holder.bind(item.toDoList, clickListener)
           }
       }
    }

    fun updateList(list: List<ToDoList>?) {
        adapterScope.launch {
            val items = list?.map {
                ToDoListDataItem.ToDoListItem(it)
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    class ViewHolder private constructor(val binding: ListItemToDoListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ToDoList, clickListener: ToDoListListener)
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
    fun onClick(toDoList: ToDoList) = clickListener(toDoList.toDoListId)
}


sealed class ToDoListDataItem {
    abstract val id: Long
    abstract val name: String
    abstract val isFinished: Boolean
    data class ToDoListItem(val toDoList: ToDoList): ToDoListDataItem(){
        override val id = toDoList.toDoListId
        override val name = toDoList.toDoListName
        override val isFinished = toDoList.isFinished
    }
    //gak ada bedanya kalau name sama isfinished di comment atau gak

}