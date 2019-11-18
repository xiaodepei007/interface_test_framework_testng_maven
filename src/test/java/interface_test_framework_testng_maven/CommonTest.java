package interface_test_framework_testng_maven;

import interface_test_framework_testng_maven.context.Context;
import interface_test_framework_testng_maven.context.ContextManager;
import interface_test_framework_testng_maven.network.MyRequest;
import interface_test_framework_testng_maven.network.MyResponse;
import interface_test_framework_testng_maven.test.ClassLoadFileBase;
import interface_test_framework_testng_maven.test.process.IHttpPrePostExceptionCallback;

import java.util.HashMap;
import java.util.Map;

public abstract class CommonTest extends ClassLoadFileBase implements IHttpPrePostExceptionCallback {

    public abstract void beforeRequestNotice(MyRequest request, MyResponse response, Map<String, String> deliver);
    public abstract void afterResponseNotice(MyRequest request, MyResponse response, Map<String, String> deliver);
    @Override
    public void pre(MyRequest request, MyResponse response, Map<String, String> deliver) {
        beforeRequestNotice(request, response, deliver);
        Map<String, String> temp = new HashMap<>();
        temp.putAll(deliver);
        request.addExtra("request_deliver", temp);
        notice(request);
    }

    @Override
    public void post(MyRequest request, MyResponse response, Map<String, String> deliver) {
        Map<String, Object> map = new HashMap<>();
        map.put("request", request);
        map.put("response", response);
        notice(map);

        afterResponseNotice(request, response, deliver);
    }
    public void notice(MyRequest request){
        ContextManager.getInstance().getContext(getKey()).notice(Context.REQUEST_PARSED_TYPE, request);
    }
    public void notice(Map<String, Object> requestResponseMap){
        ContextManager.getInstance().getContext(getKey()).notice(Context.RESPONSE_PARSED_TYPE, requestResponseMap);
    }
}
