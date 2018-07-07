package com.chenjiajuan.service;


/**
 * Created by chenjiajuan on 2018/6/27.
 */

public interface QRTaskListener {

    /**
     * 手机端确认成功
     *
     * @param userInfo
     */
    void onQRLoginSuccess(String userInfo);

    /**
     * 登录失败
     *
     * @param code
     * @param msg
     */

    void onQRLoginFailure(int code, String msg);

    /**
     * 扫码成功
     *
     * @param code
     * @param msg
     */

    void onQRScanCodeSuccess(int code, String msg);

    /**
     * 刷新二维码
     *
     * @param code
     * @param msg
     */

    void onQRRefresh(int code, String msg);


}
