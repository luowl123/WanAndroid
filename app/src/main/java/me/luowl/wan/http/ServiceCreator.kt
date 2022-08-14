package me.luowl.wan.http

import me.luowl.wan.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.logging.Level

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val httpClient = OkHttpClient.Builder()

    init {
        val loggingInterceptor = HttpLoggingInterceptor("WanHttp")
        loggingInterceptor.setPrintLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        loggingInterceptor.setColorLevel(Level.INFO)
        httpClient.addInterceptor(CookieInterceptor())
        httpClient.addInterceptor(loggingInterceptor)
    }

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())


    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

}