package com.jude.rollviewpagerdome.module.main.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpagerdome.BuildConfig;
import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.module.main.model.ResourceModule;
import com.jude.rollviewpagerdome.module.version.module.VersionModule;
import com.jude.rollviewpagerdome.module.network.AdConstanst;
import com.jude.rollviewpagerdome.module.network.ApiKey;
import com.jude.rollviewpagerdome.module.network.JsonParser;
import com.jude.rollviewpagerdome.module.version.view.UpdateService;
import com.jude.rollviewpagerdome.utils.FileUtil;
import com.jude.rollviewpagerdome.utils.LogUtil;
import com.jude.rollviewpagerdome.utils.SpManager;
import com.jude.rollviewpagerdome.utils.SystemUtil;
import com.jude.rollviewpagerdome.utils.UnicodeUtil;
import com.jude.rollviewpagerdome.widget.ClearCacheDialog;
import com.jude.rollviewpagerdome.widget.LoadingDialog;
import com.jude.rollviewpagerdome.widget.MenuDialog;
import com.jude.rollviewpagerdome.widget.SetHostDialog;
import com.jude.rollviewpagerdome.widget.ToastDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by garry on 17/4/30.
 */

public class HomeActivity extends AppCompatActivity {
    public static final String WEBVIEW_CACHE_DIRNAME = "Webview_cache";

    public static final int MSG_WHAT_HIDE_LOADING = 1;//隐藏加载圈圈
    public static final int MSG_WHAT_SHOW_LOADING = 2;//显示加载圈圈
    public static final int MSG_WHAT_CLEAR_CACHE = 3; //清除菜单缓存
    public static final int MSG_WHAT_LOAD_HTML_RESOURCES = 4; //加载Html菜单
    public static final int MSG_WHAT_CACHE_HTML_RESOURCES = 5;//缓存html菜单
    public static final int MSG_WHAT_LOAD_IMAGE_RESOURCES = 6;//加载Photo菜单,并缓存
    public static final int MSG_WHAT_LOAD_IMAGES_RESOURCES = 7;//显示菜单
    public static final int MSG_WHAT_SHOW_TOAST = 8;//显示提示
    public static final int MSG_WHAT_SHOW_TOAST_DIALOG = 9;//显示提示框
    public static final int MSG_WHAT_LOAD_ERROR_PAGE = 10;//加载错误页,用于调试htmlUrl
    public static final int MSG_WHAT_SHOW_CACHE = 11;//显示缓存菜单
    public static final int MSG_WHAT_SHOW_MENU = 12;//显示菜单

    private OkHttpClient mOkClient;
    private ImageView mPhotoView;//展示图片视图
    private WebView mShowWebView;//展示Html视图
    private WebView mCacheWebView;//缓存Html视图
    private RollPagerView mPhotoLoopView;//展示轮询图片视图
    private ResourceModule mResourceModule; //菜单数据类，保存类似update_TS的信息
    private VersionModule mVersionModule;   //App版本数据类
    private UpdateMenuRunnable mUpdateMenuRunnable; //自动更新菜单任务
    private LoadingDialog mLoadingDialog; //加载圈圈
    private SetHostDialog mSetHostDialog; //设置Host界面
    private ClearCacheDialog mClearCacheDialog; //设置Host界面
    private ToastDialog mToastDialog;     //显示错误JSON界面
    private MenuDialog mMenuDialog;
    private ImageLoopAdapter mLoopAdapter;

