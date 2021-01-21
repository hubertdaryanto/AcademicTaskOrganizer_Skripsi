package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.DiffUtil
import org.stephenbrewer.arch.recyclerview.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
//import org.stephenbrewer.arch.recyclerview.LinearLayoutManager
//import androidx.recyclerview.widget.ListAdapter
import org.stephenbrewer.arch.recyclerview.ListAdapter
import org.stephenbrewer.arch.recyclerview.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.components.MyItemDecoration
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.ToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListAgendaHeaderBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTugasBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTugasSelesaiBinding
import kotlinx.coroutines.*


class TugasKuliahDate(d: String, c: String): TugasKuliahListItemType {
    var date = d
    var count = c
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }
}



interface TugasKuliahListItemType {
    val ITEM_VIEW_TYPE_HEADER: Int
        get() = 0
    val ITEM_VIEW_TYPE_ITEM: Int
        get() = 1
    val ITEM_VIEW_TYPE_ITEM_FINISHED: Int
        get() = 2

    fun getType(): Int
}

class TugasAdapter(val clickListener: TugasKuliahListener): ListAdapter<TugasKuliahDataItem, RecyclerView.ViewHolder>(
    TugasKuliahDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> TextViewHolder.from(parent)
            1 -> ViewHolder.from(parent)
            2 -> ViewHolder2.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahList
                holder.bind(item.tugasKuliahListItemType as TugasKuliah, clickListener)
            }
            is TextViewHolder -> {
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahList
                holder.bind(item.tugasKuliahListItemType as TugasKuliahDate)
            }
            is ViewHolder2 ->
            {
                val item = getItem(position) as TugasKuliahDataItem.TugasKuliahList
                holder.bind(item.tugasKuliahListItemType as TugasKuliah, clickListener)
            }
        }
    }

    fun addHeaderAndSubmitList(list: List<TugasKuliahListItemType>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(TugasKuliahDataItem.Header)
                else -> list.map {
                    TugasKuliahDataItem.TugasKuliahList(it)
                }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }


    class TextViewHolder private constructor(val binding: ListAgendaHeaderBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: TugasKuliahDate)
        {
            binding.tugasDate = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListAgendaHeaderBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTugasBinding): RecyclerView.ViewHolder(
        binding.root
    )
    {
        fun bind(item: TugasKuliah, clickListener: TugasKuliahListener) {
            binding.tugas = item
            binding.clickListener = clickListener
            binding.lifecycleOwner = this

            var viewModelJob = Job()
            val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
//            val viewModelFactory = TugasAdapterViewModelFactory(application, dataSource)
//            val tugasAdapterViewModel = ViewModelProvider(this, viewModelFactory).get(
//                TugasAdapterViewModel::class.java
//            )
            val dataSource = AppDatabase.getInstance(binding.root.context).getAllQueryListDao

            val _toDoList = MutableLiveData<MutableList<ToDoList>>()
            val toDoList: LiveData<MutableList<ToDoList>> = _toDoList

            uiScope.launch {
                _toDoList.value = dataSource.loadToDoListsByTugasKuliahId(item.tugasKuliahId)
            }

            val toDoListAdapter = ToDoListAdapter(ToDoListListener { toDoListId ->
                //function buat apa yang terjadi kalau di click
            }
                , object : ToDoListInterface{
                    override fun onUpdateText(id: Long, data: String) {
                        //function buat update data
                    }

                    override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
                        //function buat update to do list langsung
//                        uiScope.launch {
                            _toDoList.value?.get(id.toInt())?.isFinished  = isFinished

//                        }
                    }

                    override fun onRemoveItem(id: Long) {
                        AlertDialog.Builder(binding.root.context).apply {
                            setTitle(context.getString(R.string.delete_todolist_confirmation_title))
                            setMessage(context.getString(R.string.delete_todolist_confirmation_subtitle))
                            setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                                //remove to do list langsung
                            }
                            setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
                            }
                        }.create().show()
                    }

                    override fun onEnterPressed(id: Long) {

                    }
                }
            )
            binding.homeToDoList.adapter = toDoListAdapter
            binding.homeToDoList.recycledViewPool.setMaxRecycledViews(1, 0)

//            toDoListAdapter.updateList(toDoList.value)
            binding.lifecycleOwner.let {
                if (it != null) {
                    toDoList.observe(it, Observer {
                        it?.let {
                            toDoListAdapter.updateList(it)
//                            uiScope.launch {
//                                toDoList.value!!.toList().forEach {
//                                    it.bindToTugasKuliahId = item.tugasKuliahId
//                                    dataSource.insertToDoList(it)
//                                }
//                            }
                        }
                    })

//                    _toDoList.observe(it, Observer {
//                        it?.let {
//                            uiScope.launch {
//                                _toDoList.value!!.toList().forEach {
//                                    it.bindToTugasKuliahId = item.tugasKuliahId
//                                    dataSource.insertToDoList(it)
//                                }
//                            }
//                        }
//                    })
                }
            }
            val manager = LinearLayoutManager(binding.root.context)
            binding.homeToDoList.addItemDecoration(MyItemDecoration(0))
            binding.homeToDoList.layoutManager = manager
//            val swipeHandler = object : SwipeToDeleteCallback(binding.root.context) {
//                override fun onSwiped(
//                    viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
//                    direction: Int
//                ) {
//                    val adapter = binding.homeToDoList.adapter as ToDoListAdapter
//                    adapter.removeAt(_toDoList.value , viewHolder.adapterPosition)
//                }
//            }
//
//            val itemTouchHelper = ItemTouchHelper(swipeHandler)
//            itemTouchHelper.attachToRecyclerView(binding.homeToDoList)
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTugasBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ViewHolder2 private constructor(val binding: ListItemTugasSelesaiBinding): RecyclerView.ViewHolder(
        binding.root
    )
    {
        fun bind(item: TugasKuliah, clickListener: TugasKuliahListener) {
            binding.tugas = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder2 {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTugasSelesaiBinding.inflate(layoutInflater, parent, false)
                return ViewHolder2(binding)
            }
        }
    }
}

class TugasKuliahDiffCallback : DiffUtil.ItemCallback<TugasKuliahDataItem>() {
    override fun areItemsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem == newItem
    }
}

class TugasKuliahListener(val clickListener: (TugasKuliahId: Long) -> Unit)
{
    fun onClick(tugas: TugasKuliah) = clickListener(tugas.tugasKuliahId)
}

sealed class TugasKuliahDataItem {
    abstract val id: Long
    open var count: Long = 0
    abstract val type: Int
    object Header: TugasKuliahDataItem(){
        override val id = Long.MIN_VALUE
         override val type = 0
}
    data class TugasKuliahList(val tugasKuliahListItemType: TugasKuliahListItemType): TugasKuliahDataItem()
    {
        fun getLong(): Long{
            if (tugasKuliahListItemType is TugasKuliah)
            {
                return tugasKuliahListItemType.tugasKuliahId + count
            }
            else
            {
                count = count + 1
                return count
            }
        }
        override val id: Long = getLong()
        override val type: Int = tugasKuliahListItemType.getType()
    }
}