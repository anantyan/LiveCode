package id.anantyan.livecode.ui.detail
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import id.anantyan.livecode.R
import id.anantyan.livecode.data.model.CastItem
import id.anantyan.livecode.databinding.ListItemCreditsBinding
import java.util.Objects
import javax.inject.Inject

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
class CasterAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val asyncDiffer = object : DiffUtil.ItemCallback<CastItem>() {
        override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
            return Objects.equals(oldItem.id, newItem.id)
        }

        override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
            return Objects.equals(oldItem, newItem)
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, asyncDiffer)

    fun submitList(model: List<CastItem>) {
        asyncListDiffer.submitList(model)
    }

    inner class CasterViewHolder(val binding: ListItemCreditsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CasterViewHolder(ListItemCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        if (holder is CasterViewHolder) {
            holder.binding.txtName.text = item.originalName
            holder.binding.txtCharacter.text = item.character
            holder.binding.imgProfilePath.load(item.profilePath) {
                crossfade(true)
                placeholder(R.drawable.image_24px)
                error(R.drawable.image_24px)
                transformations(RoundedCornersTransformation(16F))
                size(ViewSizeResolver(holder.binding.imgProfilePath))
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
}