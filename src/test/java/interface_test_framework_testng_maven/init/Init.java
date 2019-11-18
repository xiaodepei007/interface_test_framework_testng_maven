package interface_test_framework_testng_maven.init;

import interface_test_framework_testng_maven.context.Context;
import interface_test_framework_testng_maven.context.ContextManager;
import interface_test_framework_testng_maven.init.observer.RequestSignObserver;
import interface_test_framework_testng_maven.init.observer.ResponseSignObserver;
import interface_test_framework_testng_maven.network.util.HttpRequestUtil;
import interface_test_framework_testng_maven.util.testng.TestContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Init {
    //在test suite中的所有test运行之前运行，只运行一次；
    @BeforeSuite(description = "初始化测试上下文")
    public void beforeSuite(ITestContext iTestContext) {
        String suiteName = TestContextUtil.getSuiteName(iTestContext);
        Context context = new Context(suiteName, suiteName);
        context.init();//初始化
        context.addObserver(Context.REQUEST_PARSED_TYPE, new RequestSignObserver());//订阅消息
        context.addObserver(Context.RESPONSE_PARSED_TYPE, new ResponseSignObserver());//订阅消息
        initNetConfig(context);
        ContextManager.getInstance().addContext(suiteName, context);
    }

    public void initNetConfig(Context context) {
        try {
            String netConfigUrl = context.getProperty("net_config_url");
            if (!StringUtils.isEmpty(netConfigUrl)) {
                byte[] content = HttpRequestUtil.post(netConfigUrl, "utf-8", context.getMapProperties());
                Map<String, String> netParamsMap = netConfig(content);
                for (Map.Entry<String, String> entry : netParamsMap.entrySet()) {
                    context.addProperty(entry.getKey(), entry.getValue());
                }
            }
        } catch (IOException e) {
            //忽略异常
            e.printStackTrace();
        }
    }
    private Map<String, String> netConfig(byte[] content) {
        Map<String, String> temp = new HashMap<>();
        if (null == content || content.length == 0) {
            return temp;
        }
        try {
            String json = new String(content, "utf-8");
            System.out.println(json);
            JSONObject root = new JSONObject(json);
            if (root.getBoolean("success")) {
                JSONObject c = root.getJSONObject("content");
                for (String s : c.keySet()) {
                    temp.put(s, c.getString(s));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return temp;
    }
}