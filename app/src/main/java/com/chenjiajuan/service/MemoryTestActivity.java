package com.chenjiajuan.service;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by chenjiajuan on 2018/6/7.
 * 常规webview内存泄漏的方案
 */
public class MemoryTestActivity extends Activity {
    private WebView webView;
    private String url="https://www.jianshu.com/u/772becec8ed4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momory);
        webView = new WebView(this);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.loadUrl(url);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(webView, params);
    }

    @Override
    protected void onDestroy() {
        ViewParent parent = webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webView);
        }
        webView.stopLoading();
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearHistory();
        webView.clearView();
        webView.removeAllViews();
        webView.destroy();
        webView=null;
        super.onDestroy();
    }
}
