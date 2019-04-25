package cn.firegod.study.myapplication;

import android.content.Context;
import android.webkit.JavascriptInterface;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import static android.content.Context.MODE_PRIVATE;

public class AndroidFileInterface {

    Context mContext;

    AndroidFileInterface(Context c) {
        mContext = c;
    }

    //文件名称
    String fileName = "windowList.txt";

    //向指定的文件中写入指定的数据
    @JavascriptInterface
    public void writeFileData(String content) {

        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, MODE_PRIVATE);//获得FileOutputStream

            //将要写入的字符串转换为byte数组

            byte[] bytes = content.getBytes();

            fos.write(bytes);//将byte数组写入文件

            fos.close();//关闭文件输出流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    //打开指定文件，读取其数据，返回字符串对象
    public String readFileData() {

        String result = "";

        try {

            FileInputStream fis = mContext.openFileInput(fileName);

            //获取文件长度
            int lenght = fis.available();

            byte[] buffer = new byte[lenght];

            fis.read(buffer);

            //将byte数组转换成指定格式的字符串
            result = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
