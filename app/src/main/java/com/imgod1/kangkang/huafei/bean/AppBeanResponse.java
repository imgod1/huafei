package com.imgod1.kangkang.huafei.bean;

import java.util.List;

/**
 * AppBeanResponse.java
 *
 * @author imgod1
 * @version 2.0.0 2018/11/23 15:43
 * @update imgod1 2018/11/23 15:43
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class AppBeanResponse {

    /**
     * data : [{"remark":"特殊标记","appName":"test","appLogo":"http://pic.qiantucdn.com/58pic/18/14/82/56F58PICahj_1024.jpg","apkUrl":"imtt.dd.qq.com/16891/DD9C70F0C3C361F6BA3483341FB58567.apk"}]
     * showMessage : 操作成功
     * status : 100
     */

    private String showMessage;
    private int status;
    private List<AppBean> data;

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AppBean> getData() {
        return data;
    }

    public void setData(List<AppBean> data) {
        this.data = data;
    }

    public static class AppBean {
        /**
         * remark : 特殊标记
         * appName : test
         * appLogo : http://pic.qiantucdn.com/58pic/18/14/82/56F58PICahj_1024.jpg
         * apkUrl : imtt.dd.qq.com/16891/DD9C70F0C3C361F6BA3483341FB58567.apk
         */

        private String remark;
        private String appName;
        private String appLogo;
        private String apkUrl;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppLogo() {
            return appLogo;
        }

        public void setAppLogo(String appLogo) {
            this.appLogo = appLogo;
        }

        public String getApkUrl() {
            return apkUrl;
        }

        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }
    }
}
