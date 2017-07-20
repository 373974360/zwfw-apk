package cn.firegod.study.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;

public class Cookie {

    Context mContext;

    /** Instantiate the interface and set the context */
    Cookie(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void setCookie(String url,String name,String value) {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, name + "=" + value);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @JavascriptInterface
    public String getCookie(String url) {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);
        return cookie;
    }
}
