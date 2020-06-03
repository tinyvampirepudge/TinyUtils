package com.tinytongtong.tinyutils

import android.os.Looper
import com.tinytongtong.tinyutils.LogUtils.e

/**
 * @Description: Thread相关的工具类
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:39
 * @Version
 */
object ThreadUtils {
    /**
     * 验证是否是主线程
     */
    fun validateMainThread() {
        check(Looper.getMainLooper() == Looper.myLooper()) { "Must be called from the main thread." }
    }

    /**
     * 打印当前Thread的名称
     */
    @JvmOverloads
    fun logCurrThreadName(name: String = "") {
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            e(name + ": main Thread,name --> " + Thread.currentThread().name)
        } else {
            e(name + ": sub Thread,name --> " + Thread.currentThread().name)
        }
    }

    /**
     * 当前线程是否是主线程
     *
     * @return
     */
    val isMainThread: Boolean
        get() = Thread.currentThread() === Looper.getMainLooper().thread
}