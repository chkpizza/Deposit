package com.wantique.base.ui

interface SimpleListAdapterDependency<ITEM: SimpleModel> {
    fun getCurrentList(): List<ITEM>
    fun submitList(list: List<ITEM>?)
}