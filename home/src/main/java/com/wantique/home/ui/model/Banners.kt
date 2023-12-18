package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.BR
import com.wantique.home.R

data class Banners<ITEM: SimpleModel>(
    val simpleSubmittableState: SimpleSubmittableState<ITEM>,
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_banner_horizontal
    override fun bindingVariableId(): Int? = BR.model
}