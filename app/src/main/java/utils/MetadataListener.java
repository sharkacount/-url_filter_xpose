package utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hookObserve.Subject;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MetadataListener extends Thread {
    private Subject subject;
    public int preHash = 0;

    public MetadataListener(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void run() {
        // 心跳检测
        Map<String, Map<String, Object>> curMp = getEmpty();
        while (true) {
            String sinkUrl = StaticData.sinkURL;
            while (StaticData.package_name.equals("")) {

            }
            Log.i("StaticData.package_name: ", StaticData.package_name);
            try {
                curMp = getHookMethodFromNet(StaticData.sinkURL);
                String curMpStr = curMp.toString();
                Log.i("sinkURL: ", sinkUrl);
                Log.i("mySinkData!!!  ", curMpStr);
                int curMpHash = getSinkHashCode(curMp);
                Log.i("curMpHash", Integer.toString(curMpHash));
                subject.notifyObserver(curMp);
                // 跟之前的hash不同才重新hook，避免重复hook相同的
//                if (curMpHash != preHash) {
//                    preHash = curMpHash;
//                    subject.notifyObserver(curMp);
//                    System.out.println("sink changed!!!");
////                    Toast.makeText(StaticData.globalContext, "change hahaha!!!", Toast.LENGTH_SHORT).show();
//                }
                // 暂时用来做检测，不做心跳了
                break;
            } catch (Exception e) {
                // temp
                OkHttpClient client = new OkHttpClient();
                MultipartBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("error", e.toString())
                        .build();
                Request request = new Request.Builder()
                        .url(StaticData.errorURL)
                        .post(formBody)
                        .build();
                String result = "";
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            // 睡眠
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getSinkHashCode(Map<String, Map<String, Object>> curMp) {
        return 0;
    }

    public Map<String, Map<String, Object>> getHookMethodFromNet(String url) {
        // 临时关闭
        Map<String, Map<String, Object>> result = new HashMap<>();
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                    .build();
            MultipartBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("package_name", StaticData.package_name)
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
        return result;
    }

    public Map<String, Map<String, Object>> getEmpty() {
        Map<String, Map<String, Object>> mp = new HashMap<>();
        return mp;
    }

    // for demo
    public static Map<String, Map<String, Map<String, List<List<Object>>>>> getHookMethod() {
        Map<String, Map<String, List<List<Object>>>> appMp = new HashMap<>();
        List<List<Object>> sumList = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        list.add("java.lang.String");
        list.add("java.lang.Integer");
        list.add("int");
        list.add("[C");
        sumList.add(list);
        Map<String, List<List<Object>>> tmp = new HashMap<>();
        tmp.put("hello", sumList);
        appMp.put("gosec.xpose_victim.MainActivity", tmp);
        Map<String, Map<String, Map<String, List<List<Object>>>>> mp = new HashMap<>();
        mp.put("gosec.xpose_victim", appMp);
        return mp;
    }
}
