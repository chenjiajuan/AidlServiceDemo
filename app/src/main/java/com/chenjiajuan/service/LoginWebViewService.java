package com.chenjiajuan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import java.lang.ref.WeakReference;

/**
 * Created by chenjiajuan on 2018/6/7.
 */

public class LoginWebViewService extends Service {
    private static final String TAG = "LoginWebViewService";
    private LoginWebView loginWebView;
    private IWebViewCallback callback;
    private WebViewHandler webViewHandler = new WebViewHandler(this);

    /**
     * 由于加载webview页面必须在主线程，所以此处采用了handler
     */
    private IWebViewService webViewService = new IWebViewService.Stub() {
        @Override
        public void doLoadWebViewJsUrl(final IWebViewCallback webViewCallback) throws RemoteException {
            Message message=new Message();
            message.what=0;
            message.obj=webViewCallback;
            webViewHandler.sendMessage(message);

        }
    };

    private static class WebViewHandler extends Handler {
        private WeakReference<LoginWebViewService> weakReference;
        public WebViewHandler(LoginWebViewService webViewService) {
            this.weakReference = new WeakReference<>(webViewService);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    weakReference.get().callback = (IWebViewCallback) msg.obj;
                    weakReference.get().loginWebView.showQRCode(new LoginWebView.QRCodeListener() {
                        @Override
                        public void fetchLoginUrl(String url) {
                            try {
                                weakReference.get().callback.showQRCode(url);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    }


    /**
     * 使用Webview的回调，通过Activity内的callback，返回状态给Activity
     * webveiw--->Service-->Activity
     */
    private class LoginQRTask implements QRTaskListener {
        public LoginQRTask() {

        }

        @Override
        public void onQRLoginSuccess(String userInfo) {
            try {
                callback.onQRLoginSuccess(userInfo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onQRLoginFailure(int code, String msg) {
            try {
                callback.onQRLoginFailure(code, msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onQRScanCodeSuccess(int code, String msg) {
            try {
                callback.onQRScanCodeSuccess(code, msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onQRRefresh(int code, String msg) {
            try {
                callback.onQRRefresh(code, msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化webview
        loginWebView = new LoginWebView(this);
        //设置监听
        loginWebView.setQrTaskListener(new LoginQRTask());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return webViewService.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        //干掉进程！！！！
        System.exit(0);
        return true;
    }


}
