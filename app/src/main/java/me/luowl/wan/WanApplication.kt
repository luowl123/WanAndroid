package me.luowl.wan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.bugly.Bugly
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import me.luowl.wan.base.AppManager
import me.luowl.wan.ui.webview.SonicRuntimeImpl
import me.luowl.wan.util.logDebug
import timber.log.Timber


/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class WanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this

        val debug = BuildConfig.DEBUG
        if (debug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

//        CrashReport.initCrashReport(applicationContext, AppConfig.BUGLY_APP_ID, debug)
        Bugly.init(applicationContext, AppConfig.BUGLY_APP_ID, debug)

        LiveEventBus.get()
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)

        setApplication(this)

        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(this), SonicConfig.Builder().build())
        }

    }

    private fun setApplication(app: Application) {
        app.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityCreated")
                AppManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityStarted")
            }

            override fun onActivityResumed(activity: Activity) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityResumed")
            }

            override fun onActivityPaused(activity: Activity) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityPaused")
            }

            override fun onActivityStopped(activity: Activity) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityStopped")
            }

            override fun onActivityDestroyed(activity: Activity) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivityDestroyed")
                AppManager.finishActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                logDebug(TAG, "${activity.javaClass.simpleName} onActivitySaveInstanceState")
            }

        })
    }

    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

    companion object {
        const val TAG = "WanApplication"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}