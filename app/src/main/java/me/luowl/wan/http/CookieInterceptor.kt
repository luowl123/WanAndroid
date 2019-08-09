package me.luowl.wan.http

import me.luowl.wan.AppConfig
import me.luowl.wan.util.Preference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc CookieInterceptor: 添加Cookie，保存Cookie
 */
class CookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")

        val domain = request.url().host()
        val requestUrl = request.url().toString()
        if (domain.isNotEmpty() && (requestUrl.contains(AppConfig.COLLECTIONS_WEBSITE)
                    || requestUrl.contains(AppConfig.UNCOLLECTIONS_WEBSITE)
                    || requestUrl.contains(AppConfig.ARTICLE_WEBSITE)
                    || requestUrl.contains(AppConfig.TODO_WEBSITE))
        ) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(AppConfig.COOKIE_NAME, cookie)
            }
        }

        val response = chain.proceed(builder.build())
        // set-cookie maybe has multi, login to save cookie
        if ((requestUrl.contains(AppConfig.SAVE_USER_LOGIN_KEY)
                    || requestUrl.contains(AppConfig.SAVE_USER_REGISTER_KEY))
            && response.headers(AppConfig.SET_COOKIE_KEY).isNotEmpty()
        ) {
            val cookies = response.headers(AppConfig.SET_COOKIE_KEY)
            val cookie = AppConfig.encodeCookie(cookies)
            AppConfig.saveCookie(requestUrl, domain, cookie)
        }

        return response
    }

}