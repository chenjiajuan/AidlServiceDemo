package com.chenjiajuan.service;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by chenjiajuan on 2018/6/7.
 */

public class LoginWebView extends LinearLayout {
    private static final String TAG = "LoginWebView";
    private WebView webView;
    private QRCodeListener qrCodeListener;
    private String url = "xxxxxxxx"; //测试url请填自己的

    public interface QRCodeListener {
        void fetchLoginUrl(String url);

    }

    public QRTaskListener qrTaskListener;

    public LoginWebView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        webView = new WebView(context);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new LoginInterceptWebView());
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);

    }
    public class LoginInterceptWebView extends WebViewClient {
        public LoginInterceptWebView() {
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            qrCodeListener.fetchLoginUrl(url);
            return super.shouldInterceptRequest(view, url);
        }
    }

    public void showQRCode(QRCodeListener listener) {
        this.qrCodeListener = listener;
        webView.loadUrl(url);


    }

    public void setQrTaskListener(QRTaskListener qrTaskListener) {
        this.qrTaskListener = qrTaskListener;
    }

}
