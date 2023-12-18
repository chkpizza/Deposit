package com.wantique.base.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SimpleListAdapter<ITEM: SimpleModel> : SimpleListAdapterDependency<ITEM>, ListAdapter<ITEM, SimpleAutoBindingViewHolder> (
    object : DiffUtil.ItemCallback<ITEM>() {
        override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
            return oldItem == newItem
        }
    }
) {
    private val layoutIdMap = hashMapOf<Int, SimpleModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleAutoBindingViewHolder {
        return createAutoBindingViewHolder(viewType, parent)
    }

    override fun onBindViewHolder(holder: SimpleAutoBindingViewHolder, position: Int) {
        holder.onBid(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return retrieveLayoutId(currentList[position])
    }

    private fun retrieveLayoutId(item: ITEM): Int {
        if(!layoutIdMap.containsKey(item.layoutId())) {
            layoutIdMap[item.layoutId()] = item
        }
        return item.layoutId()
    }

    private fun createAutoBindingViewHolder(layoutId: Int, parent: ViewGroup): SimpleAutoBindingViewHolder {
        val item = layoutIdMap[layoutId]!!
        return SimpleAutoBindingViewHolder(item.bindingVariableId(), layoutId, parent)
    }
}