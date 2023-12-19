package com.wantique.home.ui.home.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class DepositSmall(
    val bankCode: Int,
    val icon: String,
    val title: String,
    val description: String,
    val maximum: Double,
    val minimum: Double,
    val onClickListener: (DepositSmall) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_small

    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}