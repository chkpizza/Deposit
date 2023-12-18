package com.wantique.base.ui

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter("simple_submittable_state")
fun <T: SimpleModel> RecyclerView.setSimpleSubmittableState(state: SimpleSubmittableState<T>?) {
    setState(state)
}

fun <T: SimpleModel> RecyclerView.setState(state: SimpleSubmittableState<T>?) {
    if(state == null) {
        adapter = null
    } else {
        val adapter = SimpleListAdapter<T>()
        state.setAdapterDependency(adapter)
        this.adapter = adapter
    }
}

@BindingAdapter("load_image")
fun ImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}