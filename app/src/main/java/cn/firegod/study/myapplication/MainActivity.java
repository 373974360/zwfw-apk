package cn.firegod.study.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends Activity {


    String url = "http://zwfw.itl.gov.cn:8080/hall/pingjiaqi";

    WebView webView = null;
    static long lastReload = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//
//



        View viewById = findViewById(R.id.webview);
        webView = (WebView) viewById;
        webView.setSoundEffectsEnabled(true);
        WebSettings settings = webView.getSettings();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 设置无边框
        settings.setUseWideViewPort(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // web内容强制满屏
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);

        final Handler handler=new Handler();

        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                Toast.makeText(getApplicationContext(), "网络可能不稳定，每10秒重试连接", Toast.LENGTH_SHORT).show();
                webView.reload();
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
            public void onLoadResource(WebView view, String url) {
                if (url.indexOf("please_comment") != -1) {
                    // 调用系统默认浏览器处理url
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        view.evaluateJavascript("play_please_comment()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                if ("找不到网页".equals(view.getTitle())) {
                        lastReload = new Date().getTime();
                        handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 10000);

                }else{
                    handler.removeCallbacks(runnable);
                }
            }
        });

        webView.loadUrl(url);
//        final Context applicationContext = this.getApplicationContext();
//        boolean bFlag = false;
//        do {
//            bFlag = isWifiAvailable(applicationContext);
//            if (bFlag) {
//                Log.i("Wifi state - ", "connected");
//                webView.loadUrl(url);
//                Log.i("111", "Webview load url ");
//                return;
//            } else {
//                Log.i("Wifi state - ", "not connected");
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        while (!bFlag);

////        webView.setSystemUiVisibility(0);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//        Toast.makeText(getApplicationContext(), "qidongle", Toast.LENGTH_SHORT).show();

    }

    /**
     * 判断wifi连接状态
     *
     * @param ctx
     * @return
     */
    public boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return NetworkInfo.State.CONNECTED == wifi;
    }
}
