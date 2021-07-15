package com.teamdev.myapplication

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager


class StatusBarUtil {


    companion object {
        @JvmStatic
        fun statusTranSlucent(activity: Activity?) {
            if (activity == null) {
                return
            }
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                activity.window.statusBarColor = Color.TRANSPARENT
            }
        }

        @JvmStatic
        fun transparentFull(activity: Activity?) {
            if (activity == null) {
                return
            }
            val window: Window = activity.window

            // make full transparent statusBar

            // make full transparent statusBar
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true)
            var visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.getDecorView().setSystemUiVisibility(visibility)
            if (Build.VERSION.SDK_INT >= 21) {
                var windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                windowManager = windowManager or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                setWindowFlag(activity, windowManager, false)
                window.setStatusBarColor(Color.TRANSPARENT)
                window.setNavigationBarColor(Color.TRANSPARENT)
            }
        }

        @JvmStatic
        private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }

        @JvmStatic
        fun getStatusBarHeight(context: Context?): Int {
            context?.let {
                val resources: Resources = context.resources
                val resId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
                return if (resId > 0) {
                    resources.getDimensionPixelSize(resId)
                } else 80
            }
            return 80
        }

        @JvmStatic
        fun getNavigationBarHeight(context: Context?): Int {
            context?.let {
                val resources: Resources = context.resources
                val resId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
                val hasNavBarId: Int = resources.getIdentifier("config_showNavigationBar",
                        "bool", "android")
                return if (resId > 0 && hasNavBarId > 0 && resources.getBoolean(hasNavBarId)) {
                    resources.getDimensionPixelSize(resId)
                } else 0
            }
            return 0
        }


    }

}