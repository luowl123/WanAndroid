package me.luowl.wan

import me.luowl.wan.data.model.LoginData
import me.luowl.wan.util.Preference


/*
 *
 * Created by luowl
 * Date: 2019/8/5 
 * Desc：
 */

object AppConfig {

    const val SAVE_WAN_ANDROID_COOKIE = "save_wan_android_cookie"
    const val HEADER_SAVE_WAN_ANDROID_COOKIE = "save_wan_android_cookie:true"

    const val ADD_WAN_ANDROID_COOKIE = "add_wan_android_cookie"
    const val HEADER_AND_WAN_ANDROID_COOKIE = "add_wan_android_cookie:true"

    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val PROJECT_ISSUES_URL="https://github.com/luowl123/WanAndroid/issues"
    const val ABOUT_APP_URL="file:///android_asset/about_app.html"
    const val BUGLY_APP_ID=""//自己申请

    fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            .map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            .forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }

    fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        var spUrl: String by Preference(url, cookies)
        @Suppress("UNUSED_VALUE")
        spUrl = cookies
        domain ?: return
        var spDomain: String by Preference(domain, cookies)
        @Suppress("UNUSED_VALUE")
        spDomain = cookies
    }

    const val LOGIN_KEY = "login"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
    const val TOKEN_KEY = "token"

    /**
     * local username
     */
     var user: String by Preference(USERNAME_KEY, "")

    /**
     * local password
     */
     var pwd: String by Preference(PASSWORD_KEY, "")

    /**
     * token
     */
     var token: String by Preference(TOKEN_KEY, "")

    fun saveLoginInfo(info:LoginData){
        user=info.username
        pwd=info.password
        token=info.token
    }

    fun clearLoginInfo(){
        Preference.clearPreference()
    }

}