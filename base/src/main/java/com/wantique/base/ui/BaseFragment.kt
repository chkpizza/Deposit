package com.wantique.base.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wantique.base.navigation.Navigator
import com.wantique.base.navigation.NavigatorProvider

open class BaseFragment<T: ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!

    protected lateinit var navigator: Navigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as NavigatorProvider).also {
            navigator = it.getNavigator()
            Log.d("navigatorTest", navigator.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}