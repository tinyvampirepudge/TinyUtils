package com.tinytongtong.tinyutils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import java.io.*
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * @Description: 字符串操作工具包
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:38
 * @Version
 */
object CommonUtils {
    private val emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")

    //国内手机号：0?(13|14|15|18)[0-9]{9}
    private val phone = Pattern
            .compile("^((14[0-9])|(13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$")
    const val MOBILE = 0
    const val EMAIL = 1
    const val ID_CARD = 2
    const val NAME = 3
    const val ADDR = 4
    private val dateFormater: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        }
    }
    private val dateFormater2: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        }
    }

    /**
     * 指定时间之前的所有日期
     * @param beginDate
     * @return
     */
    fun getDatesBetweenTwoDate(beginDate: Date): List<Long> {
        val endDate = Date()
        val lDate: MutableList<Date> = ArrayList()
        val returnlist: MutableList<Long> = ArrayList()
        lDate.add(beginDate) // 把开始时间加入集合
        val cal = Calendar.getInstance()
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.time = beginDate
        val bContinue = true
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1)
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.time)) {
                lDate.add(cal.time)
            } else {
                break
            }
        }
        for (i in lDate.indices) {
            returnlist.add(lDate[i].time / 1000L)
        }
        return returnlist
    }

    /*
       * 检查是否存在sd卡
       */
    fun hasSdcard(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    val currentTime: String
        get() = System.currentTimeMillis().toString() + ""

    val longCurrentTime: Long
        get() = System.currentTimeMillis()

    /**
     * 获取当前时间的Unix时间戳
     *
     * @return
     */
    val currentTimeMillis: String
        get() = (System.currentTimeMillis() / 1000L).toString()

    /**
     * 根据时间戳转换日期
     *
     * @param mTimeStamp 时间戳
     * @return
     */
    fun getLongTimeStamp(mTimeStamp: Long): String {
        return getDate(mTimeStamp.toString() + "", "MM-dd HH:mm")
    }

    fun getLongTimeStamp2(mTimeStamp: Long): String {
        return getDate(mTimeStamp.toString() + "", "MM月dd日 HH:mm")
    }

    fun getLongTimeStamp3(mTimeStamp: Long): String {
        return getDate(mTimeStamp.toString() + "", "MM-dd HH:mm:ss")
    }

    /**
     * 根据时间戳转换日期
     *
     * @param mTimeStamp 十位时间戳
     * @return
     */
    fun getLongTimeDate(mTimeStamp: Long): String {
        return getDate(mTimeStamp.toString() + "", "yyyy-MM-dd")
    }

    /**
     * 返回当前系统时间
     */
    fun getDataTime(format: String?): String {
        val df = SimpleDateFormat(format, Locale.CHINA)
        return df.format(Date())
    }

    /**
     * 时间戳获取日期格式
     *
     * @param mTimeStamp 时间戳(以秒为单位的十位字符串)
     * @param pattern    格式化条件
     * @return
     */
    fun getDate(mTimeStamp: String, pattern: String?): String {
        var strTimeDesc: String
        var format: SimpleDateFormat? = null
        //        int iTimeStamp = Integer.parseInt(mTimeStamp);
        val changeTime = mTimeStamp.toLong()
        val messageTimeStamp = changeTime * 1000
        try {
            format = SimpleDateFormat(pattern)
            strTimeDesc = format.format(messageTimeStamp)
        } catch (exception: IllegalArgumentException) {
            strTimeDesc = "格式有误"
        }
        return strTimeDesc
    }

    /**
     * 时间戳获取上下午时段
     *
     * @param mTimeStamp 时间戳( 以秒为单位的十位字符串)
     * @return
     */
    fun getCourseNoon(mTimeStamp: String): String {
        var strTimeDesc = getDate(mTimeStamp, "HH:mm")
        strTimeDesc = if (strTimeDesc.substring(0, 2).toInt() < 12) {
            "上午"
        } else {
            "下午"
        }
        return strTimeDesc
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param mTime 十位数字的字符串型时间戳
     * @return boolean
     */
    fun isToday(mTime: String): Boolean {
        var b = false
        val nowDate = getDate(currentTime.substring(0, 10), "yyyy-MM-dd")
        val timeDate = getDate(mTime, "yyyy-MM-dd")
        if (nowDate == timeDate) {
            b = true
        }
        return b
    }

    //导致TextView异常换行的原因：安卓默认半角字符不能为第一行以后每行的开头字符，因为数字、字母为半角字符
    //所以我们只需要将半角字符转换为全角字符即可，方法如下
    fun ToSBC(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i] == ' ') {
                c[i] = '\u3000'
            } else if (c[i] < '\u007f') {
                c[i] = (c[i] + 65248)
            }
        }
        return String(c)
    }

    /**
     * 返回当前系统时间
     */
    val dataTime: String
        get() = getDataTime("HH:mm")

    /**
     * 毫秒值转换为mm:ss
     *
     * @param ms
     * @author kymjs
     */
    fun timeFormat(ms: Int): String {
        var ms = ms
        val time = StringBuilder()
        time.delete(0, time.length)
        ms /= 1000
        val s = ms % 60
        val min = ms / 60
        if (min < 10) {
            time.append(0)
        }
        time.append(min).append(":")
        if (s < 10) {
            time.append(0)
        }
        time.append(s)
        return time.toString()
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    fun toDate(sdate: String?): Date? {
        return try {
            dateFormater.get().parse(sdate)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    fun isEmpty(input: String?): Boolean {
        if (input == null || "" == input) return true
        for (i in 0 until input.length) {
            val c = input[i]
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false
            }
        }
        return true
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    fun isEmail(email: String?): Boolean {
        return if (email == null || email.trim { it <= ' ' }.length == 0) false else emailer.matcher(email).matches()
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    fun isPhone(phoneNum: String?): Boolean {
        return if (phoneNum == null || phoneNum.trim { it <= ' ' }.length == 0) false else phone.matcher(phoneNum).matches()
    }

    /**
     * 检查字符串是否匹配对应规则
     *
     * @param str      待匹配的字符串
     * @param typeCode 规则类型
     * @return boolean
     * @author cxy
     */
    fun isMatch(str: String?, typeCode: Int): Boolean {
        var isMatch = false
        when (typeCode) {
            MOBILE -> isMatch = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(str).matches()
            EMAIL -> isMatch = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(str).matches()
            ID_CARD -> isMatch = (Pattern.compile("^(\\d{6})(((1[8,9])|(20))\\d{2})(\\d{2})(\\d{2})(\\d{3})([0-9]|x|X)$").matcher(str).matches()
                    || Pattern.compile("^(\\d{6})(\\d{2})((0[1,2,3,4,5,7,8,9])|(1[0,1,2]))(([0][1,2,3,4,5,7,8,9])|([1,2]/d)|(3[0,1]))(\\d{3})$").matcher(str).matches())
            NAME -> isMatch = Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z]+$").matcher(str).matches()
            ADDR -> isMatch = Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z0-9\\-]+$").matcher(str).matches()
            else -> {
            }
        }
        return isMatch
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    fun toInt(str: String, defValue: Int): Int {
        try {
            return str.toInt()
        } catch (e: Exception) {
        }
        return defValue
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toInt(obj: Any?): Int {
        return if (obj == null) 0 else toInt(obj.toString(), 0)
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toLong(obj: String): Long {
        try {
            return obj.toLong()
        } catch (e: Exception) {
        }
        return 0
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toDouble(obj: String): Double {
        try {
            return obj.toDouble()
        } catch (e: Exception) {
        }
        return 0.0
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    fun toBool(b: String?): Boolean {
        try {
            return java.lang.Boolean.parseBoolean(b)
        } catch (e: Exception) {
        }
        return false
    }

    /**
     * 判断一个字符串是不是数字
     */
    fun isNumber(str: String): Boolean {
        try {
            str.toInt()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * 获取AppKey
     */
    fun getMetaValue(context: Context?, metaKey: String?): String? {
        var metaData: Bundle? = null
        var apiKey: String? = null
        if (context == null || metaKey == null) {
            return null
        }
        try {
            val ai = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA)
            if (null != ai) {
                metaData = ai.metaData
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey)
            }
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return apiKey
    }

    /**
     * 获取手机IMEI码
     */
    fun getPhoneIMEI(aty: Activity): String {
        val tm = aty
                .getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }

    /**
     * MD5加密
     */
    fun md5(string: String): String {
        val hash: ByteArray
        hash = try {
            MessageDigest.getInstance("MD5").digest(
                    string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("Huh, UTF-8 should be supported?", e)
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b and 0xFF.toByte() < 0x10) hex.append("0")
            hex.append(b and 0xFF.toByte())
        }
        return hex.toString()
    }

    /**
     * KJ加密
     */
    fun KJencrypt(str: String): String {
        val cstr = str.toCharArray()
        val hex = StringBuilder()
        for (c in cstr) {
            hex.append((c.toInt() + 5).toChar())
        }
        return hex.toString()
    }

    /**
     * KJ解密
     */
    fun KJdecipher(str: String): String {
        val cstr = str.toCharArray()
        val hex = StringBuilder()
        for (c in cstr) {
            hex.append((c.toInt() - 5).toChar())
        }
        return hex.toString()
    }

    /**
     * 对网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    fun isOpenNetwork(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (connManager.activeNetworkInfo != null) {
            connManager.activeNetworkInfo.isAvailable
        } else false
    }//length表示生成字符串的长度

    /**
     * 随机生成长度为8-16个字符
     *
     * @return
     */
    val randomString: String
        get() { //length表示生成字符串的长度
            val base = "abcdefghijklmnopqrstuvwxyz0123456789"
            val random = Random()
            val length = random.nextInt(8) + 8
            val sb = StringBuffer()
            for (i in 0 until length) {
                val number = random.nextInt(base.length)
                sb.append(base[number])
            }
            return sb.toString()
        }

    /**
     * 移除SharedPreference
     *
     * @param context
     * @param key
     */
    fun RemoveValue(context: Context, key: String) {
        val editor = getSharedPreference(context).edit()
        editor.remove(key)
        val result = editor.commit()
        if (!result) {
            Log.e("移除Shared", "save $key failed")
        }
    }

    private fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * 获取SharedPreference 值
     *
     * @param context
     * @param key
     * @return
     */
    fun getValue(context: Context, key: String?): String {
        return getSharedPreference(context).getString(key, "")
    }

    fun getBooleanValue(context: Context, key: String?): Boolean {
        return getSharedPreference(context).getBoolean(key, false)
    }

    fun putBooleanValue(context: Context, key: String?,
                        bl: Boolean) {
        val edit = getSharedPreference(context).edit()
        edit.putBoolean(key, bl)
        edit.commit()
    }

    fun getIntValue(context: Context, key: String?): Int {
        return getSharedPreference(context).getInt(key, 0)
    }

    fun getLongValue(context: Context, key: String?,
                     default_data: Long): Long {
        return getSharedPreference(context).getLong(key, default_data)
    }

    fun putLongValue(context: Context, key: String?,
                     value: Long?): Boolean {
        val editor = getSharedPreference(context).edit()
        editor.putLong(key, value!!)
        return editor.commit()
    }

    fun hasValue(context: Context, key: String?): Boolean {
        return getSharedPreference(context).contains(key)
    }

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    fun putValue(context: Context, key: String?,
                 value: String?): Boolean {
        var value = value
        value = value ?: ""
        val editor = getSharedPreference(context).edit()
        editor.putString(key, value)
        return editor.commit()
    }

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    fun putIntValue(context: Context, key: String?,
                    value: Int): Boolean {
        val editor = getSharedPreference(context).edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    fun getVersion(context: Context): String {
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName,
                    0)
            info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getVersionCode(context: Context): Int {
        return try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = info.versionName
            info.versionCode.toString().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private var sDensity = 0f

    /**
     * DP转换为像素
     *
     * @param context
     * @param nDip
     * @return
     */
    fun dipToPixel(context: Context, nDip: Int): Int {
        if (sDensity == 0f) {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            sDensity = dm.density
        }
        return (sDensity * nDip).toInt()
    }

    fun Dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun Px2Dp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * 获取时间
     *
     * @param lasttime 比如 0 就是今天 -1 就是昨天
     * @return
     */
    fun getTime(lasttime: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -lasttime)
        val mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
        return SimpleDateFormat("M/d", Locale.CHINA).format(cal.time)
    }

    fun getTimestamp(ss: Int): String {
        val format: DateFormat = SimpleDateFormat("HH:mm")
        return format.format(ss)
    }

    /**
     * 判断是否为汉字
     *
     * @param str
     * @return
     */
    fun vd(str: String): Boolean {
        val chars = str.toCharArray()
        var isGB2312 = false
        for (i in chars.indices) {
            val bytes = ("" + chars[i]).toByteArray()
            if (bytes.size == 2) {
                val ints = IntArray(2)
                ints[0] = (bytes[0] and 0xff.toByte()).toInt()
                ints[1] = (bytes[1] and 0xff.toByte()).toInt()
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isGB2312 = true
                    break
                }
            }
        }
        return isGB2312
    }

    /**
     * 加减按钮计算方法
     *
     * @param max   最大值
     * @param isAdd true为+，false为-
     */
    fun setCountResult(add: TextView, sub: TextView, numTv: TextView, max: Int, isAdd: Boolean): Int {
        val count = numTv.text.toString().toInt()
        val newCount = if (isAdd) count + 1 else count - 1
        numTv.text = newCount.toString() + ""
        if (newCount == 1) {
            sub.isEnabled = false
        } else {
            sub.isEnabled = true
        }
        if (newCount == max) {
            add.isEnabled = false
        } else {
            add.isEnabled = true
        }
        return newCount
    }

    /**
     * 保留两位小数的格式化方法
     *
     * @param num
     */
    fun format(num: Double): String {
        val df = DecimalFormat("0.00")
        return df.format(num)
    }

    /**
     * 有小数保留两位，没有取整
     *
     * @param num
     */
    fun formatInt(num: Double): String {
        val df = DecimalFormat("#0.00")
        val format = df.format(num)
        val split = format.split("\\.".toRegex()).toTypedArray()
        return if (split[1] == "00") {
            split[0]
        } else if (split[1].endsWith("0")) {
            split[0] + "." + split[1][0]
        } else {
            format
        }
    }

    /**
     * 获取第一个小号的SpannableString对象
     *
     * @param str 缩小的
     * @return
     */
    //*****该方法不能用 需修改*******//
    fun getFirstOneSpannableText(str: String?): SpannableString {
        val spanStr = SpannableString(str)
        spanStr.setSpan(AbsoluteSizeSpan(48, true), 0, 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spanStr
    }

    /**
     * 获取末尾小号的SpannableString对象
     *
     * @param str 缩小的
     * @return
     */
    fun getSpannableText(str: String): SpannableString {
        val spanStr = SpannableString(str)
        spanStr.setSpan(RelativeSizeSpan(0.7f), str.length - 1, str.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spanStr
    }

    fun getSpannableText2(str: String): SpannableString {
        val spanStr = SpannableString(str)
        spanStr.setSpan(RelativeSizeSpan(0.7f), str.length - 2, str.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spanStr
    }

    /**
     * 获取末尾小号的SpannableString对象
     * 缩小两位
     *
     * @param str 缩小的
     * @return
     */
    fun getTwoSpannableText(str: String): SpannableString {
        val spanStr = SpannableString(str)
        spanStr.setSpan(RelativeSizeSpan(0.7f), str.length - 1, str.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spanStr.setSpan(RelativeSizeSpan(0.7f), str.length - 2, str.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanStr
    }

    /**
     * 18581879487 --> 185****8187
     *
     * @param mobile
     * @return encryptMobile
     */
    fun getEncryptMobile(mobile: String): String {
        val encryptMobile: String
        if (mobile.length > 10) {
            encryptMobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11)
        } else {
            //没拿到手机号，退出登录
            encryptMobile = ""
        }
        return encryptMobile
    }

    /**
     * 除过末尾字符其他全部被*代替
     *
     * @param str
     * @return
     */
    fun getHideString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            if (i < str.length - 1) {
                stringBuilder.append("*")
            } else {
                stringBuilder.append(str[str.length - 1])
            }
        }
        return stringBuilder
    }

    /**
     * 首位和末尾不隐藏其他全部隐藏
     *
     * @param str
     * @return
     */
    fun getHideTwoString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            if (i == 0) {
                stringBuilder.append(str[0])
            } else if (i < str.length - 1 && str.length > 0) {
                stringBuilder.append("*")
            } else {
                stringBuilder.append(str[str.length - 1])
            }
        }
        return stringBuilder
    }

    /**
     * 隐藏中间的位数
     *
     * @param str
     * @return
     */
    fun getHideCenterString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            if (i <= 3) {
                stringBuilder.append(str[i])
            } else if (i < str.length - 4 && str.length > 3) {
                stringBuilder.append("*")
            } else {
                stringBuilder.append(str[i])
            }
        }
        return stringBuilder
    }

    /**
     * 隐藏中间的位数
     *
     * @param str
     * @return
     */
    fun getHideCardString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            if (i <= 3) {
                stringBuilder.append(str[i])
            } else if (i < str.length - 8 && str.length > 3) {
                stringBuilder.append("*")
            } else {
                stringBuilder.append(str[i])
            }
        }
        return stringBuilder
    }

    /**
     * 隐藏中间的位数
     *
     * @param str
     * @return
     */
    fun getHideCentersString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            if (i <= 3) {
                stringBuilder.append(str[i])
            } else if (i < str.length - 4 && str.length > 3) {
                stringBuilder.append("*")
            } else {
                stringBuilder.append(str[i])
            }
        }
        return stringBuilder
    }

    /**
     * 隐藏全部的字符
     * @param str
     * @return
     */
    fun getHideAllString(str: String): StringBuilder {
        val stringBuilder = StringBuilder()
        for (i in 0 until str.length) {
            stringBuilder.append("*")
        }
        return stringBuilder
    }

    /**
     * 时间转成正常
     * 如20161128175545 转成2016-11-28 17:55：45
     */
    fun getConverterTime(dateString: String?): String? {
        var newStr = ""
        val df = SimpleDateFormat("yyyyMMddhhmmss")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val date = df.parse(dateString)
            newStr = sdf.format(date)
            return newStr
        } catch (ex: Exception) {
        }
        return null
    }

    /**
     * 去除字符串数组中的重复数据
     *
     */
    fun resetPhone(lists: List<String>?): List<String>? {
        if (lists == null) {
            return null
        }
        val list: MutableList<String> = ArrayList()
        val iterator = lists.iterator()
        while (iterator.hasNext()) {
            val str = iterator.next()
            // 去重
            if (!list.contains(str)) {
                list.add(str)
            }
        }
        return list
    }

    //设置日期格式
    val currentDate: String
        get() {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //设置日期格式
            return df.format(Date())
        }

    /**
     * 节假日查询
     * @des 用作查询指定时间是否是节假日
     * @param time 要查询的时间，格式：20161104，若为null则是当前时间
     */
    fun queryHoliday(time: String?): String {
        val appid = "26649"
        val secret = "87b15420189441dfb06d14635c268a0d"
        /*return new ShowApiRequest( "http://route.showapi.com/894-1",appid,secret)
                .addTextPara("day",time)
                .post();*/return sendPost("http://route.showapi.com/894-2", "showapi_appid=26649&showapi_sign=87b15420189441dfb06d14635c268a0d&day=")
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     * 发送请求的 URL
     * @param param
     * 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    fun sendPost(url: String?, param: String?): String {
        var out: PrintWriter? = null
        var `in`: BufferedReader? = null
        var result = ""
        try {
            val realUrl = URL(url)
            // 打开和URL之间的连接
            val conn = realUrl.openConnection()
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
            // 发送POST请求必须设置如下两行
            conn.doOutput = true
            conn.doInput = true
            // 获取URLConnection对象对应的输出流
            out = PrintWriter(conn.getOutputStream())
            // 发送请求参数
            out.print(param)
            // flush输出流的缓冲
            out.flush()
            // 定义BufferedReader输入流来读取URL的响应
            `in` = BufferedReader(
                    InputStreamReader(conn.getInputStream()))
            var line: String
            while (`in`.readLine().also { line = it } != null) {
                result += line
            }
        } catch (e: Exception) {
            println("发送 POST 请求出现异常！$e")
            e.printStackTrace()
        } //使用finally块来关闭输出流、输入流
        finally {
            try {
                out?.close()
                `in`?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        return result
    }

    /**
     * @des 判断当前时间是否在某个时间段
     * @param startHour 起始时间点
     * @param endHour 截止时间点
     */
    fun isCurrentBetweenTime(startHour: Int, endHour: Int): Boolean {
        val now = Calendar.getInstance()
        val currentHour = now[Calendar.HOUR_OF_DAY]
        Log.i("dayType", "hour:$currentHour")
        return startHour <= currentHour && endHour >= currentHour
    }
}