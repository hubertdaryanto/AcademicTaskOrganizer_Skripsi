package com.example.academictaskorganizer_skripsi.view

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
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

class ToDoListAdapter(val clickListener: ToDoListListener
, toDoListInterface: ToDoListInterface
): ListAdapter<DataItem, RecyclerView.ViewHolder>(ToDoListDiffCallback()) {


//    var TDLI: ToDoListInterface? = null
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    var textToSend = ""
//    private var mAdapterCallback: AdapterCallback()


    interface ToDoListInterface{
        fun onUpdateText(data: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when (holder)
       {
           is ViewHolder ->
           {

               val item = getItem(position) as DataItem.ToDoListItem
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
            fun from(parent: ViewGroup): ViewHolder{
                val TDLR: ToDoListInterface? = null
                val textWatcher = object : TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (TDLR != null) {
                            TDLR.onUpdateText(s.toString())
                        }
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