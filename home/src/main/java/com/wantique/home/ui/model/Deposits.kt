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

    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}
