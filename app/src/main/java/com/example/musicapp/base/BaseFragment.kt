package com.teamdev.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding?> (protected var layoutBinding: T? =null): Fragment() {

    protected lateinit var viewRoot: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.layoutBinding = DataBindingUtil.inflate<T>(inflater, this.initLayout(), container, false)
        this.viewRoot = layoutBinding!!.getRoot()

        /*
        * 1
        * */
        this.init()

        /*
        * 2
        * */
        this.initViews()

        return this.viewRoot
    }

    protected abstract fun initLayout(): Int

    protected abstract fun init()

    protected abstract fun initViews()

}