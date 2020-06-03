package com.tinytongtong.tinyutils

import com.tinytongtong.tinyutils.LogUtils.d

/**
 * @Description: list相关工具类。
 *
 * @Author wangjianzhou
 * @Date 2019-08-01 11:39
 * @Version
 */
object ListUtils {
    /**
     * 判断list是否为空
     *
     * @param list
     * @return list部位空且size大于零。
     */
    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.size == 0
    }

    /**
     * 必将两个List是否相同，长度，顺序。
     * 需要重写元素的equals()方法和hashCode()方法。
     *
     * @param list1
     * @param list2
     * @return
     */
    fun equals(list1: List<*>?, list2: List<*>?): Boolean {
        if (list1 === list2) {
            return true
        }
        if (list1 == null || list2 == null) {
            return false
        }
        if (list1.size != list2.size) {
            return false
        }
        for (i in list1.indices) {
            if (list1[i] != list2[i]) {
                return false
            }
        }
        return true
    }

    fun logList(list: List<*>) {
        if (!isEmpty(list)) {
            for (j in list.indices) {
                d("position " + j + " --> " + list[j].toString())
            }
        }
    }
}