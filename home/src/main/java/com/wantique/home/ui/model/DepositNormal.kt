package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class DepositNormal(
    val bankCode: Int,
    val icon: String,
    val title: String,
    val description: String,
    val maximum: Double,
    val minimum: Double,
    val onClickListener: (DepositNormal) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_normal

    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}
