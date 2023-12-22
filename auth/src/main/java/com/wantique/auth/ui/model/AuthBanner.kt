package com.wantique.auth.ui.model

import com.wantique.auth.BR
import com.wantique.auth.R
import com.wantique.base.ui.SimpleModel

data class AuthBanner(
    val uid: String,
    val url: String
) : SimpleModel {
    override fun layoutId(): Int = R.layout.view_holder_auth_banner
    override fun bindingVariableIds(): Map<String, Int> {
        return hashMapOf<String, Int>().apply {
            put("model", BR.model)
        }
    }

}