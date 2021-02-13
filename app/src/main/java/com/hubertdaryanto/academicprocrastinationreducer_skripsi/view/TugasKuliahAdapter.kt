package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

//import androidx.recyclerview.widget.DiffUtil
//import org.stephenbrewer.arch.recyclerview.LinearLayoutManager
//import androidx.recyclerview.widget.ListAdapter
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListAgendaHeaderBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTugasBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemTugasSelesaiBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.*
import kotlinx.coroutines.*
import org.stephenbrewer.arch.recyclerview.ListAdapter
import org.stephenbrewer.arch.recyclerview.RecyclerView


class TugasKuliahAdapter(val clickListener: TugasKuliahListener, var tugasKuliahToDoListFinishedInterface: TugasKuliahToDoListFinishedInterface): ListAdapter<TugasKuliahDataItem, RecyclerView.ViewHolder>(
    TugasKuliahDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        tugasKuliahToDoListFinishedInterface.onFinished()
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
            val tugasKuliahDataSource = AppDatabase.getInstance(binding.root.context).getTugasKuliahDao
            val tugasKuliahToDoListDataSource = AppDatabase.getInstance(binding.root.context).getTugasKuliahToDoListDao
            val tugasKuliahCompletionHistoryDataSource = AppDatabase.getInstance(binding.root.context).getTugasKuliahCompletionHistoryDao

            val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
            val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>> = _toDoList



            uiScope.launch {
                _toDoList.value = tugasKuliahToDoListDataSource.loadToDoListsByTugasKuliahId(item.tugasKuliahId)
                if (_toDoList.value!!.count() == 0)
                 {
                     binding.homeToDoList.visibility = View.GONE
                     binding.textViewSubjectAndDeadline.setPadding(0,0,0,24)
                 }
            }

            val toDoListAdapter = TugasKuliahToDoListAdapter(TugasKuliahToDoListListener { toDoListId ->
                //function buat apa yang terjadi kalau di click
            }
                , object :
                    TugasKuliahToDoListInterface {
                    override fun onUpdateText(id: Long, data: String) {
                        //function buat update data

                        uiScope.launch {
                            _toDoList.value?.get(id.toInt())?.tugasKuliahToDoListName = data
                            _toDoList.value?.get(id.toInt())?.let { tugasKuliahToDoListDataSource.insertTugasKuliahToDoList(it) }
                        }
                    }

                    override fun onUpdateCheckbox(id: Long, isFinished: Boolean) {
                        //function buat update to do list langsung
                        uiScope.launch {
                            var allFinished: Boolean = true
                            _toDoList.value?.get(id.toInt())?.isFinished  = isFinished
                            _toDoList.value?.get(id.toInt())?.let { tugasKuliahToDoListDataSource.insertTugasKuliahToDoList(it) }
                            for (i in _toDoList.value!!)
                            {
                                if (i.isFinished == false)
                                {
                                    allFinished = false
                                    break
                                }
                            }
                            if (allFinished)
                            {
                                item.isFinished = true
                                Toast.makeText(binding.root.context, "Tugas kuliah " + item.tugasKuliahName + " selesai.", Toast.LENGTH_LONG).show()
                                shared_data.toDoListFinished = true
                                uiScope.launch {
                                    tugasKuliahDataSource.updateTugasKuliah(item)
                                    var mTaskCompletionHistory: TugasKuliahCompletionHistory? = tugasKuliahCompletionHistoryDataSource.getTugasKuliahCompletionHistoryByTugasKuliahId(item.tugasKuliahId)
                                    if (mTaskCompletionHistory == null) {
                                        mTaskCompletionHistory =
                                            TugasKuliahCompletionHistory(
                                                bindToTugasKuliahId = item.tugasKuliahId,
                                                activityType = "Tugas Kuliah Selesai"
                                            )
                                        tugasKuliahCompletionHistoryDataSource.insertTugasKuliahCompletionHistory(mTaskCompletionHistory)
                                    }
                                    else
                                    {
                                        mTaskCompletionHistory.activityType = "Tugas Kuliah Selesai"
                                        mTaskCompletionHistory.tugasKuliahCompletionHistoryId = System.currentTimeMillis()
                                        tugasKuliahCompletionHistoryDataSource.insertTugasKuliahCompletionHistory(mTaskCompletionHistory)

                                    }

                                }

                            }
                        }
                    }

                    override fun onRemoveItem(id: Long) {

                        if (_toDoList.value?.get(id.toInt())?.tugasKuliahToDoListName?.isEmpty()!!)
                        {
                            //remove to do list langsung
                            val realId: Long? = _toDoList.value?.get(id.toInt())?.tugasKuliahToDoListId
                            _toDoList.removeItemAt(id.toInt())
                            _toDoList.notifyObserver()
                            uiScope.launch {
                                if (realId != null) {
                                    tugasKuliahToDoListDataSource.deleteTugasKuliahToDoListById(realId.toLong())
                                }
                            }
                        }
                        else
                        {
                            AlertDialog.Builder(binding.root.context).apply {
                                setTitle(context.getString(R.string.delete_todolist_confirmation_title))
                                setMessage(context.getString(R.string.delete_todolist_confirmation_subtitle))
                                setPositiveButton(context.getString(R.string.ya)) { _, _ ->
                                    //remove to do list langsung
                                    val realId: Long? = _toDoList.value?.get(id.toInt())?.tugasKuliahToDoListId
                                    _toDoList.removeItemAt(id.toInt())
                                    _toDoList.notifyObserver()
                                    uiScope.launch {
                                        if (realId != null) {
                                            tugasKuliahToDoListDataSource.deleteTugasKuliahToDoListById(realId.toLong())
                                        }
                                    }
                                }
                                setNegativeButton(context.getString(R.string.tidak)) { _, _ ->
                                }
                            }.create().show()
                        }

                    }

                    override fun onEnterPressed(id: Long) {
                        uiScope.launch {
                            val mToDoList =
                                TugasKuliahToDoList(
                                    tugasKuliahToDoListName = "",
                                    bindToTugasKuliahId = item.tugasKuliahId,
                                    isFinished = false,
                                    deadline = 0L
                                )
                            _toDoList.addNewItem(mToDoList)
                            _toDoList.notifyObserver()
                            tugasKuliahToDoListDataSource.insertTugasKuliahToDoList(mToDoList)
                        }
                    }

                    override fun onRemoveEmptyItem(id: Long) {
                        //remove to do list langsung
                        val realId: Long? = _toDoList.value?.get(id.toInt())?.tugasKuliahToDoListId
                        _toDoList.removeItemAt(id.toInt())
                        _toDoList.notifyObserver()
                        uiScope.launch {
                            if (realId != null) {
                                tugasKuliahToDoListDataSource.deleteTugasKuliahToDoListById(realId.toLong())
                            }
                        }
                    }
                }
            )
            binding.homeToDoList.adapter = toDoListAdapter
            binding.homeToDoList.recycledViewPool.setMaxRecycledViews(1, 0)

//            toDoListAdapter.updateList(toDoList.value)
            binding.lifecycleOwner.let {
                if (it != null) {
                    tugasKuliahToDoList.observe(it, Observer {
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
            binding.homeToDoList.addItemDecoration(
                RecyclerViewItemDecoration(
                    0
                )
            )
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

