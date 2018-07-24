package com.chenjiajuan.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * service进程加载webview
 * aidl通信
 */

public class LoginServiceActivity extends Activity {
    private static final String TAG = "LoginServiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setPackage(this.getPackageName());
        intent.setAction("com.chenjiajuan.webview");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    /**
     * 设置连接，获取service，绑定callback
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IWebViewService webViewService = IWebViewService.Stub.asInterface(service);
            if (webViewService == null)
                return;
            try {
                Log.e(TAG, "onServiceConnected......");
                webViewService.doLoadWebViewJsUrl(webViewCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 设置callback，处理数据
     */
    private IWebViewCallback webViewCallback = new IWebViewCallback.Stub() {

        /**
         * 获取到url，decode成二维码图片
         * @param url
         * @throws RemoteException
         */
        @Override
        public void showQRCode(String url) throws RemoteException {

        }

        /**
         * 扫码登录成功
         * @param userInfo
         * @throws RemoteException
         */

        @Override
        public void onQRLoginSuccess(String userInfo) throws RemoteException {

        }

        /**
         * 扫码登录失败
         * @param code
         * @param msg
         * @throws RemoteException
         */

        @Override
        public void onQRLoginFailure(int code, String msg) throws RemoteException {

        }

        /**
         * 扫码成功
         * @param code
         * @param msg
         * @throws RemoteException
         */

        @Override
        public void onQRScanCodeSuccess(int code, String msg) throws RemoteException {

        }

        /**
         * 刷新二维码
         * @param code
         * @param msg
         * @throws RemoteException
         */
        @Override
        public void onQRRefresh(int code, String msg) throws RemoteException {


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
