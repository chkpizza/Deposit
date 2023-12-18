package com.wantique.base.ui

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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

@BindingAdapter("simple_pager_submittable_state")
fun <T: SimpleModel> ViewPager2.setPagerSubmittableState(state: SimpleSubmittableState<T>?) {
    setState(state)
}

fun <T: SimpleModel> ViewPager2.setState(state: SimpleSubmittableState<T>?) {
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

@BindingAdapter("pager_indicator")
fun ViewPager2.setPagerIndicator(indicator: TextView) {
    indicator.text = "${currentItem + 1} / ${adapter?.itemCount}"

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            indicator.text = "${position + 1} / ${adapter?.itemCount}"
        }
    })
}