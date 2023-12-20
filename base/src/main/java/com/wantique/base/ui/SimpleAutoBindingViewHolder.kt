package com.wantique.base.ui

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class SimpleAutoBindingViewHolder(
    private val bindingVariableIds: Map<String, Int>,
    layoutId: Int,
    parent: ViewGroup
) : SimpleViewHolder<ViewDataBinding, SimpleModel>(layoutId, parent) {
    override fun onBid(model: SimpleModel) {
        bindingVariableIds.forEach { key, value ->
            when(key) {
                "model" -> binding.setVariable(value, model)
                "bindingAdapterPosition" -> binding.setVariable(value, bindingAdapterPosition)
                "absoluteAdapterPosition" -> binding.setVariable(value, absoluteAdapterPosition)
            }
        }

        binding.executePendingBindings()
    }
}