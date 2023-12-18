package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class Deposit(
    val icon: String,
    val title: String,
    val maximum: String,
    val minimum: String
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit

    override fun bindingVariableId(): Int? = BR.model
}