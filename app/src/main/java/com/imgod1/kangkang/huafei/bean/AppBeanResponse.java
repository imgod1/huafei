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
     * data : [{"remark":"test","appName":"邦邦小助手","appLogo":"bangbang_logo.png","apkUrl":"邦邦小助手1.6.apk","packageName":"test"},{"remark":"test","appName":"蜜蜂小助手","appLogo":"mifeng_icon.png","apkUrl":"蜜蜂小助手1.3.apk","packageName":"test"},{"remark":"test","appName":"玖玖小助手","appLogo":"jiujiushou_logo.png","apkUrl":"玖玖小助手1.0.apk","packageName":"test"}]
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
         * remark : test
         * appName : 邦邦小助手
         * appLogo : bangbang_logo.png
         * apkUrl : 邦邦小助手1.6.apk
         * packageName : test
         */

        private String remark;
        private String appName;
        private String appLogo;
        private String apkUrl;
        private String packageName;

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

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }
}
