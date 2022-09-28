package hook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.HttpUtils;
import utils.StaticData;

class ParameterHandleTask implements Callable<Object[]> {
    private Object[] args;
    private String methodSign;
    public String sinkMethod;
    public String sourceMethod;

    public ParameterHandleTask(Object[] args, String methodSign, String sourceMethod, String sinkMethod) {
        this.args = args;
        this.methodSign = methodSign;
        this.sourceMethod = sourceMethod;
        this.sinkMethod = sinkMethod;
    }

    private void wechatShareDataCollectionByWecharSDK(Object mediaObject) throws Exception{
        // 文本分享
        if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXTextObject")) {
            // 获取原来的值
            String textObject = (String)mediaObject.getClass().getField("text").get(mediaObject);
            System.out.println("微信文本分享内容为: " + textObject);
            // 发送
            String res = shareContentCheck(textObject);
        }
        // 图片分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXImageObject")) {
            Object imageDataByteArr = mediaObject.getClass().getField("imageData").get(mediaObject);
            Object imagePath = mediaObject.getClass().getField("imagePath").get(mediaObject);
            System.out.println("imageDataByteArr: " + imageDataByteArr);
            System.out.println("imagePath: " + imagePath);
            mediaObject.getClass().getField("imagePath").set(mediaObject, "what the fuck?!");
        }
        // 音乐分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMusicObject")) {
            String[] fieldArr = {"musicLowBandUrl", "musicDataUrl", "musicDataUrl", "musicLowBandDataUrl"};
            for (String field : fieldArr) {
                String musicUrlObject = (String) mediaObject.getClass().getField(field).get(mediaObject);
                if (musicUrlObject == null || musicUrlObject.equals("")) continue;
                System.out.println("微信音乐分享内容为: " + musicUrlObject);
                // 发送
                String res = shareContentCheck(musicUrlObject);
            }

        }
        // 视频分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXVideoObject")) {
            // 获取原来的值
            String videoUrlObject = (String) mediaObject.getClass().getField("videoUrl").get(mediaObject);
            System.out.println("oriWebpageUrlObject: " + videoUrlObject);
            // 发送
            String res = shareContentCheck(videoUrlObject);
        }
        // 网页分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXWebpageObject")) {
            String webpageUrlObject = (String)mediaObject.getClass().getField("webpageUrl").get(mediaObject);
            System.out.println("webpageUrlObject: " + webpageUrlObject);
            // 发送
            String res = shareContentCheck(webpageUrlObject);
        }
        // 小程序分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject")) {
            // 获取原来的值
            String webpageUrlObject = (String) mediaObject.getClass().getField("webpageUrl").get(mediaObject);
            System.out.println("oriwebpageUrlObject: " + webpageUrlObject);
            // 发送
            String res = shareContentCheck(webpageUrlObject);
        }
        // 音乐视频分享
        else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMusicVideoObject")) {
            String musicUrlObject = (String) mediaObject.getClass().getField("musicUrl").get(mediaObject);
            String musicDataUrlObject = (String) mediaObject.getClass().getField("musicDataUrl").get(mediaObject);
            System.out.println("musicUrlObject: " + musicUrlObject);
            System.out.println("musicDataUrlObject: " + musicDataUrlObject);
            // 发送
            shareContentCheck(musicUrlObject);
            shareContentCheck(musicDataUrlObject);
        } else {
            System.out.println("what the hell>?!!!!!!!!!!???????");
        }
    }

    private void wechatShareDataCollectionByShareSDK(Object mediaObject) throws Exception{
        // 文本分享
        if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXTextObject")) {
            Object textObject = mediaObject.getClass().getField("text").get(mediaObject);
            System.out.println("微信文本分享内容为: " + textObject);
        }
        // 图片分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXImageObject")) {
            Object imageDataByteArr = mediaObject.getClass().getField("imageData").get(mediaObject);
            Object imagePath = mediaObject.getClass().getField("imagePath").get(mediaObject);
            System.out.println("imageDataByteArr: " + imageDataByteArr);
            System.out.println("imagePath: " + imagePath);
            mediaObject.getClass().getField("imagePath").set(mediaObject, "what the fuck?!");
        }
        // 音乐分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXMusicObject")) {
            String[] fieldArr = {"musicLowBandUrl", "musicDataUrl", "musicDataUrl", "musicLowBandDataUrl"};
            for (String field : fieldArr) {
                String musicUrlObject = (String) mediaObject.getClass().getField(field).get(mediaObject);
                if (musicUrlObject == null || musicUrlObject.equals("")) continue;
                System.out.println("微信音乐分享内容为: " + musicUrlObject);
                // 发送
                String res = shareContentCheck(musicUrlObject);
            }

        }
        // 视频分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXVideoObject")) {
            // 获取原来的值
            String videoUrlObject = (String) mediaObject.getClass().getField("videoUrl").get(mediaObject);
            System.out.println("oriWebpageUrlObject: " + videoUrlObject);
            // 发送
            String res = shareContentCheck(videoUrlObject);
        }
        // 网页分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXWebpageObject")) {
            String webpageUrlObject = (String)mediaObject.getClass().getField("webpageUrl").get(mediaObject);
            System.out.println("webpageUrlObject: " + webpageUrlObject);
            // 发送
            String res = shareContentCheck(webpageUrlObject);
        }
        // 小程序分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXMiniProgramObject")) {
            // 获取原来的值
            String webpageUrlObject = (String) mediaObject.getClass().getField("webpageUrl").get(mediaObject);
            System.out.println("oriwebpageUrlObject: " + webpageUrlObject);
            // 发送
            String res = shareContentCheck(webpageUrlObject);
        }
        // 音乐视频分享
        else if (mediaObject.getClass().getName().equals("cn.sharesdk.wechat.utils.WXMusicVideoObject")) {
            String musicUrlObject = (String) mediaObject.getClass().getField("musicUrl").get(mediaObject);
            String musicDataUrlObject = (String) mediaObject.getClass().getField("musicDataUrl").get(mediaObject);
            System.out.println("musicUrlObject: " + musicUrlObject);
            System.out.println("musicDataUrlObject: " + musicDataUrlObject);
            // 发送
            shareContentCheck(musicUrlObject);
            shareContentCheck(musicDataUrlObject);
        } else {

        }
    }

    // 性能测试
