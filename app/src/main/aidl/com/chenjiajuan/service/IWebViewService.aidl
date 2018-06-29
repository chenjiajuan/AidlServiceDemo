// IWebViewService.aidl
package com.chenjiajuan.service;
import com.chenjiajuan.service.IWebViewCallback;

// Declare any non-default types here with import statements

interface IWebViewService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void doLoadWebViewJsUrl(in IWebViewCallback webViewCallback);
}
