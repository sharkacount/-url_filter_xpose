package utils;


import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    public static void log(String str) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("log", str)
                .build();
        Request request = new Request.Builder()
                .url(StaticData.logURL)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String downloadJSONByOkHttp(String url) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                    .build();
            MultipartBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("package_name", "tv.danmaku.bili") // temp
                    .build();
            Request request = new Request.Builder()
                    .url(StaticData.sinkByPackageNameURL)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            result = (Map<String, Map<String, Object>>) JSON.parse(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    // JSON字符串下载
    public static String downloadJSON(String path)  {
        URL url = null;
        InputStream input = null;
        HttpURLConnection conn = null;
        byte data[] = null;
        String output = "";
        try {
            url = new URL(path);// 1.将网址封装到URL中
            conn = (HttpURLConnection) url.openConnection();// 2.打开http连接
            conn.connect();// 3.开始连接
            input = conn.getInputStream();// 4.获取网络输入流，用于读取要下载内容
            ByteArrayOutputStream bos = new ByteArrayOutputStream();// 5.获取字节转换流
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            data = bos.toByteArray();
            output = new String(data);
        } catch (Exception e) {
            Log.e("error", e.toString());
            e.printStackTrace();
            output = e.toString();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        String jsonStr = new String(data);
//        //第一种方式
//        Map<String, Map<String, Map<String, List<List<Object>>>>> maps = (Map<String, Map<String, Map<String, List<List<Object>>>>>)JSON.parse(jsonStr);
//        System.out.println(maps);
        return output;
    }

    //下载图片
    public static void downloadPhoto(String photo, String dest) {
        URL url = null;
        InputStream input = null;
        HttpURLConnection conn = null;
        OutputStream out = null;
        byte date[] = null;
        try {
            out = new FileOutputStream(dest);
            url = new URL(photo);// 1.将网址封装到URL中
            conn = (HttpURLConnection) url.openConnection();// 2.打开http连接
            conn.connect();// 3.开始连接
            input = conn.getInputStream();// 4.获取网络输入流，用于读取要下载内容
            ByteArrayOutputStream bos = new ByteArrayOutputStream();// 5.获取字节转换流
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            date = bos.toByteArray();
            out.write(date);
            System.out.println(dest + "图片下载成功，以写入本地磁盘..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