//    @Override
//    public Object[] call() throws Exception {
//        double originTime = 0;
//        double time = 10;
//        for(int i=0; i<time; i++){
//            long startTime = System.currentTimeMillis();   //获取开始时间
//            //测试的代码段
//            for (int j = 0; j < args.length; j++) {
//                // 伪代码，模拟干事情
//            }
//            long endTime = System.currentTimeMillis(); //获取结束时间
//            originTime += (endTime - startTime);
//        }
//        originTime /= time;
//
//        double defenceTime = 0;
//        for(int i=0; i<time; i++){
//            long startTime = System.currentTimeMillis();   //获取开始时间
//            //测试的代码段
//            for (int j = 0; j < args.length; j++) {
//                if (args[j] == null) continue;
//                if (args[j] instanceof String) {
//                    String res = shareContentCheck((String) args[j]);
//                }
//            }
//            long endTime = System.currentTimeMillis(); //获取结束时间
//            defenceTime += (endTime - startTime);
//        }
//        defenceTime /= time;
//        System.out.println("originTime: " + originTime);
//        System.out.println("defenceTime: " + defenceTime);
//
//        return args;
//    }


    // for 脱敏
    @Override
    public Object[] call() throws Exception {
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[i].toString(): " + args[i].toString());
            System.out.println("args[i].getClass(): " + args[i].getClass().toString());
            System.out.println("args[i].father(): " + args[i].getClass().getSuperclass().getName());
            System.out.println("args[i].getClass().getName(): " + args[i].getClass().getName());
            if (args[i] instanceof String) {
                String res = filterUrlByOkHttp(StaticData.urlFilteURL, (String) args[i]);
                String filtedUrl = ((Map<String, String>) JSON.parse(res)).get("url");
                System.out.println("filtedUrl: " + filtedUrl);
                args[i] = filtedUrl;
//                args[i] = "I am ur mom, rememeber that, bull!";
            } else if (args[i].getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.SendMessageToWX$Req")) {
                Field messageField = args[i].getClass().getField("message");
                Object messageObject = messageField.get(args[i]);

                Object mediaObject = messageObject.getClass().getField("mediaObject").get(messageObject);
                System.out.println("mediaObject.getClass().getName(): " + mediaObject.getClass().getName());

                // 文本分享
                if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXTextObject")) {
                    Object textObject = mediaObject.getClass().getField("text").get(mediaObject);
                    System.out.println("微信文本分享内容为: " + textObject);
                }
                // 图片分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXImageObject")) {
//                    Object imageDataByteArr = mediaObject.getClass().getField("imageData").get(mediaObject);
//                    Object imagePath = mediaObject.getClass().getField("imagePath").get(mediaObject);
//                    System.out.println("imageDataByteArr: " + imageDataByteArr);
//                    System.out.println("imagePath: " + imagePath);
//                    mediaObject.getClass().getField("imagePath").set(mediaObject, "what the fuck?!");
                }
                // 音乐分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMusicObject")) {
                    Object musicUrlObject = mediaObject.getClass().getField("musicUrl").get(mediaObject);
                    System.out.println("微信音乐分享内容为: " + musicUrlObject);
                }
                // 视频分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXVideoObject")) {
                    // 获取原来的值
                    String videoUrlObject = (String) mediaObject.getClass().getField("videoUrl").get(mediaObject);
                    System.out.println("oriWebpageUrlObject: " + videoUrlObject);
                    // 设置
                    String res = filterUrlByOkHttp(StaticData.urlFilteURL, videoUrlObject);
                    String filtedUrl = ((Map<String, String>) JSON.parse(res)).get("url");
                    mediaObject.getClass().getField("videoUrl").set(mediaObject, filtedUrl);
                    System.out.println("filtedUrl: " + filtedUrl);
                    // 查看新的值
                    videoUrlObject = (String) mediaObject.getClass().getField("videoUrl").get(mediaObject);
                    System.out.println("newWebpageUrlObject: " + videoUrlObject);
                }
                // 网页分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXWebpageObject")) {
                    Object webpageUrlObject = mediaObject.getClass().getField("webpageUrl").get(mediaObject);
                    System.out.println("webpageUrlObject: " + webpageUrlObject);
                }
                // 小程序分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject")) {
                    // 获取原来的值
                    String webpageUrlObject = (String) mediaObject.getClass().getField("webpageUrl").get(mediaObject);
                    System.out.println("oriwebpageUrlObject: " + webpageUrlObject);
                    // 设置
                    String res = filterUrlByOkHttp(StaticData.urlFilteURL, webpageUrlObject);
                    String filtedUrl = ((Map<String, String>) JSON.parse(res)).get("url");
                    mediaObject.getClass().getField("webpageUrl").set(mediaObject, filtedUrl);
                    System.out.println("filtedUrl: " + filtedUrl);
                    // 查看新的值
                    webpageUrlObject = (String) mediaObject.getClass().getField("webpageUrl").get(mediaObject);
                    System.out.println("newWebpageUrlObject: " + webpageUrlObject);
                }
                // 音乐视频分享
                else if (mediaObject.getClass().getName().equals("com.tencent.mm.opensdk.modelmsg.WXMusicVideoObject")) {
                    Object musicUrlObject = mediaObject.getClass().getField("musicUrl").get(mediaObject);
                    Object musicDataUrlObject = mediaObject.getClass().getField("musicDataUrl").get(mediaObject);
                    System.out.println("webpageUrlObject: " + musicDataUrlObject);
                }
            } else {
                System.out.println("unhandlable type");
            }
        }
        return args;
    }

    public String filterUrlByOkHttp(String url, String oriUrl) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ori_url", oriUrl)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String shareContentCheck(String content) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("package_name", StaticData.package_name)
                .addFormDataPart("method_sign", methodSign)
                .addFormDataPart("xpos_id", StaticData.xposId)
                .addFormDataPart("sink_method", this.sinkMethod)
                .addFormDataPart("link", content)
                .build();
        Request request = new Request.Builder()
                .url(StaticData.shareContentCollectUrl)
                .post(formBody)
                .build();
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}