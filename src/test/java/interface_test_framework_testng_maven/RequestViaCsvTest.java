package interface_test_framework_testng_maven;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import interface_test_framework_testng_maven.annotation.IgnoreNamedParam;
import interface_test_framework_testng_maven.annotation.NamedParam;
import interface_test_framework_testng_maven.annotation.Scenario;
import interface_test_framework_testng_maven.context.ContextManager;
import interface_test_framework_testng_maven.data.annotation.ByteDataSource;
import interface_test_framework_testng_maven.data.test_data.dataProvider.CsvDataProvider;
import interface_test_framework_testng_maven.data.test_data.dataProvider.DataProviders;
import interface_test_framework_testng_maven.guice.module.factory.ByteDataSourceModuleFactory;
import interface_test_framework_testng_maven.network.MyRequest;
import interface_test_framework_testng_maven.network.MyResponse;
import interface_test_framework_testng_maven.template.IMarker;
import interface_test_framework_testng_maven.test.process.IDeliverableHttpRequestProcessor;
import interface_test_framework_testng_maven.test.process.ListJSONHttpRequestPrePostExceptionProcessor;
import io.qameta.allure.*;
import org.testng.ITestContext;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;


@Epic("账户模块")
@Feature("登陆")
@Scenario("登录测试正常场景集合")
@ByteDataSource(filePath = "classpath:test/request/request.json", charset = "utf-8")
@Guice(moduleFactory = ByteDataSourceModuleFactory.class)
public class RequestViaCsvTest extends CommonTest {

    //@Guice通过注入的方式实例化IMarker
    @Inject
    @Named("Freemarker")
    IMarker marker;


    @Story("异常登陆")
    @Description("json登陆测试很详细的细节描述偶")
    @Step("mid {mid}")
    @CsvDataProvider(path = "classpath:test/request/data.csv")
    @Test(dataProvider = "csv", dataProviderClass = DataProviders.class, description = "json登陆测试")
    public void login(@NamedParam("mid") String mid,
                      @IgnoreNamedParam ITestContext context){

        Map<String, Object> map = new HashMap<>();
        //后来者覆盖前者
        map.putAll(super.getAllParameters());
        map.putAll(super.getParams());

        String json = getFileContent();
        try {
            json = marker.mark(json, map);
            IDeliverableHttpRequestProcessor prePostExceptionProcessor = new ListJSONHttpRequestPrePostExceptionProcessor(json, this, true);
            prePostExceptionProcessor.addDeliver(super.getAllParameters());
            prePostExceptionProcessor.start();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(),throwable);
        }
    }

    @Override
    public void beforeRequestNotice(MyRequest request, MyResponse response, Map<String, String> deliver) {
        //do some replace
    }

    @Override
    public void afterResponseNotice(MyRequest request, MyResponse response, Map<String, String> deliver) {
//        String net_param_url = ContextManager.getInstance().getContext(getKey()).getProperty("net_param_url");
//        System.out.println(net_param_url);
        Thread thread = Thread.currentThread();
        System.out.println("Thread Name: " + thread.getName());
        System.out.println("Thread: " + thread);
        System.out.println("Thread Id: " + thread.getId());
        System.out.println("Thread Group: " + thread.getThreadGroup());
        System.out.println("Thread Priority: " + thread.getPriority());
    }

}
