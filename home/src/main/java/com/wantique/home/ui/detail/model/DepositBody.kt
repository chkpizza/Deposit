package com.wantique.home.ui.detail.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class DepositBody(
    val uid: String,
    val bankCode: Int,
    val signUpMethod: String,
    val target: String,
    val contractPeriod: String,
    val signUpAmount: String,
    val protect: Boolean,
    val url: String,
    val onClickListener: (DepositBody) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_body

    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
        }
    }

}
