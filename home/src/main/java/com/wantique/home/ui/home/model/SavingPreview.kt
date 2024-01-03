package com.wantique.home.ui.home.model

import com.wantique.base.ui.SimpleModel
import com.wantique.home.BR
import com.wantique.home.R

data class SavingPreview(
    val uid: String,
    val bankCode: Int,
    val title: String,
    val description: String,
    val maxRate: Double,
    val minRate: Double,
    val onClickListener: (SavingPreview) -> Unit
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_home_saving_preview
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
            put("bindingAdapterPosition", BR.bindingAdapterPosition)
            put("absoluteAdapterPosition", BR.absoluteAdapterPosition)
        }
    }
}