package hookObserve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hook.Hooker;

/**
 * 具体的观察者实现
 * Created by yangxiangjun on 2020/12/21.
 */
public class HookObserver implements Observer{
    private XC_LoadPackage.LoadPackageParam lpparam;
    private final Hooker hooker;
    private List<XC_MethodHook.Unhook> unhookList;
    public HookObserver(XC_LoadPackage.LoadPackageParam lpparam){
        this.lpparam = lpparam;
        this.hooker = new Hooker();
        this.unhookList = new ArrayList<>();
    }

    @Override
    public void call(Map<String, Map<String, Object>> appMp) {
        // do somethink
        // unhook before
        hooker.unhook(unhookList);
        // hook now
        unhookList = hooker.hookWithSourceAndSink(lpparam, appMp);
    }
}