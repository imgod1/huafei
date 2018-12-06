package com.imgod1.kangkang.huafei;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.imgod1.kangkang.huafei.bean.AppBeanResponse;
import com.imgod1.kangkang.huafei.bean.AppBeanResponse.AppBean;
import com.imgod1.kangkang.huafei.utils.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swiperefresh;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    private void initData() {
        swiperefresh.setRefreshing(true);
        requestAppList();
    }

    private void initViews() {
        swiperefresh = findViewById(R.id.swiperefresh);
        recyclerview = findViewById(R.id.recyclerview);
        swiperefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAppList();
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        AppAdapter appAdapter = new AppAdapter(MainActivity.this, mAppBeanList);
        appAdapter.setOnItemClickListener(new AppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        recyclerview.setAdapter(appAdapter);
    }

    private List<AppBean> mAppBeanList = new ArrayList<>();

    private void requestAppList() {
        String url = API.BASE_URL + API.APP_LIST;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        swiperefresh.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "网络请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        swiperefresh.setRefreshing(false);
                        if (!TextUtils.isEmpty(response)) {
                            AppBeanResponse appBeanResponse = GsonUtils.getGson().fromJson(response, AppBeanResponse.class);
                            if (null != appBeanResponse) {
                                mAppBeanList.clear();
                                mAppBeanList.addAll(appBeanResponse.getData());
                                recyclerview.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
