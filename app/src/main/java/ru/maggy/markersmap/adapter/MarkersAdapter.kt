package ru.maggy.markersmap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maggy.markersmap.R
import ru.maggy.markersmap.databinding.MarkerCardBinding
import ru.maggy.markersmap.dto.Marker

interface OnInterfactionListener {
    fun onDelete(marker: Marker) {}
    fun onEdit(marker: Marker) {}
}

class MarkersAdapter(
    private val onInterfactionListener: OnInterfactionListener
) : ListAdapter<Marker, MarkerViewHolder>(MarkerDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkerViewHolder {
        val binding = MarkerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerViewHolder(binding, onInterfactionListener)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = getItem(position)
        holder.bind(marker)
    }
}

class MarkerViewHolder(
    private val binding: MarkerCardBinding,
    private val onInterfactionListener: OnInterfactionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(marker: Marker) {
        binding.apply {
            markerTitle.text = marker.title

        markerTitle.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_marker)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            onInterfactionListener.onDelete(marker)
                            true
                        }
                        R.id.edit -> {
                            onInterfactionListener.onEdit(marker)
                            true
                        } else -> false
                    }
                }
            }.show()
        }
            binding.move.setOnClickListener {

            }
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<Marker>() {
    override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem == newItem
    }
}