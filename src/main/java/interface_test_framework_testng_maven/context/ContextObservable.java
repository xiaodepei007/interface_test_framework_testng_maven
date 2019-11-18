package interface_test_framework_testng_maven.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供context使用观察者模式   这个是被观察者对象类
 */
public class ContextObservable {

    //map是用来存放观察者的
    Map<String, List<Observer>> map = new HashMap<>();


    /**
     * 线程安全 忽略null的订阅者
     * @param type
     * @param observer
     */
    public  void addObserver(String type, Observer observer){
        if(null == observer)
            return;

        synchronized (this){
            if(map.containsKey(type)){
                map.get(type).add(observer);
            }else {
                List<Observer> observers = new ArrayList<>();
                observers.add(observer);
                map.put(type, observers);
            }
        }

    }


    public void notice(String type, Object arg){
        synchronized (this){
            if(map.containsKey(type)){
                List<Observer> observers = map.get(type);
                call(observers, type, arg);
            }
        }
    }

    protected void call(List<Observer> observers, String type, Object arg){
        for (Observer observer : observers) {
            observer.update(this, type, arg);
        }
    }
}
