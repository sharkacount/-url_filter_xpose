package hookObserve;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

/**
 * 具体的被观察者实现
 * Created by yangxiangjun on 2020/12/21.
 */
public class HookSubject implements Subject {

    //存放观察者列表的集合
    private Vector<Observer> observers;

    public HookSubject() {
        this.observers = new Vector<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int indexOf = observers.indexOf(observer);
        if (indexOf > 0) {
            observers.remove(indexOf);
        }
    }

    @Override
    public void notifyObserver(Map<String, Map<String, Object>> appMp) {
        CompletableFuture[] futures = new CompletableFuture[observers.size()];
        observers.stream().forEach(observer -> {
            final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> observer.call(appMp));
            futures[observers.indexOf(observer)] = future;
        });
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futures);
        completableFuture.join();
    }
}
