package me.luowl.wan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.base.AppManager
import me.luowl.wan.util.logDebug


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
        LiveEventBus.get()
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
        setApplication(this)
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

    companion object {
        const val TAG = "WanApplication"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}