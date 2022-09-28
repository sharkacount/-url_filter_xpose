package gosec.xpose_detection;

import de.robv.android.xposed.IXposedHookLoadPackage;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

// TODO
// 通过IXposedHookLoadPackage 接口中的 handleLoadPackage 方法来实现 Hook 并篡改程序的输出结果
// example.com.androiddemo 是目标程序的包名
// example.com.androiddemo.MainActivity 是想要Hook的类
// toastMessage 是想要 Hook 的方法
// 在 afterHookedMethod 方法 修改了toastMessage()方法的返回值
//

public class HookTest implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
//        XposedBridge.log("xpose is into hook");
//        Log.v("haha", "xpose start");
        // appMp: app packagename -> class name -> method name -> arguments list
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    List<XC_MethodHook.Unhook> unhookList = new ArrayList<>();
//                    HashMap<String, HashMap<String, HashMap<String, ArrayList<Object>>>> appMp = MetadataListener.getHookMethod();
//                    String curAppPackageName = loadPackageParam.packageName;
//                    if (!appMp.containsKey(curAppPackageName)) return;
//                    // classMp: class name -> method name -> arguments list
//                    HashMap<String, HashMap<String, ArrayList<Object>>> classMp = appMp.get(curAppPackageName);
//                    for (Map.Entry<String, HashMap<String, ArrayList<Object>>> classEntry : classMp.entrySet()) {
//                        Class clazz = null;
//                        try {
//                            clazz = loadPackageParam.classLoader.loadClass(classEntry.getKey());
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        for (Map.Entry<String, ArrayList<Object>> methodEntry : classEntry.getValue().entrySet()) {
//                            String methodName = methodEntry.getKey();
//                            methodEntry.getValue().add(new HookLogic());
//                            Object[] arr = methodEntry.getValue().toArray();
//                            XC_MethodHook.Unhook unhook = XposedHelpers.findAndHookMethod(clazz, methodName, arr);
//                            unhookList.add(unhook);
//                        }
//                    }
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    for (XC_MethodHook.Unhook unhook : unhookList)
//                        unhook.unhook();
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        t.start();

    }
}

