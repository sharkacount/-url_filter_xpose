package utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaticData {
    static {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        xposId = sb.toString();
    }
    public volatile static String xposId;
    public volatile static boolean hasStart = false;
    public volatile static String package_name = "";
    public static ExecutorService es = Executors.newFixedThreadPool(10);
    public static String prefFileName = "data";
    public static String SinkKey = "sink_url";
    public static String RootPath = File.separator + "sdcard" + File.separator;
    public static String[] NeededPermission = {
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.INTERNET",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"
    };

    public static String ROOT_URL = "http://192.168.137.225:5000/";
    public static String shareContentCollectUrl = ROOT_URL + "share_content_collect_url";
    public static String sinkURL = ROOT_URL + "get_sink";
    public static String sinkByPackageNameURL = ROOT_URL + "get_sink_by_package_name";
    public static String dataHandlerURL = ROOT_URL + "sink_class";
    public static String urlFilteURL = ROOT_URL + "filter_url_replace";
    public static String errorURL = ROOT_URL + "error";
    public static String logURL = ROOT_URL + "log";

    @SuppressLint("StaticFieldLeak")
    public volatile static Context globalContext = null;

    // 调用顺序
    public static Map<String, TreeSet<Long>> methodInvokeTimeMp = new HashMap<>();
}
