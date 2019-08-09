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
    const val DEFAULT_TIMEOUT: Long = 15
    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"

    const val COLLECTIONS_WEBSITE = "lg/collect"
    const val UNCOLLECTIONS_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val TODO_WEBSITE = "lg/todo"

    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

    const val ARTICLE_COLLECT="article_collect"

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
        user=""
        pwd=""
        token=""
        Preference.clearPreference()
//        CookieManager().clearAllCookies()
    }

}