package com.wantique.base.ui

class SimpleSubmittableState<ITEM: SimpleModel> {
    private var list: List<ITEM>? = null
    private var adapterDependency: SimpleListAdapterDependency<ITEM>? = null

    fun getCurrentList(): List<ITEM> {
        return list ?: emptyList()
    }

    fun setAdapterDependency(adapterDependency: SimpleListAdapterDependency<ITEM>?) {
        if(adapterDependency == null) {
            this.adapterDependency?.submitList(null)
        } else {
            list?.let {
                adapterDependency.submitList(it)
            }
        }
        this.adapterDependency = adapterDependency
    }

    fun submitList(list: List<ITEM>?) {
        this.list = list
        adapterDependency?.submitList(list)
    }
}