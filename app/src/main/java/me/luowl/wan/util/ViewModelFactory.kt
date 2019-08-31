package me.luowl.wan.util

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.local.SearchRecordDataSource
import me.luowl.wan.data.local.WanDatabase
import me.luowl.wan.data.network.WanNetwork
import me.luowl.wan.ui.FindViewModel
import me.luowl.wan.ui.MainViewModel
import me.luowl.wan.ui.account.*
import me.luowl.wan.ui.architecture.ArchitectureArticleListViewModel
import me.luowl.wan.ui.architecture.ArchitectureCategoryViewModel
import me.luowl.wan.ui.architecture.ArchitectureViewModel
import me.luowl.wan.ui.home.HomeViewModel
import me.luowl.wan.ui.navigation.NavigationViewModel
import me.luowl.wan.ui.project.ProjectListViewModel
import me.luowl.wan.ui.project.ProjectViewModel
import me.luowl.wan.ui.search.SearchViewModel
import me.luowl.wan.ui.wxarticle.ArticleListViewModel
import me.luowl.wan.ui.wxarticle.WXArticleChaptersViewModel

/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory private constructor(
    private val repository: WanRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel()
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)
                isAssignableFrom(WXArticleChaptersViewModel::class.java) ->
                    WXArticleChaptersViewModel(repository)
                isAssignableFrom(ArticleListViewModel::class.java) ->
                    ArticleListViewModel(repository)
                isAssignableFrom(ProjectViewModel::class.java) ->
                    ProjectViewModel(repository)
                isAssignableFrom(ProjectListViewModel::class.java) ->
                    ProjectListViewModel(repository)
                isAssignableFrom(NavigationViewModel::class.java) ->
                    NavigationViewModel(repository)
                isAssignableFrom(ArchitectureViewModel::class.java) ->
                    ArchitectureViewModel(repository)
                isAssignableFrom(ArchitectureCategoryViewModel::class.java) ->
                    ArchitectureCategoryViewModel()
                isAssignableFrom(ArchitectureArticleListViewModel::class.java) ->
                    ArchitectureArticleListViewModel(repository)
                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(repository, SearchRecordDataSource(WanDatabase.instance.searchRecordDao()))
                isAssignableFrom(FindViewModel::class.java) ->
                    FindViewModel()
                isAssignableFrom(MineViewModel::class.java) ->
                    MineViewModel(repository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel(repository)
                isAssignableFrom(CollectionViewModel::class.java) ->
                    CollectionViewModel(repository)
                isAssignableFrom(CoinViewModel::class.java) ->
                    CoinViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(WanRepository.getInstance(WanNetwork.getInstance()))
                    .also { INSTANCE = it }
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}