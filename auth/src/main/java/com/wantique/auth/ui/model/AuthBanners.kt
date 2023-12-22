package com.wantique.auth.ui.model

import com.wantique.auth.BR
import com.wantique.auth.R
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState

data class AuthBanners<ITEM: SimpleModel>(
    val simpleSubmittableState: SimpleSubmittableState<ITEM>
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_auth_banner_horizontal
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
        }
    }
}
