package com.wantique.home.domain.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.BR
import com.wantique.home.R

data class HorizontalDeposit<ITEM: SimpleModel> (
    val simpleSubmittableState: SimpleSubmittableState<ITEM>,
    val title: String,
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_horizontal

    override fun bindingVariableId(): Int? = BR.model
}
