package com.project.therollamissionapp.ui.checkin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.project.therollamissionapp.AppExecutors
import com.project.therollamissionapp.R
import com.project.therollamissionapp.data.Patron
import com.project.therollamissionapp.databinding.ItemPatronBinding
import com.project.therollamissionapp.ui.common.DataBoundListAdapter

class PatronListAdapter(
    appExecutors: AppExecutors,
    private val patronClickCallback: ((Patron) -> Unit)?
) : DataBoundListAdapter<Patron, ItemPatronBinding>(
    appExecutors = appExecutors,
    diffCallback = object: DiffUtil.ItemCallback<Patron>() {
        override fun areItemsTheSame(oldItem: Patron, newItem: Patron): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Patron, newItem: Patron): Boolean {
            return oldItem.firstName == newItem.firstName
                    && oldItem.lastName == newItem.lastName
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemPatronBinding {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patron, parent, false)
        val binding = ItemPatronBinding.bind(root)
        binding.root.setOnClickListener {
            binding.patron?.let {
                patronClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemPatronBinding, item: Patron) {
        binding.patron = item
    }
}