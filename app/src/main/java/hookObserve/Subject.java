package hookObserve;

import java.util.List;
import java.util.Map;

/**
 * 抽象被观察者
 * Created by yangxiangjun on 2020/12/21.
 */
public interface Subject {
    /**
     * 观察者注册
     * @param observer 具体观察者
     */
    void addObserver(Observer observer);

    /**
     * 删除观察者
     * @param observer 具体观察者
     */
    void removeObserver(Observer observer);

    /**
     * 主题有变化时通知观察者
     */
    void notifyObserver(Map<String, Map<String, Object>> appMp);
}