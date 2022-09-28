package hook;

import java.util.concurrent.Future;

import de.robv.android.xposed.XC_MethodHook;
import utils.StaticData;

public class HookBySingleSink {
    public String methodSign;

    HookBySingleSink(String methodSign) {
        this.methodSign = methodSign;
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        Object[] args = param.args;
        Future<Object[]> future = StaticData.es.submit(new ParameterHandleTask(args, this.methodSign, null, null));
        Object[] res = future.get();
        // 直接args=future.get()好像不会成功，这里直接一个个赋值
        for (int i = 0; i < args.length; i++) {
            args[i] = res[i];
        }
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

    }
}
