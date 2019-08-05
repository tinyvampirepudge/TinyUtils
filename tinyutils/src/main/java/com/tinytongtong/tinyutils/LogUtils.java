package com.tinytongtong.tinyutils;

import android.util.Log;

import java.util.logging.Level;


/**
 * @Description: 日志管理类
 * @Author wangjianzhou
 * @Date 2019-08-01 11:37
 * @Version
 */
public class LogUtils {
    private static final String TAG = "tiny_module";
    // 是否打印日志标志
    private static boolean isOpenLog = true;

    public static void openLog(boolean flag) {
        isOpenLog = flag;
    }

    public static boolean isOpenLog() {
        return isOpenLog;
    }

    // 打印调试信息
    public static void d(String log) {
        if (isOpenLog) {
            Log.d(TAG, log);
        }
    }

    // 打印调试信息
    public static void d(String tag, String log) {
        if (isOpenLog) {
            Log.d(tag, log);
        }
    }

    // 打印调试信息
    public static void i(String tag, String log) {
        if (isOpenLog) {
            log = log == null ? "" : log;
            Log.i(tag, log);
        }
    }

    // 打印错误信息
    public static void e(String log) {
        if (isOpenLog) {
            Log.e(TAG, log);
        }
    }

    public static void e(String tag, String log) {
        if (isOpenLog) {
            Log.e(tag, log);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isOpenLog) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String log) {
        if (isOpenLog) {
            Log.i(TAG, log);
        }
    }

    public static void w(String log) {
        if (isOpenLog) {
            Log.w(TAG, log);
        }
    }

    /**
     * 打印完整log
     *
     * @param tag
     * @param msg
     */
    public static void vFullMsg(String tag, String msg) {
        if (isOpenLog) {
            printFullMsg(Log.VERBOSE, tag, msg);
        }
    }

    public static void dFullMsg(String tag, String msg) {
        if (isOpenLog) {
            printFullMsg(Log.DEBUG, tag, msg);
        }
    }

    public static void iFullMsg(String tag, String msg) {
        if (isOpenLog) {
            printFullMsg(Log.INFO, tag, msg);
        }
    }

    public static void wFullMsg(String tag, String msg) {
        if (isOpenLog) {
            printFullMsg(Log.WARN, tag, msg);
        }
    }

    /**
     * 截断输出日志，打印完整log
     * https://blog.csdn.net/oShenLi1/article/details/73332575
     *
     * @param msg
     */
    public static void eFullMsg(String tag, String msg) {
        if (isOpenLog) {
            printFullMsg(Log.ERROR, tag, msg);
        }
    }

    public static void printFullMsg(int logLevel, String tag, String msg) {

        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0) {
            return;
        }

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {
            // 长度小于等于限制直接打印
            printByLevel(logLevel, tag, msg);
        } else {
            while (msg.length() > segmentSize) {
                // 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                printByLevel(logLevel, tag, logContent);
            }
            // 打印剩余日志
            printByLevel(logLevel, tag, msg);
        }

    }

    /**
     * 根据不同logLevel值调用不同的log方法。
     *
     * @param logLevel
     * @param tag
     * @param msg
     */
    private static void printByLevel(int logLevel, String tag, String msg) {
        switch (logLevel) {
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }
    }
}
