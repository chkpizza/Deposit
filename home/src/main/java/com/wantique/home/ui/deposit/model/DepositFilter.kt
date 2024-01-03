package com.wantique.home.ui.deposit.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.BR
import com.wantique.home.R

data class DepositFilter(
    val title: String,
    val position: Int,
    val isSelect: Boolean,
    val onClickListener: (DepositFilter) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_product_filter
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}