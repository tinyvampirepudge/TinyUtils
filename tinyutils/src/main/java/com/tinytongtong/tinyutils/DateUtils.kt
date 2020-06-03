package com.tinytongtong.tinyutils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description: 日期相关工具类
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:38
 * @Version
 */
object DateUtils {
    /**
     * 英文简写（默认）如：2010-12-01
     */
    var FORMAT_SHORT = "yyyy-MM-dd"

    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    var FORMAT_LONG = "yyyy-MM-dd HH:mm:ss"

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S"

    /**
     * 中文简写  如：2010年12月01日
     */
    var FORMAT_SHORT_CN = "yyyy年MM月dd"

    /**
     * 中文全称  如：2010年12月01日  23时15分06秒
     */
    var FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒"

    /**
     * 精确到毫秒的完整中文时间
     */
    var FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"

    /**
     * 获取当前时间
     *
     * @param format 时间格式   yyyy-MM-dd HH:mm:ss
     * @return 2018-01-05 23:27:51
     */
    fun getCurrDateStr(format: String?): String {
        val df = SimpleDateFormat(format)
        val calendar = Calendar.getInstance()
        return df.format(calendar.time)
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param format      格式
     * @param millSeconds 毫秒级别的时间戳
     * @return
     */
    fun millSeconds2DateStr(format: String?, millSeconds: String?): String {
        if (TextUtils.isEmpty(format) || TextUtils.isEmpty(millSeconds)) {
            return ""
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(millSeconds)))
    }
}