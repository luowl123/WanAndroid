package me.luowl.wan.data.network

import me.luowl.wan.http.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class WanNetwork {

    private val apiService = ServiceCreator.create(ApiService::class.java)

    suspend fun getHomeArticleList(pageIndex: Int) = apiService.getHomeArticleList(pageIndex).await()

    suspend fun getBanner() = apiService.getBanner().await()

    suspend fun getTopArticleList() = apiService.getTopArticleList().await()

    suspend fun getHotSearchKey() = apiService.getHotSearchKey().await()

    suspend fun getArchitectureTree() = apiService.getArchitectureTree().await()

    suspend fun getArticleListByTreeId(pageIndex: Int, cid: Long) =
        apiService.getArticleListByTreeId(pageIndex, cid).await()

    suspend fun getNavigationData() = apiService.getNavigationData().await()

    suspend fun getProjectClassification() = apiService.getProjectClassification().await()

    suspend fun getProjectList(pageIndex: Int, cid: Long) = apiService.getProjectList(pageIndex, cid).await()

    suspend fun getNewestProjectList(pageIndex: Int) = apiService.getNewestProjectList(pageIndex).await()

    suspend fun getWXArticleChapters() = apiService.getWXArticleChapters().await()

    suspend fun getWXArticleList(chapterId: Long, pageIndex: Int) =
        apiService.getWXArticleList(chapterId, pageIndex).await()

    suspend fun getWXArticleListByKey(chapterId: Long, pageIndex: Int, key: String) =
        apiService.getWXArticleListByKey(chapterId, pageIndex, key).await()

    suspend fun searchArticleByKey(pageIndex: Int, key: String) = apiService.searchArticleByKey(pageIndex, key).await()

    suspend fun login(username:String,password:String)=apiService.login(username,password).await()

    suspend fun register(username:String,password:String,repassword:String)=apiService.register(username,password,repassword).await()

    suspend fun logout()=apiService.logout().await()

    suspend fun getCollectList(pageIndex: Int)=apiService.getCollectList(pageIndex).await()

    suspend fun addCollect(articleId:Long)=apiService.addCollectArticle(articleId).await()

    suspend fun addCollectOutsideArticle(title: String,author: String,link: String)=apiService.addCollectOutsideArticle(title,author,link).await()

    suspend fun cancelCollectArticleByOriginId(articleId:Long)=apiService.cancelCollectArticleByOriginId(articleId).await()

    suspend fun cancelCollectArticle(articleId:Long,originId: Long=-1)=apiService.cancelCollectArticle(articleId,originId).await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: WanNetwork? = null

        fun getInstance(): WanNetwork {
            if (network == null) {
                synchronized(WanNetwork::class.java) {
                    if (network == null) {
                        network = WanNetwork()
                    }
                }
            }
            return network!!
        }
    }
}