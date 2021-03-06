package com.imgod1.kangkang.huafei;

import android.app.Application;
import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * HuaFeiApplication.java
 *
 * @author imgod1
 * @version 2.0.0 2018/11/23 15:30
 * @update imgod1 2018/11/23 15:30
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class HuaFeiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //这里必须在super.onCreate方法之后，顺序不能变
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        initLibs();
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }

    //初始化一些库
    private void initLibs() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
