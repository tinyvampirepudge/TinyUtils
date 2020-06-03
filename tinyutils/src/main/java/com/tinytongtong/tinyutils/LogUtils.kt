package com.tinytongtong.tinyutils

import android.util.Log

/**
 * @Description: 日志管理类
 * @Author wangjianzhou
 * @Date 2019-08-01 11:37
 * @Version
 */
object LogUtils {
    private const val TAG = "tiny_module"

    // 是否打印日志标志
    var isOpenLog = true
        private set

    fun openLog(flag: Boolean) {
        isOpenLog = flag
    }

    // 打印调试信息
    fun d(log: String?) {
        if (isOpenLog) {
            Log.d(TAG, log)
        }
    }

    // 打印调试信息
    fun d(tag: String?, log: String?) {
        if (isOpenLog) {
            Log.d(tag, log)
        }
    }

    // 打印调试信息
    fun i(tag: String?, log: String?) {
        var log = log
        if (isOpenLog) {
            log = log ?: ""
            Log.i(tag, log)
        }
    }

    // 打印错误信息
    fun e(log: String?) {
        if (isOpenLog) {
            Log.e(TAG, log)
        }
    }

    fun e(tag: String?, log: String?) {
        if (isOpenLog) {
            Log.e(tag, log)
        }
    }

    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (isOpenLog) {
            Log.e(tag, msg, tr)
        }
    }

    fun i(log: String?) {
        if (isOpenLog) {
            Log.i(TAG, log)
        }
    }

    fun w(log: String?) {
        if (isOpenLog) {
            Log.w(TAG, log)
        }
    }

    /**
     * 打印完整log
     *
     * @param tag
     * @param msg
     */
    fun vFullMsg(tag: String?, msg: String?) {
        if (isOpenLog) {
            printFullMsg(Log.VERBOSE, tag, msg)
        }
    }

    fun dFullMsg(tag: String?, msg: String?) {
        if (isOpenLog) {
            printFullMsg(Log.DEBUG, tag, msg)
        }
    }

    fun iFullMsg(tag: String?, msg: String?) {
        if (isOpenLog) {
            printFullMsg(Log.INFO, tag, msg)
        }
    }

    fun wFullMsg(tag: String?, msg: String?) {
        if (isOpenLog) {
            printFullMsg(Log.WARN, tag, msg)
        }
    }

    /**
     * 截断输出日志，打印完整log
     * https://blog.csdn.net/oShenLi1/article/details/73332575
     *
     * @param msg
     */
    fun eFullMsg(tag: String?, msg: String?) {
        if (isOpenLog) {
            printFullMsg(Log.ERROR, tag, msg)
        }
    }

    fun printFullMsg(logLevel: Int, tag: String?, msg: String?) {
        var msg = msg
        if (tag == null || tag.length == 0 || msg == null || msg.length == 0) {
            return
        }
        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        if (length <= segmentSize) {
            // 长度小于等于限制直接打印
            printByLevel(logLevel, tag, msg)
        } else {
            while (msg!!.length > segmentSize) {
                // 循环分段打印日志
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                printByLevel(logLevel, tag, logContent)
            }
            // 打印剩余日志
            printByLevel(logLevel, tag, msg)
        }
    }

    /**
     * 根据不同logLevel值调用不同的log方法。
     *
     * @param logLevel
     * @param tag
     * @param msg
     */
    private fun printByLevel(logLevel: Int, tag: String, msg: String?) {
        when (logLevel) {
            Log.VERBOSE -> Log.v(tag, msg)
            Log.DEBUG -> Log.d(tag, msg)
            Log.INFO -> Log.i(tag, msg)
            Log.WARN -> Log.w(tag, msg)
            Log.ERROR -> Log.e(tag, msg)
            else -> {
            }
        }
    }
}