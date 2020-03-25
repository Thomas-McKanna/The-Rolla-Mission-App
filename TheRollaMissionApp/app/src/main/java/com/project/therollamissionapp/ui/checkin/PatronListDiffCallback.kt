package com.project.therollamissionapp.ui.checkin

import androidx.recyclerview.widget.DiffUtil
import com.project.therollamissionapp.data.Patron

class PatronListDiffCallback(
    val old: List<Patron>,
    val new: List<Patron>
): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.get(oldItemPosition).id == new.get(newItemPosition).id
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return new.get(newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.get(oldItemPosition).name == new.get(newItemPosition).name
    }
}