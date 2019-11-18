package interface_test_framework_testng_maven.init.observer;

import interface_test_framework_testng_maven.context.ContextObservable;
import interface_test_framework_testng_maven.context.Observer;

public class ResponseSignObserver implements Observer {
    @Override
    public void update(ContextObservable o, String type, Object arg) {
            System.out.println("收到请求完毕消息，开始解密："+Thread.currentThread().getName());
    }
}
