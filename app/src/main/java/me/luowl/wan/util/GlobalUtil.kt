package me.luowl.wan.util

import android.widget.Toast
import androidx.core.content.ContextCompat
import me.luowl.wan.WanApplication


/**
 * 应用程序全局的通用工具类，功能比较单一，经常被复用的功能，应该封装到此工具类当中，从而给全局代码提供方面的操作。
 *
 */
object GlobalUtil {

    private var TAG = "GlobalUtil"

    private val toast: Toast by lazy {
        Toast.makeText(WanApplication.context,null,Toast.LENGTH_SHORT)
    }

    fun showToastShort(msg: String) {
        toast.setText(msg)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    /**
     * 获取当前应用程序的包名。
     *
     * @return 当前应用程序的包名。
     */
    val appPackage: String
        get() = WanApplication.context.packageName

    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getString(resId: Int): String {
        return WanApplication.context.resources.getString(resId)
    }

    /**
     * 获取指定资源名的资源id。
     *
     * @param name
     * 资源名
     * @param type
     * 资源类型
     * @return 指定资源名的资源id。
     */
    fun getResourceId(name: String, type: String): Int {
        return WanApplication.context.resources.getIdentifier(name, type, appPackage)
    }

    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(WanApplication.context, resId)
    }

}
