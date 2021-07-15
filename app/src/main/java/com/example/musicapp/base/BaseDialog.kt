package com.teamdev.myapplication

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.Exception

abstract class BaseDialog<T : ViewDataBinding?> {

    protected var layoutBinding: T? = null
    protected lateinit var viewRoot: View
    protected var dialog: Dialog? = null

    /*
    * create bắt buộc phải được gọi
    * */
    open fun create() {
        if (dialog == null) {
            dialog = onCreate()
        }
        this.initView()
    }

    /*
    * Khởi tạo dialog
    * */
    private fun onCreate(): Dialog {
        if (context() == null) {
            throw Exception("Context Null")
        }
        if (styleDialog() == null) {
            this.dialog = context()?.let { Dialog(it) }
        } else {
            this.dialog = context()?.let { Dialog(it, styleDialog()!!) }
        }

        val view = context()?.let {
            LayoutInflater.from(it)
        }?.inflate(this.initLayout(), null)

        this.layoutBinding = view?.let { DataBindingUtil.bind<T>(it) }

        this.viewRoot = this.layoutBinding!!.getRoot()

        this.dialog!!.setContentView(this.viewRoot)

        val window: Window = this.dialog!!.window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.setGravity(Gravity.BOTTOM)
        this.dialog!!.setCanceledOnTouchOutside(true)
        if (disableBack()) {
            this.dialog!!.setCancelable(false)
        }
        if (fulldialog()) {
            window.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        } else {
            window.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }
        return this.dialog!!
    }

    protected abstract fun context(): Context?

    /*
    * giao diện
    * */
    protected abstract @LayoutRes
    fun initLayout(): Int

    /*
    * Style cho dialog nếu có
    * */
    open  @StyleRes fun styleDialog(): Int? {
        return null
    }

    /*
    * dialog fullscreen
    * */
    fun fulldialog(): Boolean {
        return false
    }

    /*
    * Disable nút back vật lí
    * */
    open fun disableBack(): Boolean {
        return false
    }

    open  fun get(): Dialog? {
        return dialog
    }

    /*
    * Xử lí các view trong này yêu cầu oncreate được gọi
    * */
    open fun initView() {}

    /*
    * ShowDialog
    * */
    open fun showDialog() {
        if (dialog?.isShowing!!) {
            return
        }
        this.dialog?.show()
    }

    /*
    * dismissDialog
    * */
    open fun cancel() {
        if (!dialog?.isShowing!!) {
            return
        }
        dialog!!.cancel()
    }

}