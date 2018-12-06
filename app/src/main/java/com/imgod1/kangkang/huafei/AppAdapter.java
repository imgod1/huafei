package com.imgod1.kangkang.huafei;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imgod1.kangkang.huafei.bean.AppBeanResponse.AppBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;

/**
 * AppAdapter.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/11/23 15:42
 * @update imgod1 2018/11/23 15:42
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class AppAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<AppBean> mAppBeanList;

    public AppAdapter(Context context, List<AppBean> appBeanList) {
        mContext = context;
        mAppBeanList = appBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_app, null, false);
        return new AppBeanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        AppBean appBean = mAppBeanList.get(i);
        final int position = i;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        ((AppBeanViewHolder) viewHolder).tv_content.setText(appBean.getAppName());
        OkHttpUtils.get().url(API.BASE_URL + appBean.getAppLogo()).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Bitmap response, int id) {
                ((AppBeanViewHolder) viewHolder).iv_logo.setImageBitmap(response);
            }
        });


    }

    @Override
    public int getItemCount() {
        return null == mAppBeanList ? 0 : mAppBeanList.size();
    }

    public static class AppBeanViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_logo;
        public TextView tv_content;

        public AppBeanViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_logo = itemView.findViewById(R.id.iv_logo);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(int position);
    }
}
