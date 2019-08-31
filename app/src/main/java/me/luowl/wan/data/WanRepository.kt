package me.luowl.wan.data

import me.luowl.wan.data.network.WanNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class WanRepository private constructor(private val network: WanNetwork) {

    suspend fun getHomeArticleList(pageIndex: Int) = withContext(Dispatchers.IO) {
        network.getHomeArticleList(pageIndex)
    }

    suspend fun getBanner() = withContext(Dispatchers.IO) {
        network.getBanner()
    }

    suspend fun getTopArticleList() = withContext(Dispatchers.IO) {
        network.getTopArticleList()
    }

    suspend fun getHotSearchKey() = withContext(Dispatchers.IO) {
        network.getHotSearchKey()
    }

    suspend fun getArchitectureTree() = withContext(Dispatchers.IO) {
        network.getArchitectureTree()
    }

    suspend fun getArticleListByTreeId(pageIndex: Int, cid: Long) = withContext(Dispatchers.IO) {
        network.getArticleListByTreeId(pageIndex, cid)
    }

    suspend fun getNavigationData() = withContext(Dispatchers.IO) {
        network.getNavigationData()
    }

    suspend fun getProjectClassification() = withContext(Dispatchers.IO) {
        network.getProjectClassification()
    }

    suspend fun getProjectList(pageIndex: Int, cid: Long) = withContext(Dispatchers.IO) {
        network.getProjectList(pageIndex, cid)
    }

    suspend fun getNewestProjectList(pageIndex: Int) = withContext(Dispatchers.IO) {
        network.getNewestProjectList(pageIndex)
    }

    suspend fun getWXArticleChapters() = withContext(Dispatchers.IO) {
        network.getWXArticleChapters()
    }

    //每日问答 440
    //面试相关 73
    suspend fun getWXArticleList(chapterId: Long, pageIndex: Int) = withContext(Dispatchers.IO) {
        network.getWXArticleList(chapterId, pageIndex)
    }

    suspend fun getWXArticleListByKey(chapterId: Long, pageIndex: Int, key: String) = withContext(Dispatchers.IO) {
        network.getWXArticleListByKey(chapterId, pageIndex, key)
    }

    suspend fun searchArticleByKey(pageIndex: Int, key: String) = withContext(Dispatchers.IO) {
        network.searchArticleByKey(pageIndex, key)
    }

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        network.login(username, password)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        network.logout()
    }

    suspend fun register(username: String, password: String, repassword: String) = withContext(Dispatchers.IO) {
        network.register(username, password, repassword)
    }

    suspend fun getCollectionList(pageIndex: Int) = withContext(Dispatchers.IO) {
        network.getCollectList(pageIndex)
    }

    suspend fun  addCollect(articleId:Long) = withContext(Dispatchers.IO){
        network.addCollect(articleId)
    }

    suspend fun addCollectOutsideArticle(title: String,author: String,link: String)= withContext(Dispatchers.IO){
        network.addCollectOutsideArticle(title,author,link)
    }

    suspend fun cancelCollectArticleByOriginId(articleId:Long)= withContext(Dispatchers.IO){
        network.cancelCollectArticleByOriginId(articleId)
    }

    suspend fun cancelCollectArticle(articleId:Long,originId: Long=-1)= withContext(Dispatchers.IO){
        network.cancelCollectArticle(articleId,originId)
    }

    suspend fun getMyCoinCount()= withContext(Dispatchers.IO){
        network.getMyCoinCount()
    }

    suspend fun getCoinList(pageIndex: Int) = withContext(Dispatchers.IO) {
        network.getCoinList(pageIndex)
    }

    companion object {
        private var instance: WanRepository? = null

        fun getInstance(network: WanNetwork): WanRepository {
            if (instance == null) {
                synchronized(WanRepository::class.java) {
                    if (instance == null) {
                        instance = WanRepository(network)
                    }
                }
            }
            return instance!!
        }
    }
}