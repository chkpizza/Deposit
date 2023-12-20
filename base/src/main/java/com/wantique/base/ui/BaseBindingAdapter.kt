package com.wantique.base.ui

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wantique.base.R
import com.wantique.resource.Constant
import com.zhpan.indicator.IndicatorView
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import java.text.DecimalFormat

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

@BindingAdapter("simple_pager_submittable_state", "pager_indicator")
fun <T: SimpleModel> ViewPager2.setPagerSubmittableState(state: SimpleSubmittableState<T>?, indicator: IndicatorView) {
    setState(state)
    setPagerIndicator(indicator)
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

fun ViewPager2.setPagerIndicator(indicator: IndicatorView) {
    adapter?.let {
        indicator.apply {
            setNormalColor(context.getColor(com.wantique.resource.R.color.disable_color))
            setCheckedColor(context.getColor(com.wantique.resource.R.color.enable_color))
            setSliderWidth(20f)
            setSliderHeight(10f)
            setSlideMode(IndicatorSlideMode.NORMAL)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setPageSize(it.itemCount)
            notifyDataChanged()
        }

        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.onPageSelected(position)
            }
        })
    }

}

/*
@BindingAdapter("pager_indicator")
fun ViewPager2.setPagerIndicator(indicator: IndicatorView) {
    indicator.apply {
        setSliderColor(context.getColor(com.wantique.resource.R.color.main_text), context.getColor(com.wantique.resource.R.color.sub_text))
        setSliderWidth(10f)
        setSliderHeight(5f)
        setSlideMode(IndicatorSlideMode.WORM)
        setIndicatorStyle(IndicatorStyle.CIRCLE)
        setPageSize(adapter?.itemCount!!)
    }

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            indicator.onPageSelected(position)
        }
    })
}

 */

@BindingAdapter("load_image")
fun ImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}

@BindingAdapter("load_bank_icon")
fun ImageView.loadBankIcon(bankCode: Int?) {
    bankCode?.let {
        val resource = when(it) {
            Constant.NH_BANK_CODE -> com.wantique.resource.R.drawable.ic_nh
            Constant.KB_BANK_CODE -> com.wantique.resource.R.drawable.ic_kb
            Constant.SHINHAN_BANK_CODE -> com.wantique.resource.R.drawable.ic_shinhan
            else -> Throwable()
        }

        Glide.with(context)
            .load(resource)
            .into(this)
    }
}

@BindingAdapter("error_handler")
fun ViewGroup.setErrorHandler(e: Throwable?) {
    e?.let {
        Snackbar.make(this, e.message.toString(), Snackbar.LENGTH_SHORT).show()
    }
}