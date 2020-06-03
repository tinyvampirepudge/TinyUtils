package com.tinytongtong.tinyutils

import android.os.Bundle
import android.text.TextUtils
import com.tinytongtong.tinyutils.LogUtils.d
import java.util.*

/**
 * @Description: Map工具类
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:38
 * @Version
 */
object MapUtils {
    //界面之间传递的hashMap参数的key
    const val MAP_PARAMS = "MAP_PARAMS"

    /**
     * 将第二个map的数据添加进第一个map里面。
     *
     * @param dstMap
     * @param srcMap
     * @return
     */
    fun addMapToMap(dstMap: MutableMap<String?, String?>?, srcMap: Map<String?, String?>?): Map<String?, String?>? {
        var dstMap = dstMap
        if (srcMap == null || srcMap.size == 0) {
            return dstMap
        }
        if (dstMap == null) {
            dstMap = HashMap()
        }
        val iter: Iterator<*> = srcMap.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next() as Map.Entry<*, *>
            val key = entry.key as String
            val value = entry.value as String
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(key.trim { it <= ' ' }) && !TextUtils.isEmpty(value) && !TextUtils.isEmpty(value.trim { it <= ' ' })) {
                dstMap[key] = value
            }
        }
        return dstMap
    }

    fun getUrlWithParaBundle(url: String?, para: Bundle?): String {
        var url = url
        return if (url == null) {
            "unknown"
        } else {
            if (para == null || para.size() == 0) {
                url
            } else {
                for (key in para.keySet()) {
                    val `val` = para[key].toString() + ""
                    url += if (url.contains("?")) {
                        "&$key=$`val`"
                    } else {
                        "?$key=$`val`"
                    }
                }
                url
            }
        }
    }

    fun getUrlWithParaMap(url: String?, para: Map<String?, String?>?): String {
        var url = url
        return if (url == null) {
            "unknown"
        } else {
            if (para == null || para.size == 0) {
                url
            } else {
                val iter: Iterator<*> = para.entries.iterator()
                while (iter.hasNext()) {
                    val entry = iter.next() as Map.Entry<*, *>
                    val key = entry.key as String
                    val `val` = entry.value as String
                    url += if (url.contains("?")) {
                        "&$key=$`val`"
                    } else {
                        "?$key=$`val`"
                    }
                }
                url
            }
        }
    }

    /**
     * 将url中后边的参数拼接到map中。
     *
     * @param url
     * @return
     */
    fun getMapParamsFromUrl(url: String): Map<String, String> {
        var url = url
        if (TextUtils.isEmpty(url)) {
            return HashMap()
        }
        val startFlag = "?"
        val split = "&"
        val anchor = "#"
        val mapPara: MutableMap<String, String> = HashMap()
        val start = url.indexOf(startFlag)
        if (start != -1) {
            var anchorIndex = -1
            anchorIndex = url.indexOf(anchor)
            url = if (anchorIndex != -1 && anchorIndex > start) {
                url.substring(start + startFlag.length, anchorIndex)
            } else {
                url.substring(start + startFlag.length)
            }
        }
        if (!TextUtils.isEmpty(url)) {
            val strs = url.split(split.toRegex()).toTypedArray()
            if (strs != null && strs.size > 0) {
                for (i in strs.indices) {
                    if (!TextUtils.isEmpty(strs[i])) {
                        val strsTemp = strs[i].split("=".toRegex()).toTypedArray()
                        if (strsTemp != null && strsTemp.size > 1) {  //大于1防止数组越界异常
                            if (!TextUtils.isEmpty(strsTemp[0]) && !TextUtils.isEmpty(strsTemp[1])) {
                                mapPara[strsTemp[0]] = strsTemp[1]
                            }
                        }
                    }
                }
            }
        }
        return mapPara
    }

    fun getBundleFromMap(mapParams: Map<String?, String?>?): Bundle {
        val params = Bundle()
        if (mapParams != null) {
            val iter: Iterator<*> = mapParams.entries.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key as String
                val `val` = entry.value as String
                params.putString(key, `val`)
            }
        }
        return params
    }

    /**
     * 在hashmap中，通过key查找value.
     *
     * @param map
     * @param key
     * @return
     */
    fun getValueByKey(map: Map<String?, String?>?, key: String): String? {
        if (map == null) {
            return null
        }
        val iter: Iterator<*> = map.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next() as Map.Entry<*, *>
            if (key == entry.key as String) {
                return entry.value as String?
            }
        }
        return null
    }

    /**
     * log打印测试
     *
     * @param map
     */
    fun logMapParams(map: Map<String?, String?>?) {
        if (map != null) {
            val iter: Iterator<*> = map.entries.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key as String
                val `val` = entry.value as String
                d("key --> $key,value --> $`val`")
            }
        }
    }

    fun logMapParams(para: Bundle?) {
        if (para != null) {
            for (key in para.keySet()) {
                val `val` = para[key].toString() + ""
                d("key --> $key,value --> $`val`")
            }
        }
    }

    /**
     * 将Map<String></String>,String>类型的参数拼接到GET请求的URL后面。
     *
     * @param url
     * @param map
     * @return
     */
    fun addParamToUrl(url: String?, map: Map<String?, String?>?): String? {
        if (TextUtils.isEmpty(url) || map == null || map.size == 0) {
            return null
        }
        val sb = StringBuilder(url)
        if (map != null) {
            val iter: Iterator<*> = map.entries.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key as String
                val `val` = entry.value as String
                d("key --> $key,value --> $`val`")
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(`val`)) {
                    if (sb.toString().contains("?")) {
                        sb.append("&$key=$`val`")
                    } else {
                        sb.append("?$key=$`val`")
                    }
                }
            }
        }
        return sb.toString()
    }

    fun addDataToMap(key: String?, value: String?, map: MutableMap<String?, String?>?) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && map != null) {
            map[key] = value
        }
    }

    fun removeNullValue(map: MutableMap<String?, String?>?) {
        if (map != null) {
            val iter: MutableIterator<*> = map.entries.iterator()
            while (iter.hasNext()) {
                val entry = iter.next() as Map.Entry<*, *>
                val key = entry.key as String
                val `val` = entry.value as String
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(`val`)) {
                    iter.remove()
                }
            }
        }
    }
}