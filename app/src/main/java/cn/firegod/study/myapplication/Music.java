package cn.firegod.study.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.webkit.JavascriptInterface;

public class Music{

    Context mContext;

    /** Instantiate the interface and set the context */
    Music(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void playMusic(String song) {
        MediaPlayer mp1 = MediaPlayer.create(mContext, R.raw.welcom);
        MediaPlayer mp2 = MediaPlayer.create(mContext, R.raw.pingjia);
        switch (song) {
            case "welcom":
                mp1.start();
                break;
            case "comment":
                mp2.start();
                break;
        }
    }
}