    private List<String> mHtmlUrlList; //html菜单列表
    private int mCurrentHtmlIndex;     //当前html下标，用于缓存菜单
    private Handler mHandler;
    private boolean mUpdating;//标志是否正在更新菜单

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        initView();
        checkUpdate();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(mUpdateMenuRunnable).start();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateMenuRunnable.isStop = true;
        mUpdateMenuRunnable = null;
        hideLoadingDialog();
        hideSetHostDialog();
        hideToastDialog();
        hideMenuDialog();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
        if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            sendShowToastMessage("手动更新资源");
            updateMenu();
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            showSetHostDialog();
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            showClearCacheDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        showMenuDialog();
    }

    private void initData() {
        mOkClient = new OkHttpClient();
        mResourceModule = new ResourceModule(this);
        mVersionModule = new VersionModule(this);
        mUpdateMenuRunnable = new UpdateMenuRunnable();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_HIDE_LOADING:
                        hideLoadingDialog();
                        break;
                    case MSG_WHAT_SHOW_LOADING:
                        showLoadingDialog();
                        break;
                    case MSG_WHAT_CLEAR_CACHE:
                        clearCache();
                        break;
                    case MSG_WHAT_LOAD_HTML_RESOURCES:
                        String[] htmlValue = ((String) msg.obj).split(",");
                        String htmlWeekday = htmlValue[0];
                        String htmlUrl = htmlValue[1];
                        loadHtmlResources(htmlWeekday, htmlUrl);
                        break;
                    case MSG_WHAT_CACHE_HTML_RESOURCES:
                        cacheHtmlResources();
                        break;
                    case MSG_WHAT_LOAD_IMAGE_RESOURCES:
                        String[] imageValue = ((String) msg.obj).split(",");
                        String imageWeekday = imageValue[0];
                        String imageUrl = imageValue[1];
                        showAndCacheImageResources(imageWeekday, imageUrl);
                        break;
                    case MSG_WHAT_LOAD_IMAGES_RESOURCES:
                        List<String> imageUrls = (List<String>) msg.obj;
                        showAndCacheImagesResources(imageUrls);
                        break;
                    case MSG_WHAT_SHOW_CACHE:
                        showCacheRes();
                        break;
                    case MSG_WHAT_SHOW_TOAST:
                        showToast((String) msg.obj);
                        break;
                    case MSG_WHAT_SHOW_TOAST_DIALOG:
                        showToastDialog((String) msg.obj);
                        break;
                    case MSG_WHAT_LOAD_ERROR_PAGE:
                        String errorUrl = (String) msg.obj;
                        loadErrorPage(errorUrl);
                        break;
                    case MSG_WHAT_SHOW_MENU:
                        showMenuDialog();
                        break;
                }
            }
        };
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mPhotoView = (ImageView) findViewById(R.id.iv_photo);

        String cacheDirPath = getFilesDir().getAbsolutePath()
                + WEBVIEW_CACHE_DIRNAME;
        LogUtil.i("cachePath=" + cacheDirPath);
        //初始化显示WebView
        mShowWebView = (WebView) findViewById(R.id.wb_show);
        mShowWebView.getSettings().setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        mShowWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
//        mShowWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
//        mShowWebView.getSettings().setDatabaseEnabled(true);
        // 设置Application caches缓存目录
//        mShowWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
//        mShowWebView.getSettings().setAppCacheEnabled(true);
        //初始化缓存WebView
        mCacheWebView = (WebView) findViewById(R.id.wb_cache);
        mCacheWebView.getSettings().setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        mCacheWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
//        mCacheWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
//        mCacheWebView.getSettings().setDatabaseEnabled(true);
        // 设置Application caches缓存目录
