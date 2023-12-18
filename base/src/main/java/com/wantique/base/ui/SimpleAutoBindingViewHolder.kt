package com.wantique.base.ui

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.wantique.base.BR

class SimpleAutoBindingViewHolder(
    private val bindingVariableId: Int?,
    layoutId: Int,
    parent: ViewGroup
) : SimpleViewHolder<ViewDataBinding, SimpleModel>(layoutId, parent) {
    override fun onBid(model: SimpleModel) {
        bindingVariableId?.let {
            binding.setVariable(it, model)
        }
        binding.executePendingBindings()
    }
}