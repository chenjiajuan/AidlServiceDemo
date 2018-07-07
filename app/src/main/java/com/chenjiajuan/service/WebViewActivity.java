package com.chenjiajuan.service;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

/**
 * webView的基本属性
 * webView与Js交互
 */

public class WebViewActivity extends Activity {
    private static final  String TAG=WebViewActivity.class.getSimpleName();
//  private String url="https://www.baidu.com";
//  private String url="https://blog.csdn.net/carson_ho/article/details/52693322";
    private String url="file:///android_asset/javascript.html";
    private RelativeLayout rootContent;
    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        rootContent=findViewById(R.id.rootContent);
        webView=new WebView(this);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                       ViewGroup.LayoutParams.MATCH_PARENT);
        progressBar=new ProgressBar(this);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressBar.setLayoutParams(params);
        progressBar.setMax(100);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_bar_list));
        rootContent.addView(webView,layoutParams);
        webSettings=webView.getSettings();
        setBasicProperties();
        setWebViewClient();
        webView.loadUrl(url);
    }

    public void setBasicProperties(){
        webSettings.setJavaScriptEnabled(true);  //设置允许js交互
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //======
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小
        webSettings.setUseWideViewPort(true); //图片尺寸缩放值适合webView大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //======
        if (NetWorkUtils.canConnectNetWork(this)){
           webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //优先从网络获取数据
        }else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //优先使用本地缓存
        }
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "webview";
        Log.e(TAG,"cacheDirPath : "+cacheDirPath);
        webSettings.setAppCachePath(cacheDirPath); //设置 Application Caches 缓存目录
        //====
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }
    private void setWebViewClient() {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG,"onProgressChanged newProgress : "+newProgress);
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e(TAG,"onJsAlert : url "+url);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG,"onJsPrompt : url "+url);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.e(TAG,"onJsConfirm : url "+url);
                return super.onJsConfirm(view, url, message, result);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                rootContent.addView(progressBar);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                rootContent.removeView(progressBar);
                super.onPageFinished(view, url);
            }
            //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
            //加载失败时，在此处给出友好的提示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode){
                    case WebViewClient.ERROR_CONNECT:
                        Toast.makeText(WebViewActivity.this,"请检查您的网络状态！",Toast.LENGTH_LONG).show();
                        break;
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                parse(url);
                view.loadUrl(url);
                return true;
            }

        });

//        bindJsInterface();
    }
    //---------------------        Js To Android        ------------------//

    /**
     * JS调用Android的方案一
     */
    public void bindJsInterface(){
        webView.addJavascriptInterface(new JsToAndroid(),"JsToAndroid");
    }

    public class  JsToAndroid extends Object{
        @JavascriptInterface
        public boolean alertWindow(String msg){
            Toast.makeText(WebViewActivity.this," JsToAndroid msg : "+msg,Toast.LENGTH_LONG).show();
            return  true;
        }
    }

    /**
     * JS调用Android的方式二
     * 通过uri处理业务，那么如何返回值呢？
     * webview.loadUrl(function())  通过主动调用js的函数，将结果最为参数返回
     * @param url
     */
    public  void  parse(String url){
        //拦截uri实行js调用native   js://webview?username=111&password=222";
        Uri uri=Uri.parse(url);
        if (uri.getScheme().equals("js")){
            if (uri.getAuthority().equals("webview")){
                HashMap<String,String> hashMap=new HashMap<>();
                Set<String> stringSet=uri.getQueryParameterNames();
                for ( String string :stringSet ){
                    hashMap.put(string,uri.getQueryParameter(string));
                }
                for (String key:hashMap.keySet()){
                    Log.e(TAG,"hash map , key : "+key+" , value : "+hashMap.get(key));
                }
            }
        }
    }

    // -------------------          Android To Js       --------------------//
    /**
     *  Android 调Js的两种方法
     *  各有优缺点
     * @param view
     */
    public void AndroidToJs(WebView view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            evaluateJavascript();
        }else {
            view.loadUrl("javascript:callJS()");
        }
    }

    private void evaluateJavascript() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e(TAG,"evaluateJavascript");
            webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                  //获取返回值
                    Log.e(TAG,"onReceiveValue value : "+value);
                }
            });
        }
    }



    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webView!=null){
            webView.loadDataWithBaseURL(null,"", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView=null;
        }
        super.onDestroy();
    }
}