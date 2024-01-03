package com.wantique.base.ui

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
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


@BindingAdapter("load_image")
fun ImageView.loadImage(url: String?) {
    Log.d("loadImageUrl", url.toString())
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
            Constant.HANA_BANK_CODE -> com.wantique.resource.R.drawable.ic_hana
            Constant.WOORI_BANK_CODE -> com.wantique.resource.R.drawable.ic_woori
            Constant.CU_BANK_CODE -> com.wantique.resource.R.drawable.ic_cu
            Constant.SUHYUP_BANK_CODE -> com.wantique.resource.R.drawable.ic_suhyup
            Constant.KAKAO_BANK_CODE -> com.wantique.resource.R.drawable.ic_kakao
            Constant.K_BANK_CODE -> com.wantique.resource.R.drawable.ic_k
            Constant.IBK_BANK_CODE -> com.wantique.resource.R.drawable.ic_ibk
            else -> Throwable()
        }

        Glide.with(context)
            .load(resource)
            .into(this)
    }
}

@BindingAdapter("bank_signature_color")
fun ViewGroup.setBankSignatureColor(bankCode: Int?) {
    bankCode?.let {
        val resource = when(it) {
            Constant.NH_BANK_CODE -> context.getColor(com.wantique.resource.R.color.nh_bank_signature_color)
            Constant.KB_BANK_CODE -> context.getColor(com.wantique.resource.R.color.kb_bank_signature_color)
            Constant.SHINHAN_BANK_CODE -> context.getColor(com.wantique.resource.R.color.shinhan_bank_signature_color)
            Constant.HANA_BANK_CODE -> context.getColor(com.wantique.resource.R.color.hana_bank_signature_color)
            Constant.WOORI_BANK_CODE -> context.getColor(com.wantique.resource.R.color.woori_bank_signature_color)
            Constant.CU_BANK_CODE -> context.getColor(com.wantique.resource.R.color.cu_bank_signature_color)
            Constant.SUHYUP_BANK_CODE -> context.getColor(com.wantique.resource.R.color.suhyup_bank_signature_color)
            Constant.KAKAO_BANK_CODE -> context.getColor(com.wantique.resource.R.color.kakao_bank_signature_color)
            Constant.K_BANK_CODE -> context.getColor(com.wantique.resource.R.color.k_bank_signature_color)
            Constant.IBK_BANK_CODE -> context.getColor(com.wantique.resource.R.color.ibk_bank_signature_color)
            else -> context.getColor(com.wantique.resource.R.color.default_bank_signature_color)
        }

        setBackgroundColor(resource)
    }
}

@BindingAdapter("underline_text")
fun TextView.setUnderlineText(text: String) {
    paintFlags = Paint.UNDERLINE_TEXT_FLAG
    setText(text)
}

@BindingAdapter("filter")
fun TextView.setFilter(filter: Int) {
    text = resources.getStringArray(com.wantique.resource.R.array.product_filter_title)[filter]
}

@BindingAdapter("error_handler")
fun ViewGroup.setErrorHandler(e: Throwable?) {
    e?.let {
        Snackbar.make(this, e.message.toString(), Snackbar.LENGTH_SHORT).show()
    }
}