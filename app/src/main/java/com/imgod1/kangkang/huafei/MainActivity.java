package com.imgod1.kangkang.huafei;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.imgod1.kangkang.huafei.bean.AppBeanResponse;
import com.imgod1.kangkang.huafei.bean.AppBeanResponse.AppBean;
import com.imgod1.kangkang.huafei.utils.GsonUtils;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
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
                dispatchItemClick(position);
            }
        });

        recyclerview.setAdapter(appAdapter);
    }

    private static final String TAG = "MainActivity";
    private int actionPosition = 0;

    //列表的点击事件
    //1.判断有没有安装
    //2.如果安装 那就打开 没有安装的话进行安装并打开
    private void dispatchItemClick(int position) {
        actionPosition = position;
        AppBean appBean = mAppBeanList.get(position);
        if (checkPackageIsInstall(appBean.getPackageName())) {//已经安装了
            Intent intent = getLaunchehIntent(appBean.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//经测试 不用这个flag也行
            startActivity(intent);
        } else {//没有安装
            //判断是否下载到本地过
            File apkFile = new File(getCacheDir(), appBean.getApkUrl());
            if (apkFile.exists()) {
                //如果文件存在 那就执行安装的逻辑
                try {
                    int result = PluginManager.getInstance().installPackage(apkFile.getAbsolutePath(), 0);
                    Log.e(TAG, "result code:" + result);
                    switch (result) {
                        case PackageManagerCompat.INSTALL_SUCCEEDED:
                            Toast.makeText(getApplicationContext(), "应用安装成功", Toast.LENGTH_SHORT).show();
                            openPlugin(appBean.getPackageName());
                            break;
                        case PackageManagerCompat.INSTALL_FAILED_ALREADY_EXISTS:
                            Toast.makeText(getApplicationContext(), "应用已经安装,无需重复安装", Toast.LENGTH_SHORT).show();
                            openPlugin(appBean.getPackageName());
                            break;
                        case PackageManagerCompat.INSTALL_FAILED_INVALID_APK:
                            Toast.makeText(getApplicationContext(), "无效的安装包文件", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "插件安装出错,请重新尝试", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "插件安装出错", Toast.LENGTH_SHORT).show();
                }
            } else {//文件不存在 那就执行下载的逻辑
                showProgressDialog();
                downloadApk(API.BASE_URL + appBean.getApkUrl(), new FileCallBack(getCacheDir().getAbsolutePath(), appBean.getApkUrl()) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mProgressDialog.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getApplicationContext(), "文件下载出错", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        hideProgressDialog();
                        dispatchItemClick(actionPosition);
                    }
                });
            }

        }
    }

    ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        hideProgressDialog();
        mProgressDialog = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }


    private void downloadApk(String apkNetUrl, FileCallBack fileCallBack) {
        OkHttpUtils//
                .get()//
                .url(apkNetUrl)//
                .build()//
                .execute(fileCallBack);
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


    /**
     * 打开插件
     */
    private void openPlugin(String packageName) {
        Intent intent = getLaunchehIntent(packageName);
        if (null != intent) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//经测试 不用这个flag也行
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "插件还未安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拿到Intent
     *
     * @return
     */
    private Intent getLaunchehIntent(String packageName) {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return intent;
    }


    private boolean checkPackageIsInstall(String packageName) {
        try {
            List<PackageInfo> packageList = PluginManager.getInstance().getInstalledPackages(0);
            if (null != packageList) {
                Log.e("test", "installed package:" + packageList.size());
                for (PackageInfo packageInfo : packageList) {
                    Log.e("test_package", packageInfo.packageName);
                    if (packageName.equals(packageInfo.packageName)) {
                        return true;
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
