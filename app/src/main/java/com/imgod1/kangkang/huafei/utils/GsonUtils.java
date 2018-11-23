package com.imgod1.kangkang.huafei.utils;

import com.google.gson.Gson;

/**
 * GsonUtils.java
 *
 * @author imgod1
 * @version 2.0.0 2018/11/23 16:40
 * @update imgod1 2018/11/23 16:40
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class GsonUtils {
    private static Gson mGson;

    public static Gson getGson() {
        if (null == mGson) {
            mGson = new Gson();
        }
        return mGson;
    }
}
