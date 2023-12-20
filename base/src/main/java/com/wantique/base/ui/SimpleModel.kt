package com.wantique.base.ui

import androidx.annotation.LayoutRes

interface SimpleModel {
    @LayoutRes
    fun layoutId(): Int
    fun bindingVariableIds(): Map<String, Int>
}