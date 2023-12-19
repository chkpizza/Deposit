package com.wantique.home.ui.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class Banner(
    val imageUrl: String,
    val onClickListener: (Banner) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_banner

    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}