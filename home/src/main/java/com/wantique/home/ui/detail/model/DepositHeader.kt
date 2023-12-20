package com.wantique.home.ui.detail.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class DepositHeader(
    val uid: String,
    val bankCode: Int,
    val title: String,
    val description: String,
    val maxRate: Double,
    val minRate: Double,
    val rateDescription: String,
    val taxFree: Boolean,
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_header
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
        }
    }
}
