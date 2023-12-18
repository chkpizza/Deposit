package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class Banner(
    val imageUrl: String,
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_banner
    override fun bindingVariableId(): Int? = BR.model
}