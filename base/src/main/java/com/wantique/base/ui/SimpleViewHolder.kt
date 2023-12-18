package com.wantique.base.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleViewHolder<T: ViewDataBinding, ITEM: SimpleModel>(
    private val layoutId: Int,
    private val parent: ViewGroup
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {
    protected val binding: T = DataBindingUtil.bind(itemView)!!

    abstract fun onBid(model: ITEM)
}