package cn.firegod.study.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends Activity {
    String url = "http://117.36.51.98:8888/dating/comment.html";
    WebView webView = null;
    static long lastReload = 0L;

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

//绑定按钮的事件
//        Button button = findViewById(R.id.btn_refresh);
//        button.setOnClickListener(v -> {
//            webView.clearCache(true);
//            ((WebView) webView).reload();
//        });


//        findViewById(R.id.btn_switch).setOnClickListener(v->{
//            Intent intent = new Intent(this,LianhuWindowDisplayActivity.class);
//            startActivity(intent);
//        });
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

//    /**
//     * 判断wifi连接状态
//     *
//     * @param ctx
//     * @return
//     */
//    public boolean isWifiAvailable(Context ctx) {
//        ConnectivityManager conMan = (ConnectivityManager) ctx
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//                .getState();
//        return NetworkInfo.State.CONNECTED == wifi;
//    }
}
