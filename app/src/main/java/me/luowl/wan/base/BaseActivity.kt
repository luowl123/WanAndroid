package me.luowl.wan.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.event.LoginEvent
import me.luowl.wan.ui.account.LoginActivity
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.ViewModelFactory
import me.luowl.wan.util.logDebug

/*
 *
 * Created by luowl
 * Date: 2019/7/23 
 * Desc：
 */

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

//    val viewModel by lazy {
//        ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(getViewModelClass())
//    }

    val viewModel by lazy {
        ViewModelProvider(this).get(getViewModelClass())
    }

    protected lateinit var binding: V

    private var viewModelId: Int = 0

    private val pageStateViewStub by lazy {
        findViewById<ViewStub>(R.id.page_state_view_stub)?:null
    }

    private var isInflated:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接受的参数方法
        initParams()
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        registerUIChangeLiveDataCallBack()
        //初始化view
        setupViews(savedInstanceState)
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //页面数据开始加载
        startLoadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    abstract fun getViewModelClass(): Class<VM>

    open fun initParams() {

    }

    private fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
//        viewModel = initViewModel()
        viewModelId = initVariableId()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        //需要绑定lifecycleOwner，否则LiveData在xml中收不到值变化的通知
        binding.lifecycleOwner=this
    }

    //刷新布局
    fun refreshLayout() {
        binding.setVariable(viewModelId, viewModel)
    }

    private fun registerUIChangeLiveDataCallBack() {
        viewModel.stateModel.addOnPropertyChangedCallback(object :Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                logDebug("onPropertyChanged $isInflated")
                pageStateViewStub?.let {
                    if(!isInflated){
                        logDebug("onPropertyChanged $isInflated")
                        it.setOnInflateListener { stub, inflated ->
                            val viewDataBinding = DataBindingUtil.bind<ViewDataBinding>(inflated)
                            viewDataBinding?.setVariable(BR.viewModel, viewModel)
                        }
                        it.inflate()
                        isInflated=true
                    }
                }
            }
        })
        viewModel.uc.showDialogEvent.observe(this, Observer {
            showDialog(it)
        })
        viewModel.uc.dismissDialogEvent.observe(this, Observer {
            dismissDialog()
        })
        viewModel.uc.finishEvent.observe(this, Observer {
            finish()
        })
        viewModel.uc.onBackPressedEvent.observe(this, Observer {
            onBackPressed()
        })
        viewModel.uc.startActivityEvent.observe(this, Observer {
            val clz = it[BaseViewModel.CLASS] as Class<*>
            val bundle = it[BaseViewModel.BUNDLE] as Bundle
            startActivity(clz, bundle)
        })
        viewModel.uc.showLoginDialogEvent.observe(this, Observer { showLoginDialog() })
    }

//    var dialog:Dialog?

    open fun showDialog(title: String) {
//        if (dialog != null) {
//            dialog.show()
//        } else {
//            val builder = MaterialDialogUtils.showIndeterminateProgressDialog(this, title, true)
//            dialog = builder.show()
//        }
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }

    open fun dismissDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss()
//        }
        Toast.makeText(this, "dismissDialog", Toast.LENGTH_SHORT).show()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    abstract fun getLayoutId(): Int

    abstract fun initVariableId(): Int

//    abstract fun initViewModel(): VM

    open fun setupViews(savedInstanceState: Bundle?) {

    }

    open fun initViewObservable() {

    }

    open fun initLoginChangeObservable(){
        LiveEventBus.get().with(AppConfig.LOGIN_KEY, LoginEvent::class.java)
            .observe(this, Observer {
                viewModel.retry()
            })
    }

    open fun startLoadData() {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T : BaseViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
//        return ViewModelProviders.of(activity, ViewModelFactory.getInstance()).get(cls)
        return ViewModelProvider(activity).get(cls)
    }

    var toolbar: Toolbar? = null
    protected fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    protected fun showLoginDialog() {
        val dialog = AlertDialog.Builder(this).setTitle(GlobalUtil.getString(R.string.login_tip_title)).setPositiveButton(
            GlobalUtil.getString(R.string.text_login)
        ) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this@BaseActivity, LoginActivity::class.java)
            startActivity(intent)
        }.setNegativeButton(GlobalUtil.getString(R.string.text_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }


}