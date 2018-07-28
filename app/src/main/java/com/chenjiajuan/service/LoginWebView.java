package com.chenjiajuan.service;

import android.content.Context;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjiajuan on 2018/6/7.
 */

public class LoginWebView extends LinearLayout {
    private static final String TAG = "LoginWebView";
    private WebView webView;
    private QRCodeListener qrCodeListener;
    private String url = "http://login.m.taobao.com/qrcodeShow.htm?appKey=24528334&from=bcqrlogin&qrwidth=90";

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
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.contains("qrcodelogin/generateNoLoginQRCode.do")) {
                try {
                    URL url1 = new URL(url);
                    try {
                        URLConnection urlConnection = url1.openConnection();
                        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        if ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                        String result = sb.toString();
                        Log.e(TAG, "result : " + result);
                        Pattern pattern = Pattern.compile("jsonp1\\(\\{.+\\\"url\\\":\\\"((\\w|\\/|:|\\.)+)\\\"");
                        Matcher matcher = pattern.matcher(result);
                        if (matcher.find(1)) {
                            String json = matcher.group(1);
                            qrCodeListener.fetchLoginUrl(json);
                        }
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                        return new WebResourceResponse(urlConnection.getContentType(),
                                urlConnection.getHeaderField("encoding"), inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
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
