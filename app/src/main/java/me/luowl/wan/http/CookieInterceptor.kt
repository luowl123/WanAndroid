package me.luowl.wan.http

import me.luowl.wan.AppConfig
import me.luowl.wan.util.Preference
import me.luowl.wan.util.logDebug
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc CookieInterceptor: 添加Cookie，保存Cookie
 */
class CookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        val addCookie = request.header(AppConfig.ADD_WAN_ANDROID_COOKIE)?.equals("true") ?: false
        logDebug("addCookie:$addCookie")

        builder.addHeader("Content-type", "application/json; charset=utf-8")

        val domain = request.url().host()
        val requestUrl = request.url().toString()
        if (domain.isNotEmpty() && addCookie) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(AppConfig.COOKIE_NAME, cookie)
            }
        }

        val response = chain.proceed(builder.build())
        val saveCookie = request.header(AppConfig.SAVE_WAN_ANDROID_COOKIE)?.equals("true") ?: false
        logDebug("saveCookie:$saveCookie")
        // set-cookie maybe has multi, login to save cookie
        if (saveCookie && response.headers(AppConfig.SET_COOKIE_KEY).isNotEmpty()) {
            val cookies = response.headers(AppConfig.SET_COOKIE_KEY)
            val cookie = AppConfig.encodeCookie(cookies)
            AppConfig.saveCookie(requestUrl, domain, cookie)
        }

        return response
    }

}