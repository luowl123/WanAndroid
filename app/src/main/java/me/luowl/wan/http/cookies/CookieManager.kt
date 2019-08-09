package me.luowl.wan.http.cookies

import me.luowl.wan.util.logDebug
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager : CookieJar {

    private val cookieStore = PersistentCookieStore()

    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        logDebug("saveFromResponse")
        cookies ?: return
        url ?: return
        if (cookies.size > 0) {
            for (cookie in cookies) {
                logDebug("cookie:$cookie")
                cookieStore.add(url, cookie)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        logDebug("loadForRequest")
        return cookieStore.get(url)
    }

    /**
     * 清除所有cookie
     */
    fun clearAllCookies() {
        cookieStore.removeAll()
    }

    /**
     * 清除指定cookie
     *
     * @param url HttpUrl
     * @param cookie Cookie
     * @return if clear cookies
     */
    fun clearCookies(url: HttpUrl, cookie: Cookie): Boolean {
        return cookieStore.remove(url, cookie)
    }

    /**
     * 获取cookies
     *
     * @return List<Cookie>
    </Cookie> */
    fun getCookies(): List<Cookie> {
        return cookieStore.getCookies()
    }
}