package com.urban.piper.utility;

import android.util.Log;

import com.urban.piper.BuildConfig;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class LogUtility {

    /**
     * Prints info log message
     * @param tag - Used to identify the source of a log message
     * @param msg - The message you would like logged.
     */
    private static final String TAG_PREFIX = "piperCustomLog_";

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG_PREFIX + tag, msg);
        }
    }

    /**
     * Prints debug log message
     * @param tag - Used to identify the source of a log message
     * @param msg - The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG_PREFIX + tag, msg);
        }
    }

    /**
     * Prints error log message
     * @param tag - Used to identify the source of a log message
     * @param msg - The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG_PREFIX + tag, msg);
        }
    }

    /**
     * Prints the stack trace of exception object passed
     * @param exception Exception object whose stack trace to be printed
     */
    public static void printStackTrace(Exception exception) {
        if (BuildConfig.DEBUG) {
            exception.printStackTrace();
        }
    }
}
