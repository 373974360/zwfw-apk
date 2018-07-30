package cn.firegod.study.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
    //    String url = "http://117.36.51.98:8888/dating/windowScreen.html";
    String url = "http://172.16.101.2:8888/dating/windowScreen.html";
//    String url = "http://weixin.com";
    WebView webView = null;
    static long lastReload = 0L;
//    Downloader downloader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.webview);
        webView = (WebView) viewById;
        webView.setSoundEffectsEnabled(true);
        WebSettings settings = webView.getSettings();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 设置无边框
        settings.setUseWideViewPort(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // web内容强制满屏
        settings.setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new Music(getApplication()), "Music");

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                Toast.makeText(getApplicationContext(), "网络可能不稳定，每10秒重试连接", Toast.LENGTH_SHORT).show();
                webView.loadUrl(url);
            }
        };
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                if ("".equals(view.getTitle()) || "wait".equals(view.getTitle()) || "Webpage not available".equals(view.getTitle())) {
                    lastReload = new Date().getTime();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 10000);
                } else {
                    handler.removeCallbacks(runnable);
                }
                if ("".equals(view.getTitle())) {
                    view.loadData("<html><head><title>wait</title></head><body style='background:#eee;'><h1 style='text-align:center;margin-top:20%;'>请等待网络连接...</h1></body></html>", "text/html;charset=utf-8", "utf-8");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        webView.loadUrl(url);

//        downloader = new Downloader(MainActivity.this);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url.endsWith(".apk")) {
                    /**
                     * 通过系统下载apk
                     */
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
//            Intent intent =  new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);

            try {
                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
                Class<?> statusBarManager = Class.forName
                        ("android.app.StatusBarManager");
                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
//                Method expand2 = statusBarManager.getMethod ("expandSettingsPanel");
                expand.invoke(service);
//                expand2.invoke (service);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        return false;
        return super.onKeyDown(keyCode, event);
    }
}