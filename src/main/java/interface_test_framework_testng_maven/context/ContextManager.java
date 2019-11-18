package interface_test_framework_testng_maven.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//单例
public class ContextManager {
    private static ContextManager ourInstance = new ContextManager();

    public static ContextManager getInstance() {
        return ourInstance;
    }

    private ContextManager() {
    }

    Map<String, Context> map = new ConcurrentHashMap<>();


    //增加context方法
    public void addContext(String key, Context context){
        map.put(key, context);
    }

    //获得context方法
    public Context getContext(String key){
        return map.get(key);
    }


    //删除context方法
    public void removeContext(String key, Context context){
        map.keySet().remove(key);
    }
}
