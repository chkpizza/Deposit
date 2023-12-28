package com.wantique.home.ui.home.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.BR
import com.wantique.home.R

data class DepositGrid<ITEM: SimpleModel>(
    val title: String,
    val simpleSubmittableState: SimpleSubmittableState<ITEM>,
    val onClickListener: () -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_deposit_grid
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
        }
    }
}