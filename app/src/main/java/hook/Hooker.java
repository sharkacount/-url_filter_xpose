package hook;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hooker {
    private StringBuilder listFlattening(List<Object> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1)
                sb.append(".");
        }
        return sb;
    }

    /*
                "targetClass": "ld.c",
                "targetReturn": "ld.f",
                "targetMethod": "n",
                "targetArguments": [
                    "java.lang.String",
                    "jd.f",
                    "java.util.Ma"
                ]
    * */
    private String listToString(List<Object> list) {
        StringBuilder sb = new StringBuilder("(");
        if (list == null || list.size() == 0) return sb.append(")").toString();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) sb.append(",");
        }
        return sb.append(")").toString();
    }

    public List<XC_MethodHook.Unhook> hookWithSourceAndSink(XC_LoadPackage.LoadPackageParam loadPackageParam, Map<String, Map<String, Object>> appMp) {
        List<XC_MethodHook.Unhook> unhookList = new ArrayList<>();
        if (appMp == null) return unhookList;
        for (Map.Entry<String, Map<String, Object>> hookData : appMp.entrySet()) {
            com.alibaba.fastjson.JSONObject sourceData = (com.alibaba.fastjson.JSONObject) hookData.getValue().get("source");
            com.alibaba.fastjson.JSONObject sinkData = (com.alibaba.fastjson.JSONObject) hookData.getValue().get("sink");
            if (sourceData == null || sinkData == null) continue;
            String methodSign = hookData.getKey();
            String sourceMethod = null;
            String sinkMethod = null;
            if (sourceData.get("targetClass") != null) {
                sourceMethod = sourceData.get("targetClass") + "." + sourceData.get("targetMethod") + listToString((List<Object>) sourceData.get("targetArguments"));
            }
            if (sinkData.get("targetClass") != null) {
                sinkMethod = sinkData.get("targetClass") + "." + sinkData.get("targetMethod") + listToString((List<Object>) sinkData.get("targetArguments"));
            }
            try {
                Class clazz = loadPackageParam.classLoader.loadClass(sinkData.getString("targetClass"));
                String methodName = sinkData.getString("targetMethod");
                List<Object> methodParameter = JSON.parseArray(Objects.requireNonNull(sinkData.get("targetArguments")).toString());
                methodParameter.add(new HookLogicBySourceAndSink(methodSign, sourceMethod, sinkMethod));
                Log.i("targetClass: ", sinkData.getString("targetClass"));
                Log.i("methodName: ", methodName);
                Log.i("methodParameter: ", methodParameter.toString());
                Object[] arr = methodParameter.toArray();
                XC_MethodHook.Unhook unhook = XposedHelpers.findAndHookMethod(clazz, methodName, arr);
                unhookList.add(unhook);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return unhookList;
    }

    public List<XC_MethodHook.Unhook> hook(XC_LoadPackage.LoadPackageParam loadPackageParam,
                                           Map<String, Map<String, Map<String, List<List<Object>>>>> appMp) {
        List<XC_MethodHook.Unhook> unhookList = new ArrayList<>();
        // appMp: app package name -> class name -> method name -> arguments list
        String curAppPackageName = loadPackageParam.packageName;
        // android can't load class file, so I can't dynamicly load class like what I do in java. Damn!
        if (appMp.containsKey(curAppPackageName)) {
            // classMp: class name -> method name -> arguments list
            Map<String, Map<String, List<List<Object>>>> classMp = appMp.get(curAppPackageName);
            for (Map.Entry<String, Map<String, List<List<Object>>>> classEntry : classMp.entrySet()) {
                Class clazz = null;
                try {
                    clazz = loadPackageParam.classLoader.loadClass(classEntry.getKey());
                    for (Map.Entry<String, List<List<Object>>> methodEntrys : classEntry.getValue().entrySet()) {
                        String methodName = methodEntrys.getKey();
                        for (List<Object> methodParameter : methodEntrys.getValue()) {

                            StringBuilder methodSign = new StringBuilder();
                            methodSign.append(classEntry.getKey()).append(".");
                            methodSign.append(methodName).append(".");
                            methodSign.append(listFlattening(methodParameter));

                            methodParameter.add(new HookBySingleSink(methodSign.toString()));
                            Object[] arr = methodParameter.toArray();
                            XC_MethodHook.Unhook unhook = XposedHelpers.findAndHookMethod(clazz, methodName, arr);
                            unhookList.add(unhook);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return unhookList;
    }

    public void unhook(List<XC_MethodHook.Unhook> unhookList) {
        for (XC_MethodHook.Unhook unhook : unhookList)
            unhook.unhook();
    }

}
