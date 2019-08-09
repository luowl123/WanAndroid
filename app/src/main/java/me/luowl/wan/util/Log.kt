package me.luowl.wan.util

import android.util.Log

/**
 * 日志操作的扩展工具类。
 *
 */

private const val VERBOSE = 1
private const val DEBUG = 2
private const val INFO = 3
private const val WARN = 4
private const val ERROR = 5
private const val NOTHING = 6

    private val level = VERBOSE
//private val level = if (isDebug) VERBOSE else WARN

fun Any.logVerbose(msg: String?) {
    if (level <= VERBOSE) {
        Log.v(javaClass.simpleName, msg.toString())
    }
}

fun Any.logDebug(msg: String?) {
    if (level <= DEBUG) {
        Log.d(javaClass.simpleName, msg.toString())
    }
}

fun Any.logInfo(msg: String?) {
    if (level <= INFO) {
        Log.i(javaClass.simpleName, msg.toString())
    }
}

fun Any.logWarn(msg: String?, tr: Throwable? = null) {
    if (level <= WARN) {
        if (tr == null) {
            Log.w(javaClass.simpleName, msg.toString())
        } else {
            Log.w(javaClass.simpleName, msg.toString(), tr)
        }
    }
}

fun Any.logError(msg: String?, tr: Throwable) {
    if (level <= ERROR) {
        Log.e(javaClass.simpleName, msg.toString(), tr)
    }
}

fun logVerbose(tag: String, msg: String?) {
    if (level <= VERBOSE) {
        Log.v(tag, msg.toString())
    }
}

fun logDebug(tag: String, msg: String?) {
    if (level <= DEBUG) {
        Log.d(tag, msg.toString())
    }
}

fun logInfo(tag: String, msg: String?) {
    if (level <= INFO) {
        Log.i(tag, msg.toString())
    }
}

fun logWarn(tag: String, msg: String?, tr: Throwable? = null) {
    if (level <= WARN) {
        if (tr == null) {
            Log.w(tag, msg.toString())
        } else {
            Log.w(tag, msg.toString(), tr)
        }
    }
}

fun logError(tag: String, msg: String?, tr: Throwable) {
    if (level <= ERROR) {
        Log.e(tag, msg.toString(), tr)
    }
}

fun printStackTrace(t:Throwable?){
    if (level <= ERROR) {
        t?.printStackTrace()
    }
}