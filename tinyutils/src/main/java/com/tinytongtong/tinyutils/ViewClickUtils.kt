package com.tinytongtong.tinyutils

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import com.tinytongtong.tinyutils.ScreenUtils.dip2px

/**
 * @Description: 给View的事件传递设置代理，放大View点击区域。
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:40
 * @Version
 */
object ViewClickUtils {
    fun setTouchDelegate(mContext: Context?, view: View?) {
        setTouchDelegate(mContext, view, 30f, 30f, 30f, 30f)
    }

    /**
     * 放大view的点击区域，受限于父view的范围。
     *
     * @param mContext
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    fun setTouchDelegate(mContext: Context?, view: View?,
                         left: Float, top: Float, right: Float, bottom: Float) {
        if (mContext == null || view == null) {
            return
        }
        val parent = view.parent as View
        parent?.post {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before we call getHitRect()
            val r = Rect()
            view.getHitRect(r)
            r.left -= dip2px(mContext, left)
            r.top -= dip2px(mContext, top)
            r.right += dip2px(mContext, right)
            r.bottom += dip2px(mContext, bottom)
            parent.touchDelegate = object : TouchDelegate(r, view) {
                override fun onTouchEvent(event: MotionEvent): Boolean {
                    return super.onTouchEvent(event) //正常情况下返回这个值即可。
                }
            }
        }
    }
}