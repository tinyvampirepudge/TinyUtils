package com.tinytongtong.example;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tinytongtong.tinyutils.LogUtils;
import com.tinytongtong.tinyutils.ScreenUtils;

/**
 * @Description: TinyUtils相关demo
 * @Author wangjianzhou
 * @Date 2019-08-01 15:24
 * @Version v0.0.1
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.tv);
        textView.setText(ScreenUtils.getDisplayMetricsInfo(this));

        LogUtils.e(TAG, "DisplayMetricsInfo:" + ScreenUtils.getDisplayMetricsInfo(this));
    }
}
