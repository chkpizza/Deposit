package com.wantique.home.ui.home.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class TopDeposit(
    val uid: String,
    val bankCode: Int,
    val title: String,
    val description: String,
    val maxRate: Double,
    val minRate: Double,
    val onClickListener: (TopDeposit) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_top_deposit
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}
