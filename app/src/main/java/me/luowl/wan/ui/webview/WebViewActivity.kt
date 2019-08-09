package me.luowl.wan.ui.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig
import kotlinx.android.synthetic.main.activity_web_view.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.databinding.ActivityWebViewBinding

/*
 *
 * Created by luowl
 * Date: 2019/7/25
 * Desc：
 */

class WebViewActivity : BaseActivity<ActivityWebViewBinding, BaseViewModel>() {

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java

    override fun getLayoutId() = R.layout.activity_web_view

    override fun initVariableId() = BR.viewModel

    private var sonicSession: SonicSession? = null
    private lateinit var url: String

    override fun initParams() {
        super.initParams()
        url = intent.getStringExtra(URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(intent.getStringExtra(TITLE), Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(intent.getStringExtra(TITLE))
        }
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                progressBar.visibility = View.VISIBLE
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                progressBar.visibility = View.INVISIBLE
//            }
//
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                view?.loadUrl(request?.url.toString())
//                return false
//            }
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url)
//                return false
//            }
//        }
//        webView.loadUrl(url)

        initSonic()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initSonic() {
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(application), SonicConfig.Builder().build())
        }

        var sonicSessionClient: SonicSessionClientImpl? = null

        // if it's sonic mode , startup sonic session at first time
        val sessionConfigBuilder = SonicSessionConfig.Builder()
        sessionConfigBuilder.setSupportLocalServer(true)

        // if it's offline pkg mode, we need to intercept the session connection
        //                sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
        //                    @Override
        //                    public String getCacheData(SonicSession session) {
        //                        return null; // offline pkg does not need cache
        //                    }
        //                });
        //
        //                sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
        //                    @Override
        //                    public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
        //                        return new OfflinePkgSessionConnection(BrowserActivity.this, session, intent);
        //                    }
        //                });
        //            }

        // create sonic session and run sonic flow
        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build())
        if (null != sonicSession) {
            sonicSessionClient = SonicSessionClientImpl()
            sonicSession!!.bindClient(sonicSessionClient)
        } else {
            // this only happen when a same sonic session is already running,
            // u can comment following codes to feedback as a default mode.
            // throw new UnknownError("create session fail!");
            Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show()
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.INVISIBLE
                if (sonicSession != null) {
                    sonicSession!!.sessionClient.pageFinish(url)
                }
            }

            @TargetApi(21)
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return shouldInterceptRequest(view, request.url.toString())
            }

            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                if (sonicSession != null) {
                    val requestResponse = sonicSessionClient?.requestResource(url)
                    if (requestResponse is WebResourceResponse) {
                        return requestResponse
                    }
                }
                return null
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress > 95) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }
        }

        val webSettings = webView.settings

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.javaScriptEnabled = true
        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis())
        webView.addJavascriptInterface(SonicJavaScriptInterface(sonicSessionClient, intent), "sonic")

        // init webview settings
        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        //支持缩放
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls=true
        //不显示缩放按钮
        webSettings.displayZoomControls = false

        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(webView)
            sonicSessionClient.clientReady()
        } else { // default mode
            webView.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != sonicSession) {
            sonicSession!!.destroy()
            sonicSession = null
        }
    }

    companion object {
        private const val TITLE = "title"

        private const val URL = "url"

        fun startActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, WebViewActivity::class.java).apply {
                putExtra(TITLE, title)
                putExtra(URL, url)
            }
            context.startActivity(intent)
        }
    }
}