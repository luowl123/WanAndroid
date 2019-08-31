package me.luowl.wan.data.network

import me.luowl.wan.AppConfig
import me.luowl.wan.data.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
interface ApiService {
    /**
     * 首页文章列表
     * @param pageIndex 页码，拼接在连接中，从0开始
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("article/list/{pageIndex}/json")
    fun getHomeArticleList(@Path("pageIndex") pageIndex: Int): Call<BaseResp<PageData>>

    /**
     * 首页banner
     * */
    @GET("banner/json")
    fun getBanner(): Call<BaseResp<List<BannerData>>>

    /**
     *置顶文章
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("article/top/json")
    fun getTopArticleList(): Call<BaseResp<List<ArticleData>>>

    /**
     * 搜索热词,即目前搜索最多的关键词
     * */
    @GET("hotkey/json")
    fun getHotSearchKey(): Call<BaseResp<List<SearchHotKey>>>

    /**
     * 体系数据
     * */
    @GET("tree/json")
    fun getArchitectureTree(): Call<BaseResp<List<Architecture>>>

    /**
     * 知识体系下的文章
     * @param pageIndex 页码：拼接在链接上，从0开始
     * @param cid  分类的id，体系二级目录的id
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("article/list/{pageIndex}/json")
    fun getArticleListByTreeId(@Path("pageIndex") pageIndex: Int, @Query("cid") cid: Long): Call<BaseResp<PageData>>

    /**
     * 导航数据
     * */
    @GET("navi/json")
    fun getNavigationData(): Call<BaseResp<List<Navigation>>>

    /**
     * 项目分类
     * */
    @GET("project/tree/json")
    fun getProjectClassification(): Call<BaseResp<List<Architecture>>>

    /**
     * 项目列表数据
     * @param pageIndex 页码：拼接在链接上，从0开始
     * @param cid 分类的id，上面项目分类接口
     * */
    @GET("project/list/{pageIndex}/json")
    fun getProjectList(@Path("pageIndex") pageIndex: Int, @Query("cid") cid: Long): Call<BaseResp<PageData>>

    /**
     * 按时间分页展示所有项目
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("article/listproject/{pageIndex}/json")
    fun getNewestProjectList(@Path("pageIndex") pageIndex: Int): Call<BaseResp<PageData>>

    /**
     * 获取公众号列表
     * */
    @GET("wxarticle/chapters/json")
    fun getWXArticleChapters(): Call<BaseResp<List<Architecture>>>

    /**
     * 查看某个公众号历史数据
     * @param chapterId 公众号 ID：拼接在 url 中，eg:405
     * @param pageIndex 公众号页码：拼接在url 中，eg:1
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("wxarticle/list/{chapterId}/{pageIndex}/json")
    fun getWXArticleList(@Path("chapterId") chapterId: Long, @Path("pageIndex") pageIndex: Int): Call<BaseResp<PageData>>

    /**
     * 在某个公众号中搜索历史文章
     * @param chapterId 公众号 ID：拼接在 url 中，eg:405
     * @param pageIndex 公众号页码：拼接在url 中，eg:1
     * @param key 关键字
     * */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("wxarticle/list/{chapterId}/{pageIndex}/json")
    fun getWXArticleListByKey(@Path("chapterId") chapterId: Long, @Path("pageIndex") pageIndex: Int, @Query("k") key: String): Call<BaseResp<PageData>>

    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @POST("article/query/{pageIndex}/json")
    fun searchArticleByKey(@Path("pageIndex") pageIndex: Int, @Query("k") key: String): Call<BaseResp<PageData>>

    @Headers(AppConfig.HEADER_SAVE_WAN_ANDROID_COOKIE)
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") userName: String, @Field("password") password: String): Call<BaseResp<LoginData>>

    @Headers(AppConfig.HEADER_SAVE_WAN_ANDROID_COOKIE)
    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Call<BaseResp<LoginData>>

    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    fun logout(): Call<BaseResp<Any>>

    /**
     *  获取收藏列表
     *  http://www.wanandroid.com/lg/collect/list/0/json
     *  @param page
     */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("lg/collect/list/{pageIndex}/json")
    fun getCollectList(@Path("pageIndex") pageIndex: Int): Call<BaseResp<PageData>>

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id article id
     */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id: Long): Call<BaseResp<Any>>

    /**
     * 收藏站外文章
     * http://www.wanandroid.com/lg/collect/add/json
     */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    fun addCollectOutsideArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): Call<BaseResp<Any>>

    /**
     * 文章列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticleByOriginId(@Path("id") originId: Long): Call<BaseResp<Any>>

    /**
     * 收藏列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect/2805/json
     */
    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun cancelCollectArticle(
        @Path("id") id: Long,
        @Field("originId") originId: Long = -1
    ): Call<BaseResp<Any>>

    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("lg/coin/getcount/json")
    fun getMyCoinCount(): Call<BaseResp<Long>>

    @Headers(AppConfig.HEADER_AND_WAN_ANDROID_COOKIE)
    @GET("lg/coin/list/{pageIndex}/json")
    fun getCoinList(@Path("pageIndex") pageIndex: Int): Call<BaseResp<CoinPage>>
}