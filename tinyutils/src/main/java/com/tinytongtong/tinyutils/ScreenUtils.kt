package com.tinytongtong.tinyutils

import android.content.Context
import java.util.*

/**
 * @Description: 屏幕工具类
 * @Author wangjianzhou
 * @Date 2019-08-01 11:39
 * @Version
 */
object ScreenUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 获取dialog宽度
     */
    fun getDialogW(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels - 100
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenW(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenH(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

    /**
     * 获取DisplayMetrics相关信息
     *
     * @param context
     * @return
     */
    fun getDisplayMetricsInfo(context: Context?): String {
        if (context == null || context.resources == null || context.resources.displayMetrics == null) {
            return "无法获取"
        }
        val sb = StringBuffer("DisplayMetrics:\n")
        val displayMetrics = context.resources.displayMetrics
        val heightPixels = displayMetrics.heightPixels.toFloat()
        val widthPixels = displayMetrics.widthPixels.toFloat()
        val density = displayMetrics.density
        val densityDpi = displayMetrics.densityDpi
        val scaledDensity = displayMetrics.scaledDensity
        val xdpi = displayMetrics.xdpi
        val ydpi = displayMetrics.ydpi
        // 屏幕尺寸，单位inch
        val screenSize = Math.sqrt(Math.pow(heightPixels.toDouble(), 2.0) + Math.pow(widthPixels.toDouble(), 2.0)) / densityDpi
        sb.append(String.format(Locale.getDefault(), "heightPixels: %.2fpx", heightPixels))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "widthPixels: %.2fpx", widthPixels))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "density: %.2f", density))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "densityDpi: %d", densityDpi))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "scaledDensity: %.2f", scaledDensity))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "screenSize(计算得出的): %.2finch", screenSize))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "xdpi: %.2f", xdpi))
        sb.append("\n")
        sb.append(String.format(Locale.getDefault(), "ydpi: %.2f", ydpi))
        sb.append("\n")
        return sb.toString()
    }

    /**
     * 根据实际dpi获取对应的dpi等级。
     * 登记表参见 https://blog.csdn.net/guolin_blog/article/details/50727753
     * @param srcDpi
     * @return
     */
    fun getTargetDpi(srcDpi: Int): Int {
        var targetDpi = 0
        if (srcDpi <= 120) { // ldpi
            targetDpi = 120
        } else if (srcDpi <= 160) { // mdpi
            targetDpi = 160
        } else if (srcDpi <= 240) { // hdpi
            targetDpi = 240
        } else if (srcDpi <= 320) { // xhdpi
            targetDpi = 320
        } else if (srcDpi <= 480) { // xxhdpi
            targetDpi = 480
        } else if (srcDpi <= 640) { // xxxhdpi
            targetDpi = 640
        }
        return targetDpi
    }
}