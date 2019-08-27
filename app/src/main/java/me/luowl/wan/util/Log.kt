package me.luowl.wan.util

import android.os.Build
import timber.log.Timber

/**
 * 日志操作的扩展工具类。
 *
 */

fun Any.logVerbose(msg: String?) {
    Timber.v(msg)
}

fun Any.logDebug(msg: String?) {
    Timber.v(msg)
}

fun Any.logInfo(msg: String?) {
    Timber.i(msg)
}

fun Any.logWarn(msg: String?, tr: Throwable? = null) {
    Timber.w(msg)
}

fun Any.logError(msg: String?, tr: Throwable) {
    Timber.e(tr, msg)
}

fun logVerbose(tag: String, msg: String?) {
    Timber.tag(tag).v(tag, msg)
}

fun logDebug(tag: String, msg: String?) {
    Timber.tag(tag).d(msg)
}

fun logInfo(tag: String, msg: String?) {
    Timber.tag(tag).i(msg)
}

fun logWarn(tag: String, msg: String?, tr: Throwable? = null) {
    Timber.tag(tag).w(tag, msg)
}

fun logError(tag: String, msg: String?, tr: Throwable) {
    Timber.tag(tag).e(tag, msg)
}

fun printlnDeviceInfo(){
    val head = "************* Log Head ****************" +
            "\nDevice Manufacturer 制造商 : " + Build.MANUFACTURER +
            "\nDevice Model 型号          : " + Build.MODEL +
            "\nIdentifier_Brand  品牌     : " + Build.BRAND +
            "\nIdentifier_Device 设备名   : " + Build.DEVICE +
            "\nAndroid Version           : " + Build.VERSION.RELEASE +
            "\nAndroid SDK               : " + Build.VERSION.SDK_INT +
            "\n************* Log Head ****************\n\n"
    logDebug("Device",head)
}
