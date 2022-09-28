package gosec.xpose_detection;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import hookObserve.HookObserver;
import hookObserve.HookSubject;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import utils.HttpUtils;
import utils.MetadataListener;
import utils.StaticData;

import static de.robv.android.xposed.XposedBridge.log;

public class HookEntry implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        myToast(lpparam, "a");
        insertPermissions(null, StaticData.NeededPermission);
        HookObserver hookObserver = new HookObserver(lpparam);
        HookSubject hookSubject = new HookSubject();
        hookSubject.addObserver(hookObserver);
        MetadataListener metadataListener = new MetadataListener(hookSubject);
        metadataListener.start();
    }

    void myToast(XC_LoadPackage.LoadPackageParam lpparam, String mes) {
        final Class<?> clazz = XposedHelpers.findClass("android.app.Instrumentation", null);
        XposedHelpers.findAndHookMethod(clazz, "callApplicationOnCreate", Application.class
                , new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Context context = null;
                        StaticData.globalContext = context;
                        if (param.args[0] instanceof Application) {
                            context = ((Application) param.args[0]).getApplicationContext();
                        } else {
                            return;
                        }
                        StaticData.package_name = context.getPackageName();
                        if(!StaticData.hasStart){
                            Toast.makeText(context, context.getPackageName() + "is hooked ", Toast.LENGTH_SHORT).show();
                            StaticData.hasStart = true;
                        }

                    }
                });
    }

    void insertPermissions(ClassLoader classLoader, String[] permissions) {
        ///android.content.pm.PackageParser.java
        Class<?> PackageParserClass = XposedHelpers.findClass("android.content.pm.PackageParser", classLoader);
        XposedHelpers.findAndHookMethod(PackageParserClass, "parseUsesPermission",
                "android.content.pm.PackageParser$Package",
                "android.content.res.Resources",
                "android.content.res.XmlResourceParser",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Object pp_Package = param.args[0];
                        List<String> requestedPermissions = (ArrayList<String>) XposedHelpers.getObjectField(pp_Package, "requestedPermissions");

                        StringBuilder sb = new StringBuilder();
                        for (String str : requestedPermissions) {
                            sb.append(str).append(" ");
                        }
                        XposedBridge.log("requestedPermissions:" + sb.toString());
                        System.out.println("requestedPermissions:" + sb.toString());
                        for (String permission : permissions) {
                            if (!requestedPermissions.contains(permission)) {
                                requestedPermissions.add(permission);
                            }
                        }
                    }
                }
        );
    }
}
