package com.tinytongtong.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.tinytongtong.tinyutils.LogUtils
import com.tinytongtong.tinyutils.ScreenUtils

/**
 * @Description: TinyUtils相关demo
 * @Author wangjianzhou
 * @Date 2019-08-01 15:24
 * @Version v0.0.1
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.tv)
        textView.text = ScreenUtils.getDisplayMetricsInfo(this)
        LogUtils.e(TAG, "DisplayMetricsInfo:" + ScreenUtils.getDisplayMetricsInfo(this))
    }

}