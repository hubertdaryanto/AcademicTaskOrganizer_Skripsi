package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemImageForTugasBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemImageForTugasEmptyBinding
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImage
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImageDataItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoListDataItem
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageDiffCallback
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageInterface
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageListener
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahImageUtils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


//interface ImageForTugasKuliahInterface{
//    fun onRemoveItem(id: Long)
//}

class ImageForTugasKuliahAdapter(val clickKuliahImageListener: TugasKuliahImageListener, var tugasKuliahImageInterface: TugasKuliahImageInterface): ListAdapter<TugasKuliahImageDataItem, RecyclerView.ViewHolder>(
    TugasKuliahImageDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private lateinit var picasso: Picasso
    private lateinit var binding: ListItemImageForTugasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_EMPTY_ITEM -> ViewHolder2.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder)
        {
            is ViewHolder -> {
                val item = getItem(position) as TugasKuliahImageDataItem.TugasKuliahImageItem


                picasso = Picasso.Builder(holder.binding.root.context).build()
                picasso.isLoggingEnabled = true

//                if (binding.image?.imageName != null)
//                {
//                    var string = binding.image.imageName
//                    val uri = Uri.parse(string)
//                    picasso.load(File(uri.path)).into(binding.gambarTugas)
//                }




                //binding.image null!!!
                val string = item.TugasKuliahImage.tugasKuliahImageName
                val uri = Uri.parse(string)


//                picasso.load(File(uri.path)).resize(96* ( / 160), 96* ( / 160)).into(holder.binding.gambarTugas)
                picasso.load(File(uri.path)).resize(96* 3, 96* 3).into(holder.binding.gambarTugas)
                holder.binding.gambarTugas.setOnClickListener{
                    TugasKuliahImageUtils.openImageInGallery(holder.binding.root.context, uri)
                }
                holder.binding.imageDeleteBtn.setOnClickListener {
                    tugasKuliahImageInterface.onRemoveItem(holder.adapterPosition.toLong())
                }
                holder.bind(item.TugasKuliahImage, clickKuliahImageListener)
            }
            is ViewHolder2 ->
            {
                holder.bind(clickKuliahImageListener)
            }
        }
    }

    fun updateList(list: List<TugasKuliahImage>?) {
        adapterScope.launch {
            val items = list?.map {
                TugasKuliahImageDataItem.TugasKuliahImageItem(it)
            }?.plus(TugasKuliahImageDataItem.AddImage)

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TugasKuliahImageDataItem.TugasKuliahImageItem -> ITEM_VIEW_TYPE_ITEM
            is TugasKuliahImageDataItem.AddImage -> ITEM_VIEW_TYPE_EMPTY_ITEM
        }
    }


    class ViewHolder private constructor(val binding: ListItemImageForTugasBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: TugasKuliahImage, clickKuliahImageListener: TugasKuliahImageListener)
        {
            binding.image = item
            binding.imageClickListener = clickKuliahImageListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemImageForTugasBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ViewHolder2 private constructor(val binding: ListItemImageForTugasEmptyBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(clickKuliahImageListener: TugasKuliahImageListener)
        {
            binding.image = TugasKuliahImage(0,"")
            binding.imageClickListener = clickKuliahImageListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder2 {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemImageForTugasEmptyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder2(binding)
            }
        }
    }
}


