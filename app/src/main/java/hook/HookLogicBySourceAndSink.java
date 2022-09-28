package hook;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.Future;

import de.robv.android.xposed.XC_MethodHook;
import utils.HttpUtils;
import utils.StaticData;

public class HookLogicBySourceAndSink extends XC_MethodHook {
    // demo: <okhttp3.RequestBody: okhttp3.RequestBody create(okhttp3.MediaType,java.io.File)>
    public String methodSign;
    public String sourceMethod;
    public String sinkMethod;

    public long startTime, endTime;

    public HookLogicBySourceAndSink(String methodSign, String sourceMethod, String sinkMethod) {
        this.methodSign = methodSign;
        this.sourceMethod = sourceMethod;
        this.sinkMethod = sinkMethod;
    }

    public boolean sourceInvoked() {
        if (sourceMethod == null) return true;
        synchronized (StaticData.methodInvokeTimeMp) {
            if(!StaticData.methodInvokeTimeMp.containsKey(this.sourceMethod))   return false;
            Long sinkInvokedTime = System.currentTimeMillis();
            Long sourceInvokedTime = StaticData.methodInvokeTimeMp.get(this.sourceMethod).lower(sinkInvokedTime);
            if(sourceInvokedTime==null || sinkInvokedTime - sourceInvokedTime>5000) return false;
            return true;
        }
    }

    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        startTime = System.nanoTime();   //获取开始时间
        System.out.println("hook check before : " + this.sourceMethod + " " + this.sinkMethod);
        System.out.println("hook check after: " + this.sourceMethod + " " + this.sinkMethod);
        Object[] args = param.args;
        System.out.println("hook check para len: " + args.length);
        for(Object obj: args){
            System.out.println("hook check after para: " + (obj==null?"null":obj.toString()));
        }
        Future<Object[]> future = StaticData.es.submit(new ParameterHandleTask(args, this.methodSign, this.sourceMethod, this.sinkMethod));
        Object[] res = future.get();
        // 直接args=future.get()好像不会成功，这里直接一个个赋值
        for (int i = 0; i < args.length; i++) {
            args[i] = res[i];
        }
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        long endTime = System.nanoTime(); //获取结束时间
        double defenceTime = (endTime - startTime)/1000000.0;
        System.out.println("分享纳秒运行时间： " + defenceTime);
    }

}
