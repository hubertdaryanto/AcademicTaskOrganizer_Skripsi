package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.get
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
import org.w3c.dom.Text

interface ToDoListInterface{
    fun onUpdateText(name: String)
//    fun onUpdateText(id: Long, name: String)
    fun onUpdateId(id: Long)
    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
//    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
//        : String{
//            return data
//        }
}

class ToDoListAdapter(val clickListener: ToDoListListener
, var toDoListInterface: ToDoListInterface
): ListAdapter<DataItem, RecyclerView.ViewHolder>(ToDoListDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

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

               val item = getItem(position) as DataItem.ToDoListItem

               holder.binding.textViewToDoListNameDialog.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                   override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                       TODO("Not yet implemented")
                       if (hasFocus)
                       {
                           toDoListInterface.onUpdateId(holder.adapterPosition.toLong())
                       }
                   }

               })
//               holder.binding.toDoListItemCheckBox.setOnClickListener {
//                   toDoListInterface.onUpdateId(holder.adapterPosition.toLong())
//               }

               holder.binding.toDoListItemCheckBox.setOnCheckedChangeListener(object :
                   CompoundButton.OnCheckedChangeListener {
                   override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                       toDoListInterface.onUpdateCheckbox(holder.adapterPosition.toLong(), isChecked)
                   }

               })
               holder.bind(item.toDoList, clickListener)
           }
       }
    }

    fun updateList(list: List<ToDoList>?) {
        adapterScope.launch {
            val items = list?.map {
                DataItem.ToDoListItem(it)
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
                        TDLI.onUpdateText(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {
//                        if (TDLI != null) {
//                            TDLI.onUpdateText(s.toString())
//                        }
                    }

                }
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemToDoListBinding.inflate(layoutInflater, parent, false)
                binding.textViewToDoListNameDialog.addTextChangedListener(textWatcher)
                return ViewHolder(binding)
            }
        }
    }
}

class ToDoListDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ToDoListListener(val clickListener: (ToDoListId: Long) -> Unit)
{
    fun onClick(toDoList: ToDoList) = clickListener(toDoList.toDoListId)
}


sealed class DataItem {
    abstract val id: Long
    data class ToDoListItem(val toDoList: ToDoList): DataItem(){
        override val id = toDoList.toDoListId
    }

    data class ImageForTugasItem(val ImageForTugas: ImageForTugas): DataItem(){
        override val id = ImageForTugas.imageId
    }

}