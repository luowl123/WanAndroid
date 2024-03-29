package me.luowl.wan.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
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

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {

//    val viewModel by lazy {
//        ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(getViewModelClass())
//    }

    val viewModel by lazy {
        ViewModelProvider(this).get(getViewModelClass())
    }

    protected lateinit var binding: V

    private var viewModelId: Int = 0

    private var pageStateViewStub:ViewStub? = null
    private var isInflated:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getLayoutId(),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageStateViewStub=view.findViewById(R.id.page_state_view_stub)?:null
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //初始化view
        setupViews(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        initLoginChangeObservable()
        startLoadData()
    }

    abstract fun getViewModelClass(): Class<VM>

    open fun initParams() {

    }

    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner=this
    }

    //刷新布局
    fun refreshLayout() {
        binding.setVariable(viewModelId, viewModel)
    }

    private fun registerUIChangeLiveDataCallBack() {
        viewModel.stateModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
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
            activity?.finish()
        })
        viewModel.uc.onBackPressedEvent.observe(this, Observer {
            activity?.onBackPressed()
        })
        viewModel.uc.startActivityEvent.observe(this, Observer {
            val clz = it[BaseViewModel.CLASS] as Class<*>
            val bundle = it[BaseViewModel.BUNDLE] as Bundle
            startActivity(clz, bundle)
        })
    }

//    var dialog:Dialog?

    open fun showDialog(title: String) {
//        if (dialog != null) {
//            dialog.show()
//        } else {
//            val builder = MaterialDialogUtils.showIndeterminateProgressDialog(this, title, true)
//            dialog = builder.show()
//        }
        GlobalUtil.showToastShort(title)
    }

    open fun dismissDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss()
//        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(context, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, clz)
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

    fun isBackPressed(): Boolean {
        return false
    }

    protected fun showLoginDialog() {
        context?.let {
            val dialog = AlertDialog.Builder(it).setTitle(GlobalUtil.getString(R.string.login_tip_title)).setPositiveButton(
                GlobalUtil.getString(R.string.text_login)
            ) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }.setNegativeButton(GlobalUtil.getString(R.string.text_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}