//        mCacheWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
//        mCacheWebView.getSettings().setAppCacheEnabled(true);
        mCacheWebView.setWebChromeClient(new MyWebChromeClient());

        mPhotoLoopView = (RollPagerView) findViewById(R.id.iv_looper);

        mLoadingDialog = new LoadingDialog(HomeActivity.this);
        mSetHostDialog = new SetHostDialog(HomeActivity.this);
        mClearCacheDialog = new ClearCacheDialog(HomeActivity.this);
        mMenuDialog = new MenuDialog(HomeActivity.this);
        mMenuDialog.setListener(new MenuDialog.OnMenuClickListener() {
            @Override
            public void refreshClicked() {
                sendShowToastMessage("手动更新资源");
                updateMenu();
            }

            @Override
            public void clearCacheClicked() {
                showClearCacheDialog();
            }

            @Override
            public void exitAppClicked() {
                finish();
            }

            @Override
            public void setHostClicked() {
                showSetHostDialog();
            }
        });
    }

    /**
     * 更新菜单信息
     */
    private void updateMenu() {
        if (!mUpdating) {
            mUpdating = true;
            if (SystemUtil.isNetworkAvailable(getApplicationContext())) {
                showOnLineRes();
            } else {
                showCacheRes();
            }
        }
    }

    /*********************************  Handler相关方法  START *************************************/
    private void sendHideLoadingDialogMessage() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_WHAT_HIDE_LOADING);
    }

    private void sendShowLoadingDialogMessage() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_WHAT_SHOW_LOADING);
    }

    private void sendClearCacheMessage() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_WHAT_CLEAR_CACHE);
    }

    private void sendLoadHtmlResourceMessage(String weekday, String htmlUrl) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_WHAT_LOAD_HTML_RESOURCES;
            message.obj = weekday + "," + htmlUrl;
            mHandler.sendMessage(message);
        }
    }

    private void sendLoadImageResourceMessage(String weekday, String htmlUrl) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_WHAT_LOAD_IMAGE_RESOURCES;
            message.obj = weekday + "," + htmlUrl;
            mHandler.sendMessage(message);
        }
    }

    private void sendLoadImagesResourceMessage(String weekday, List<String> imageUrls) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_WHAT_LOAD_IMAGES_RESOURCES;
            List<String> weekdayAndurls = new ArrayList<>();
            //List首位为Weekday
            weekdayAndurls.add(weekday);
            weekdayAndurls.addAll(imageUrls);
            message.obj = weekdayAndurls;
            mHandler.sendMessage(message);
        }
    }

    private void sendCacheHtmlResourcesMessage() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_WHAT_CACHE_HTML_RESOURCES);
    }

    private void sendShowToastMessage(String msg) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_WHAT_SHOW_TOAST;
            message.obj = msg;
            mHandler.sendMessage(message);
        }
    }

    private void sendShowCacheMessage() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(MSG_WHAT_SHOW_CACHE);
    }

    /**
     * 通知主线程显示Json界面
     *
     * @param msg
     */
    private void sendShowToastDialogMessage(String msg) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_WHAT_SHOW_TOAST_DIALOG;
            message.obj = msg;
            mHandler.sendMessage(message);
        }
    }

    /*********************************  Handler相关方法  END *************************************/

    /**
     * 显示加载圆圈
     */
    private void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    /**
     * 隐藏加载圆圈
     */
    private void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.hide();
    }

    /**
     * 显示设置Host界面
     */
    private void showSetHostDialog() {
        if (mSetHostDialog != null && !mSetHostDialog.isShowing())
            mSetHostDialog.show();
    }

    /**
     * 隐藏设置Host界面
     */
    private void hideSetHostDialog() {
        if (mSetHostDialog != null && mSetHostDialog.isShowing())
            mSetHostDialog.dismiss();
    }

    /**
     * 设置清除菜单缓存界面
     */
    private void showClearCacheDialog() {
        if (mClearCacheDialog != null && !mClearCacheDialog.isShowing())
            mClearCacheDialog.show();
    }

    /**
     * 设置清除菜单缓存界面
     */
    private void hideClearCacheDialog() {
        if (mClearCacheDialog != null && mClearCacheDialog.isShowing())
            mClearCacheDialog.dismiss();
    }

    /**
     * 展示菜单
     */
    private void showMenuDialog() {
        if (mMenuDialog != null && !mMenuDialog.isShowing()) {
            mMenuDialog.show();
        }
    }

    private void hideMenuDialog() {
        if (mMenuDialog != null && mMenuDialog.isShowing()) {
            mMenuDialog.dismiss();
        }
    }

    /**
     * 显示错误Json界面
     *
     * @param toast
     */
    private void showToastDialog(String toast) {
        if (mToastDialog == null)
            mToastDialog = new ToastDialog(HomeActivity.this);

        if (!mToastDialog.isShowing()) {
            mToastDialog.setToast(toast);
            mToastDialog.show();
        }
    }

    private void hideToastDialog() {
        if (mToastDialog != null && mToastDialog.isShowing())
            mToastDialog.dismiss();
    }

    /**
     * 提示
     *
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 缓存Html菜单
     */
    private void cacheHtmlResources() {
        if (mCacheWebView != null && mHtmlUrlList != null && mHtmlUrlList.size() > 0) {
            mCacheWebView.loadUrl(mHtmlUrlList.get(0));
        }
    }

    /**
     * 显示Html菜单
     *
     * @param weekday 只有当天的菜单才会加载显示
     * @param htmlUrl
     */
    private void loadHtmlResources(String weekday, String htmlUrl) {
        if (SystemUtil.getCurrentWeedDay().equals(weekday)) {
            if (mHtmlUrlList != null)
                mHtmlUrlList.remove(htmlUrl);
            mShowWebView.setVisibility(View.VISIBLE);
            mPhotoView.setVisibility(View.GONE);
            mPhotoLoopView.setVisibility(View.GONE);
            mShowWebView.loadUrl(htmlUrl);
        }
    }

    /**
     * 显示错误的菜单地址,用于调试
     *
     * @param errorHtmlUrl
     */
    private void loadErrorPage(String errorHtmlUrl) {
        mShowWebView.setVisibility(View.VISIBLE);
        mPhotoView.setVisibility(View.GONE);
        mShowWebView.loadUrl(errorHtmlUrl);
    }

    /**
     * 清除WebView缓存
     * 每次更新菜单列表时调用
     * 必须在主线程执行
     */
    private void clearCache() {
        mCacheWebView.clearCache(true);
        mShowWebView.clearCache(true);
        Glide.get(HomeActivity.this).clearMemory();
    }

    /**
     * 加载、缓存图片
     *
     * @param weekday  如果是当天的图片，会直接加载显示，否则，会缓存下来
     * @param photoUrl
     */
    private void showAndCacheImageResources(String weekday, String photoUrl) {
        if (SystemUtil.getCurrentWeedDay().equals(weekday)) {
            mPhotoView.setVisibility(View.VISIBLE);
            mShowWebView.setVisibility(View.GONE);
            mPhotoLoopView.setVisibility(View.GONE);
            //网络加载图片
            Glide.with(HomeActivity.this)
                    .load(photoUrl)
                    .thumbnail(0.1f)
                    .into(mPhotoView);
        } else {
            //缓存图片
            Glide.with(HomeActivity.this)
                    .load(photoUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        }
    }

    private void showAndCacheImagesResources(List<String> photoUrls) {
        LogUtil.i("photoUrls=" + photoUrls);
        if (photoUrls != null && photoUrls.size() > 0) {
            String weekday = photoUrls.get(0);
            if (SystemUtil.getCurrentWeedDay().equals(weekday)) {
                mPhotoLoopView.setVisibility(View.GONE);
                mShowWebView.setVisibility(View.GONE);
                mPhotoLoopView.setVisibility(View.VISIBLE);
                List<String> urls = photoUrls.subList(1, photoUrls.size());
                mLoopAdapter = new ImageLoopAdapter(mPhotoLoopView, urls);
                mPhotoLoopView.setAdapter(mLoopAdapter);
            } else {
                //缓存图片
                for (int i = 1; i < photoUrls.size(); i++) {
                    String photoUrl = photoUrls.get(i);
                    Glide.with(HomeActivity.this)
                            .load(photoUrl)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                }
            }
        }
    }

    /**
     * 展示在线资源
     */
    private void showOnLineRes() {
        sendShowLoadingDialogMessage();
        try {
            final Request request = new Request.Builder()
                    .url(mResourceModule.getHostAddress() + AdConstanst.ACTION_URL_GET_RESOURCES + "?" + ApiKey.COMMON_BOXCODE + "=" + SystemUtil.getAndroidId(HomeActivity.this))
//                    .url(mResourceModule.getHostAddress() + AdConstanst.ACTION_URL_GET_RESOURCES + "?" + ApiKey.COMMON_BOXCODE + "=" + "985a04a7e1138800")
//                    .url(mResourceModule.getHostAddress() + AdConstanst.ACTION_URL_GET_RESOURCES + "?" + ApiKey.COMMON_BOXCODE + "=" + "a1˛eeeac74cd75b69")
                    .addHeader("content-type", "application/json;charset:utf-8")
                    .get()
                    .build();
            Call call = mOkClient.newCall(request);
            LogUtil.i("url=" + request.url().toString());
            LogUtil.i("androidId=" + SystemUtil.getAndroidId(HomeActivity.this));
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    LogUtil.i("error:" + e.getMessage());
                    sendShowToastMessage("获取菜单失败，失败原因:" + e.getMessage());
                    sendHideLoadingDialogMessage();
                    mUpdating = false;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    sendHideLoadingDialogMessage();
                    try {
                        final String content = response.body().string();
                        LogUtil.i("raw data:" + content);
                        JSONObject jsonObject = new JSONObject(content);
                        Map<String, Object> result = JsonParser.parseObject(jsonObject);
                        String updateTs = (String) result.get(ApiKey.GET_RES_UPDATE_TS);
                        LogUtil.i("updateTs=" + updateTs);
                        if (mResourceModule.getUpdateTs().equals(updateTs)) {
                            sendShowCacheMessage();
                            mUpdating = false;
                            return;
                        }
                        mResourceModule.setUpdateTs(updateTs);
                        List<Map<String, Object>> resList = (ArrayList) result.get(ApiKey.GET_RES_RESOURCES);
                        LogUtil.i("result = " + result);
                        if (resList != null && resList.size() > 0) {
                            boolean isTodayHasMenu = false;//标识当天是否有菜单
                            sendClearCacheMessage();
                            Glide.get(HomeActivity.this).clearDiskCache();
                            for (int i = 0; i < resList.size(); i++) {
                                //解析路径，并加载图片
                                Map<String, Object> resMap = resList.get(i);
                                String type = (String) resMap.get(ApiKey.COMMON_TYPE);
                                String weekday = (String) resMap.get(ApiKey.GRT_RES_WEEKDAY);
                                if ("image".equals(type)) {
                                    final String photoUrl = (String) resMap.get(ApiKey.COMMON_URL);
                                    LogUtil.i("photoUrl=" + photoUrl);
                                    sendLoadImageResourceMessage(weekday, photoUrl);
                                } else if ("images".equals(type)) {
//                                    List<String> photoUrls = new ArrayList<>();
//                                    photoUrls.add("http://img.taopic.com/uploads/allimg/110915/29-11091512035335.jpg");
//                                    photoUrls.add("http://img01.taopic.com/170601/240439-1F601120P286.jpg");
//                                    photoUrls.add("http://img01.taopic.com/170601/240439-1F6010IP212.jpg");
                                    List<String> photoUrls = (ArrayList) resMap.get(ApiKey.COMMON_URL);
                                    LogUtil.i("imagesUrl=" + photoUrls);
                                    mPhotoLoopView.setPlayDelay((int) resMap.get(ApiKey.GET_RES_INTERVAL) * 1000);
//                                    mPhotoLoopView.setPlayDelay(3 * 1000);
                                    sendLoadImagesResourceMessage(weekday, photoUrls);
                                } else {
                                    String rid = (String) resMap.get(ApiKey.GET_RES_RID);
                                    String htmlUrl = resMap.get(ApiKey.COMMON_URL) + "?" + ApiKey.GET_RES_RID + "=" + rid;
                                    if (mHtmlUrlList == null)
                                        mHtmlUrlList = new ArrayList<>();
                                    mHtmlUrlList.add(htmlUrl);
                                    LogUtil.i("mHtmlUrlList=" + mHtmlUrlList);
                                    sendLoadHtmlResourceMessage(weekday, htmlUrl);
                                }
                                if (SystemUtil.getCurrentWeedDay().equals(weekday))
                                    isTodayHasMenu = true;
                            }
                            mUpdating = false;
                            sendCacheHtmlResourcesMessage();
                            //将JSON写入SP
                            if (mResourceModule != null)
                                mResourceModule.setResourcesJson(content);
                            if (!isTodayHasMenu)
                                sendShowToastDialogMessage("今天没有设置菜单!!JSON格式为：" + UnicodeUtil.readUnicodeStr2(content.toString()));
                        } else {
                            LogUtil.w("菜单列列表为空");
                            sendShowToastMessage("菜单列表为空");
                            mUpdating = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mUpdating = false;
                    }
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
            sendShowToastMessage(mResourceModule.getHostAddress() + AdConstanst.ACTION_URL_GET_RESOURCES + "?" + ApiKey.COMMON_BOXCODE + "=" + SystemUtil.getAndroidId(HomeActivity.this));
            mUpdating = false;
        }
    }

    /**
     * 展示缓存资源
     */
    private void showCacheRes() {
        LogUtil.i("展示缓存资源");
        //获取缓存数据
        final String resJson = mResourceModule.getResourcesJson();
        if (resJson != null && !"".equals(resJson)) {
            try {
                JSONObject jsonObject = new JSONObject(resJson);
                Map<String, Object> result = JsonParser.parseObject(jsonObject);
                List<Map<String, Object>> resList = (ArrayList) result.get(ApiKey.GET_RES_RESOURCES);
                LogUtil.i("resList = " + resList);
                if (resList != null && resList.size() > 0) {
                    boolean isTodayHasMenu = false;
                    for (int i = 0; i < resList.size(); i++) {
                        //解析路径，并加载图片
                        Map<String, Object> resMap = resList.get(i);
                        String type = (String) resMap.get(ApiKey.COMMON_TYPE);
                        final String weekday = (String) resMap.get(ApiKey.GRT_RES_WEEKDAY);
                        if (SystemUtil.getCurrentWeedDay().equals(weekday)) {
                            isTodayHasMenu = true;
                            if ("image".equals(type)) {
                                String photoUrl = (String) resMap.get(ApiKey.COMMON_URL);
                                LogUtil.i("cache photoUrl=" + photoUrl);
                                sendLoadImageResourceMessage(weekday, photoUrl);
                            } else if ("images".equals(type)) {
//                                List<String> photoUrls = new ArrayList<>();
//                                photoUrls.add("http://img.taopic.com/uploads/allimg/110915/29-11091512035335.jpg");
//                                photoUrls.add("http://img01.taopic.com/170601/240439-1F601120P286.jpg");
//                                photoUrls.add("http://img01.taopic.com/170601/240439-1F6010IP212.jpg");
                                List<String> photoUrls = (List<String>) resMap.get(ApiKey.COMMON_URL);
                                LogUtil.i("cache photoUrls=" + photoUrls);
                                int interval = (int) resMap.get(ApiKey.GET_RES_INTERVAL);
//                                int interval = 10;
                                if (mPhotoLoopView != null)
                                    mPhotoLoopView.setPlayDelay(interval * 1000);
                                sendLoadImagesResourceMessage(weekday, photoUrls);
                            } else {
                                String rid = (String) resMap.get(ApiKey.GET_RES_RID);
                                String htmlUrl = resMap.get(ApiKey.COMMON_URL) + "?" + ApiKey.GET_RES_RID + "=" + rid;
                                LogUtil.i("cache htmlUrl=" + htmlUrl);
                                sendLoadHtmlResourceMessage(weekday, htmlUrl);
                                mUpdating = false;
                            }
                            return;
                        }
                    }

                    if (!isTodayHasMenu)
                        sendShowToastDialogMessage("今天没有菜式:" + "\n" + "json=" + UnicodeUtil.readUnicodeStr2(resJson.toString()));
                } else {
                    LogUtil.w("无ResJson缓存");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mUpdating = false;
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        if (SystemUtil.isNetworkAvailable(getApplicationContext())) {
            try {
                final Request request = new Request.Builder()
                        .url(mResourceModule.getHostAddress() + AdConstanst.ACTION_URL_FETCH_THE_NEWEST_VERSION)
                        .addHeader("content-type", "application/json;charset:utf-8")
                        .get()
                        .build();
                Call call = mOkClient.newCall(request);
                LogUtil.i("url=" + request.url().toString());
                call.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String content = response.body().string();
                            LogUtil.i("raw data:" + content);
                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(content);
                            Map<String, Object> result = JsonParser.parseObject(jsonObject);
                            LogUtil.i("result = " + result);
                            String ok = (String) result.get(ApiKey.COMMON_OK);
                            if ("yes".equals(ok)) {
                                List<Map<String, Object>> apk = (List<Map<String, Object>>) result.get(ApiKey.GET_APK_APK);
                                LogUtil.i("a" +
                                        "pk = " + apk);
                                if (apk != null && apk.size() > 0) {
                                    String title = (String) apk.get(0).get(ApiKey.COMMON_TITLE);
                                    String titleOld = mVersionModule.getTitle();
                                    LogUtil.i("title = " + title);
                                    LogUtil.i("titleOld = " + titleOld);
//                                String status = (String) apk.get(0).get(ApiKey.COMMON_STATUS);
//                                if ("启用".equals(status)) {
                                    if ("".equals(titleOld)) {
                                        LogUtil.i("首次保存title");
                                        mVersionModule.setTitle(title);
                                        return;
                                    }
                                    if (!titleOld.equals(title)) {
                                        mUpdateMenuRunnable.isStop = true;
                                        mVersionModule.setTitle(title);
                                        String apkUrl = (String) apk.get(0).get(ApiKey.COMMON_URL);
                                        LogUtil.i("apkUrl=" + apkUrl);
                                        //下载App
                                        Intent downloadIntent = new Intent(HomeActivity.this, UpdateService.class);
                                        downloadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        downloadIntent.putExtra(UpdateService.KEY_APP_NAME, title);
                                        downloadIntent.putExtra(UpdateService.KEY_APP_URL, apkUrl);
                                        HomeActivity.this.startService(downloadIntent);
//                                    }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
                sendShowToastMessage(e.toString());
            }
        }
    }

    /**
     * 清除WebView缓存
     */
    private void clearWebViewCache() {
        // 清理WebView缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // WebView缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + WEBVIEW_CACHE_DIRNAME);
        LogUtil.e("appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "webviewCache");
        LogUtil.e("appCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webView缓存目录
        if (webviewCacheDir.exists()) {
            FileUtil.deleteDirectory(getCacheDir().getAbsolutePath());
        }
        // 删除webView缓存，缓存目录
        if (appCacheDir.exists()) {
            deleteFile(getFilesDir().getAbsolutePath());
        }
    }


    class UpdateMenuRunnable implements Runnable {
        public static final int REFREASH_TIME = 1000 * 60 * 5;
        //        public static final int REFREASH_TIME = 1000 * 10;
        public boolean isStop = false;

        @Override
        public void run() {
            while (!isStop) {
                sendShowToastMessage("自动更新资源");
                updateMenu();
                try {
                    Thread.sleep(REFREASH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                //缓存下一个html文件
                sendShowToastMessage("缓存第" + mCurrentHtmlIndex + "个体资源");
                LogUtil.i("缓存第" + mCurrentHtmlIndex + "个体资源");
                if (mCurrentHtmlIndex < mHtmlUrlList.size() - 1) {
                    mCurrentHtmlIndex++;
                    mCacheWebView.loadUrl(mHtmlUrlList.get(mCurrentHtmlIndex));
                }
            }
        }
    }

    public class ImageLoopAdapter extends LoopPagerAdapter {
        public List<String> mImageUrls;

        public ImageLoopAdapter(RollPagerView viewPager, List imageUrls) {
            super(viewPager);
            mImageUrls = imageUrls;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            if (mImageUrls != null && mImageUrls.size() > 0) {
                ImageView view = new ImageView(container.getContext());
                view.setBackgroundColor(Color.parseColor("#000000"));
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(HomeActivity.this).load(mImageUrls.get(position)).into(view);
                return view;
            } else {
                return null;
            }
        }

        @Override
        public int getRealCount() {
            return mImageUrls == null ? 0 : mImageUrls.size();
        }
    }
}
