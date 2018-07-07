package com.chenjiajuan.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by chenjiajuan on 2018/6/27.
 */

public class WebViewService extends Service {
    private static final String TAG = "WebViewService";
    private LoginWebView loginWebView;
    private IWebViewCallback callback;
    private WebViewHandler webViewHandler = new WebViewHandler(this);


    private IWebViewService webViewService = new IWebViewService.Stub() {
        @Override
        public void doLoadWebViewJsUrl(final IWebViewCallback webViewCallback) throws RemoteException {
            webViewHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "doLoadWebViewJsUrl......");
                    callback = webViewCallback;
                    loginWebView.showQRCode(new LoginWebView.QRCodeListener() {
                        @Override
                        public void fetchLoginUrl(String url) {
                            try {
                                callback.showQRCode(url);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        }
    };

    private static class WebViewHandler extends Handler {
        private WeakReference<WebViewService> weakReference;

        public WebViewHandler(WebViewService webViewService) {
            this.weakReference = new WeakReference<WebViewService>(webViewService);

        }

        @Override
        public void handleMessage(Message msg) {
        }
    }


    /**
     * 各种回调状态
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
        loginWebView = new LoginWebView(this);
        loginWebView.setQrTaskListener(new LoginQRTask());
        Log.e(TAG, "onCreate.....");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return webViewService.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        System.exit(0);
        return true;
    }


}
