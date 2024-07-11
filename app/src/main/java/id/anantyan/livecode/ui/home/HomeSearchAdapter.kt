package id.anantyan.livecode.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import id.anantyan.livecode.R
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.databinding.ListItemBinding
import javax.inject.Inject

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
class HomeSearchAdapter @Inject constructor() : PagingDataAdapter<ResultsItem, HomeSearchAdapter.HomeViewHolder>(asyncDiffer) {

    private var _onItemClickListener: ((position: Int, item: ResultsItem) -> Unit)? = null

    companion object {
        private val asyncDiffer = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HomeViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.txtNama.text = item.title
            holder.binding.txtRate.text = item.voteAverage.toString()
            holder.binding.imgPosterPath.load(item.posterPath) {
                crossfade(true)
                placeholder(R.drawable.image_24px)
                error(R.drawable.image_24px)
                transformations(RoundedCornersTransformation(16F))
                size(ViewSizeResolver(holder.binding.imgPosterPath))
            }
            holder.binding.root.setOnClickListener {
                _onItemClickListener?.invoke(position, item)
            }
        }
    }

    fun setOnItemClickListener(listener: (position: Int, item: ResultsItem) -> Unit) {
        _onItemClickListener = listener
    }
}