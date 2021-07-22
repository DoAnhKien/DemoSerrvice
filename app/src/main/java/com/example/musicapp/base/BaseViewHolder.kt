package com.example.musicapp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view),
    InsetDividerDecoration.HasDivider {
    abstract fun bind(item: T)
    override fun canDivide(): Boolean = true
    open fun onDetached() = Unit
}