package com.chenjiajuan.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjiajuan on 2018/6/27.
 */

public class LoginWebView extends LinearLayout {
    private static final String TAG="LoginWebView";
    private WebView webView;
    private QRCodeListener qrCodeListener;
//    private String url="https://login.taobao.com/member/login.jhtml?redirectURL=https%3A%2F%2Fi.taobao.com%2Fmy_taobao.htm";
    private String url="http://login.m.taobao.com/qrcodeShow.htm?appKey=24528334&from=bcqrlogin&qrwidth=90";
    private WebViewHandler webViewHandler=new WebViewHandler(this);

    public  interface   QRCodeListener{
        void fetchLoginUrl(String url);

    }
    public QRTaskListener qrTaskListener;

    public LoginWebView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        Log.e(TAG,"initView......");
        webView=new WebView(context);
        webView.setWebChromeClient(new BridgeWebChromeClient());
        webView.setWebViewClient(new LoginInterceptWebView());
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);

    }

    private static class WebViewHandler extends Handler {
        private WeakReference<LoginWebView> webViewHandlerWeakReference;
        public WebViewHandler(LoginWebView loginWebView){
           this.webViewHandlerWeakReference=new WeakReference<>(loginWebView);

        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.e(TAG,"handleMessage.......");
                    webViewHandlerWeakReference.get().webView.loadUrl(webViewHandlerWeakReference.get().url);
                    break;
            }
        }
    }

     public   class LoginInterceptWebView extends WebViewClient {
        public LoginInterceptWebView(){

        }

         @Override
         public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.e(TAG,"api 21 shouldInterceptRequest.....");
             return super.shouldInterceptRequest(view, request);
         }

         @Override
         public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
             Log.e(TAG,"api 14 shouldInterceptRequest.....");
             if (url.contains("qrcodelogin/generateNoLoginQRCode.do")){
                 try {
                     URL url1=new URL(url);
                     try {
                         URLConnection urlConnection=url1.openConnection();
                         InputStreamReader inputStreamReader=new InputStreamReader(urlConnection.getInputStream());
                         BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                         StringBuilder sb=new StringBuilder();
                         String line=null;
                         if ((line=bufferedReader.readLine())!=null){
                             sb.append(line);
                         }
                         String result=sb.toString();
                         Log.e(TAG,"result : "+result);
                         Pattern pattern=Pattern.compile("jsonp1\\(\\{.+\\\"url\\\":\\\"((\\w|\\/|:|\\.)+)\\\"");
                         Matcher matcher=pattern.matcher(result);
                         if (matcher.find(1)){
                             String json=matcher.group(1);
                             Log.e(TAG,"json : "+json);
                             qrCodeListener.fetchLoginUrl(json);
                         }
                         ByteArrayInputStream inputStream=new ByteArrayInputStream(result.getBytes());
                         return new WebResourceResponse(urlConnection.getContentType(),
                                 urlConnection.getHeaderField("encoding"),inputStream);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 } catch (MalformedURLException e) {
                     e.printStackTrace();
                 }

             }
             return super.shouldInterceptRequest(view, url);
         }

         @Override
         public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e(TAG,"onPageStarted");
             super.onPageStarted(view, url, favicon);
         }

         @Override
         public void onPageFinished(WebView view, String url) {
             Log.e(TAG,"onPageFinished");
             super.onPageFinished(view, url);
         }

         @Override
         public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
             Log.e(TAG,"shouldOverrideUrlLoading");
             return super.shouldOverrideUrlLoading(view, request);
         }
     }

    public class BridgeWebChromeClient extends WebChromeClient{
        public BridgeWebChromeClient(){

        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.e(TAG,"onJsPrompt.......");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    public void showQRCode(QRCodeListener listener){
        this.qrCodeListener=listener;
        webView.loadUrl(url);
//         webViewHandler.sendEmptyMessage(1);
//        webView.loadUrl("https://login.taobao.com/member/login.jhtml?redirectURL=https%3A%2F%2Fi.taobao.com%2Fmy_taobao.htm");


    }

    public void setQrTaskListener(QRTaskListener qrTaskListener){
        this.qrTaskListener=qrTaskListener;
    }

}
