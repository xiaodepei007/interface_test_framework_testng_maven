package interface_test_framework_testng_maven.test;

import interface_test_framework_testng_maven.annotation.IgnoreNamedParam;
import interface_test_framework_testng_maven.annotation.NamedParam;
import interface_test_framework_testng_maven.annotation.Scenario;
import interface_test_framework_testng_maven.context.ContextManager;
import interface_test_framework_testng_maven.network.util.HttpRequestUtil;
import interface_test_framework_testng_maven.util.testng.TestContextUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class CommonBase extends Base implements ITest {

    //方法级别并发
    private Map<String, String> allParameters;
    public Map<String, String> getAllParameters() {
        return allParameters;
    }
    @BeforeClass
    protected void commonBase_before_class_init_parameter(ITestContext context){
        /**
         * 在bc执行完成之后就不应该去修改它。防止method级别并发同时写allParameters
         */
        allParameters = TestContextUtil.getClassParameter(context, this.getClass());
    }


    //每次执行test的时候更新方法名
    private ThreadLocal<String> methodName = ThreadLocal.withInitial(()->"");
    public String getMethodName() {
        return methodName.get();
    }
    @BeforeMethod
    protected void commonBase_before_method_init_methodName(Method m){
        methodName.set(m.getName());
    }


    //方法级别并发
    //每次执行test的时候更新参数列表
    private ThreadLocal<Map<String, String>> netParams = ThreadLocal.withInitial(()->new HashMap<>());//为测试方法提供额外参数的备用方法。旨在测试中动态提供参数
    public Map<String, String> getNetParams() {
        return netParams.get();
    }
    private ThreadLocal<Map<String, String>> params = ThreadLocal.withInitial(()->new HashMap<>());
    private ThreadLocal<Map<String, String>> allParams = ThreadLocal.withInitial(()->new HashMap<>());
    public Map<String, String> getAllParams() {
        return allParams.get();
    }
    public Map<String, String> getParams() {
        return params.get();
    }
    @BeforeMethod
    public void commonBase_before_method_init_param(Method m, Object[] objects){
        Map<String, String> all = new HashMap<>();
        Map<String, String> temp = new HashMap<>();
        Parameter[] parameters = m.getParameters();

        if(parameters == null){
            return;
        }else {
            for (int i = 0; i < m.getParameters().length; i++) {
                boolean ignore = false;
                String name = parameters[i].getName();
                Annotation[] annotations = parameters[i].getAnnotations();
                if(!ArrayUtils.isEmpty(annotations)){
                    for (Annotation annotation : annotations) {
                        if(annotation.annotationType() == IgnoreNamedParam.class){
                            ignore = true;
                            break;
                        }
                        if(annotation.annotationType() == NamedParam.class){
                            NamedParam named = (NamedParam) annotation;
                            name = named.value();
                        }

                    }
                }

                if(!ignore)
                    temp.put(name, objects[i].toString());
            }
        }
        params.set(temp);
        all.putAll(temp);
        /**
         * 每次调用接口更新该参数字典。使用post.传递param，至于接口是不是会使用这些参数，由接口定义者决定
         */

        try {
            //设定net_param_mode参数，请求更新参数.no_config_param表示在全局更新下，不更新。no_config_param优先级比net_param_mode高
            //every,told,method参数如果有net_config_param,no_config_param
            String netConfigMode = ContextManager.getInstance().getContext(getKey()).getProperty("net_param_mode");
            if(!temp.containsKey("no_config_param") && (netConfigMode.equals("every") || temp.containsKey("net_config_param"))) {
                String url =ContextManager.getInstance().getContext(getKey()).getProperty("net_param_url");
                if (!StringUtils.isEmpty(url)) {
                    byte[] content = HttpRequestUtil.post(url, "utf-8", temp);
                    Map<String, String> netParamsMap = netParam(content);
                    netParams.set(netParamsMap);
                    all.putAll(netParamsMap);
                }
            }
        } catch (IOException e) {
            //忽略异常
            e.printStackTrace();
        }

    }

    /**
     * {
     *     "success":true,
     *     "code":0,
     *     "message":"",
     *     "content":{
     *         "net_param":"net_value"
     *     }
     * }
     * @param content
     * @return
     */
    private Map<String, String> netParam(byte[] content){
        Map<String, String> temp = new HashMap<>();
        if(null == content || content.length == 0){
            return temp;
        }
        try {
            String json  = new String(content, "utf-8");
            System.out.println(json);
            JSONObject root = new JSONObject(json);
            if(root.getBoolean("success")){
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

    //方法级别并发
    //每次执行class的时候更新class名称
    @Override
    public String getTestName() {
        String testName = "";
        Scenario withTestName = this.getClass().getAnnotation(Scenario.class);
        if( null == withTestName){
            testName = this.getClass().getName();
        }else {
            testName = withTestName.value();
        }
        return testName;
    }
}
