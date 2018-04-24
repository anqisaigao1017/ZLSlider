package com.jude.rollviewpagerdome.utils;

import android.util.Log;

import com.jude.rollviewpagerdome.BuildConfig;

import java.text.SimpleDateFormat;

/**
 * Created by garry on 17/11/10.
 */

public class LogUtil {

    static String sTAG = "ZLSlider";

    static String sClassName;

    static String sMethodName;

    static int sLineNumber;

    static String sLog;
    public static boolean sIsDebug = true;

    private LogUtil() {

    }

    public static boolean isDebuggable() {
        return sIsDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    private static String createLog(String log) {

        return Thread.currentThread().getName() + "[" + sClassName + ":" + sMethodName + ":" + sLineNumber + "]" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        sClassName = sElements[1].getFileName();
        sMethodName = sElements[1].getMethodName();
        sLineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable()) {
            return;
        }

        // Throwable sInstance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(sTAG, createLog(message));
    }

    public static void e(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }

        // Throwable sInstance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag, createLog(message));
    }

    public static void e(String tag, String msg, Exception e) {
        if (!isDebuggable()) {
            return;
        }
        Log.e(tag, msg, e);
    }

    public static void i(String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.i(sTAG, createLog(message));
    }

    public static void i(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.i(tag, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.d(sTAG, createLog(message));
    }

    public static void d(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(tag, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.v(sTAG, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.w(sTAG, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(sTAG, createLog(message));
    }

    public static void timelog(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(sTAG, message + " : " + SimpleDateFormat.getDateTimeInstance().format(System
                .currentTimeMillis()));
    }
}