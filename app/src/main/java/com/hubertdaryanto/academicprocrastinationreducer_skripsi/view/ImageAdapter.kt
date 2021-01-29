package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImage
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.ImageForTugasUtils
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.databinding.ListItemImageForTugasBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


interface ImageInterface{
    fun onRemoveItem(id: Long)
//    fun onUpdateCheckbox(id: Long, isFinished: Boolean)
//        : String{
//            return data
//        }
}

class ImageForTugasAdapter(val clickListener: ImageForTugasListener, var imageInterface: ImageInterface): ListAdapter<ImageDataItem, RecyclerView.ViewHolder>(
    ImageForTugasDiffCallback()
) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private lateinit var picasso: Picasso
    private lateinit var binding: ListItemImageForTugasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder)
        {
            is ViewHolder -> {
                val item = getItem(position) as ImageDataItem.ImageForTugasItem
                picasso = Picasso.Builder(holder.binding.root.context).build()
                picasso.isLoggingEnabled = true

//                if (binding.image?.imageName != null)
//                {
//                    var string = binding.image.imageName
//                    val uri = Uri.parse(string)
//                    picasso.load(File(uri.path)).into(binding.gambarTugas)
//                }




                //binding.image null!!!
                val string = item.TugasKuliahImage.imageName
                val uri = Uri.parse(string)


//                picasso.load(File(uri.path)).resize(96* ( / 160), 96* ( / 160)).into(holder.binding.gambarTugas)
                picasso.load(File(uri.path)).resize(96* 3, 96* 3).into(holder.binding.gambarTugas)
                holder.binding.gambarTugas.setOnClickListener{
                    ImageForTugasUtils.openImageInGallery(holder.binding.root.context, uri)
                }
                holder.binding.imageDeleteBtn.setOnClickListener {
                    imageInterface.onRemoveItem(holder.adapterPosition.toLong())
                }
                holder.bind(item.TugasKuliahImage, clickListener)
            }
        }
    }

    fun updateList(list: List<TugasKuliahImage>?) {
        adapterScope.launch {
            val items = list?.map {
                ImageDataItem.ImageForTugasItem(it)
            }

            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }


    class ViewHolder private constructor(val binding: ListItemImageForTugasBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: TugasKuliahImage, clickListener: ImageForTugasListener)
        {
            binding.image = item
            binding.imageClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemImageForTugasBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ImageForTugasDiffCallback : DiffUtil.ItemCallback<ImageDataItem>() {
    override fun areItemsTheSame(oldItem: ImageDataItem, newItem: ImageDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ImageDataItem, newItem: ImageDataItem): Boolean {
        return oldItem == newItem
    }

}

class ImageForTugasListener(val clickListener: (ImageForTugasId: Long) -> Unit)
{
    fun onClick(TugasKuliahImage: TugasKuliahImage) = clickListener(TugasKuliahImage.imageId)
}


sealed class ImageDataItem {
    abstract val id: Long
    abstract val name: String
    data class ImageForTugasItem(val TugasKuliahImage: TugasKuliahImage): ImageDataItem(){
        override val id = TugasKuliahImage.imageId
        override val name = TugasKuliahImage.imageName
    }

}

