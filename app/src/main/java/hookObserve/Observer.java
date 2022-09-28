package hookObserve;

import java.util.List;
import java.util.Map;

/**
 * 抽象观察者
 * Created by yangxiangjun on 2020/12/21.
 */
public interface Observer {
    void call(Map<String, Map<String, Object>> appMp);
}