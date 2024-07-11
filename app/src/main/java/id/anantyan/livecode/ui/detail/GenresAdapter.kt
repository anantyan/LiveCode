package id.anantyan.livecode.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import id.anantyan.livecode.data.model.GenresItem
import id.anantyan.livecode.databinding.ListItemGenresBinding
import java.util.Objects
import javax.inject.Inject

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
class GenresAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val asyncDiffer = object : DiffUtil.ItemCallback<GenresItem>() {
        override fun areItemsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean {
            return Objects.equals(oldItem.id, newItem.id)
        }

        override fun areContentsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean {
            return Objects.equals(oldItem, newItem)
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, asyncDiffer)

    fun submitList(model: List<GenresItem>) {
        asyncListDiffer.submitList(model)
    }

    inner class GenresViewHolder(val binding: ListItemGenresBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenresViewHolder(
            ListItemGenresBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        if (holder is GenresViewHolder) {
            holder.binding.txtGenres.text = item.name
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
}