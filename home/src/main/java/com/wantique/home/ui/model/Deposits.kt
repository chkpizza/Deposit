package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.BR
import com.wantique.home.R

data class Deposits<ITEM: SimpleModel> (
    val title: String,
    val simpleSubmittableState: SimpleSubmittableState<ITEM>,
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_horizontal

    override fun bindingVariableId(): Int? = BR.model
}
