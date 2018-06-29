// IWebViewCallback.aidl
package com.chenjiajuan.service;

// Declare any non-default types here with import statements

interface IWebViewCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void showQRCode(in String url);

      void onQRLoginSuccess(in String userInfo);

      void onQRLoginFailure(in int code, in String msg);


      void onQRScanCodeSuccess(in int code, in String msg);


      void onQRRefresh(in int code, in String msg);

}
