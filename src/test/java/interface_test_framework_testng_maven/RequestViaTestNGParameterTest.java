package interface_test_framework_testng_maven;


import interface_test_framework_testng_maven.guice.module.factory.ByteDataSourceModuleFactory;
import interface_test_framework_testng_maven.network.util.HttpRequestUtil;
import interface_test_framework_testng_maven.test.CommonBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Epic("账户模块")
@Feature("登陆")
@Guice(moduleFactory = ByteDataSourceModuleFactory.class)
public class RequestViaTestNGParameterTest extends CommonBase {





    @Story("正常登陆")
    @Test(description = "通过testNG参数获取接口信息测试")
    public void login(){
        Map<String, String> allParameters = getAllParameters();

        Map<String, String> parameter = new HashMap<>();
        parameter.put("mid", allParameters.get("mid"));


        try {
            byte[] bytes = HttpRequestUtil.get(null, allParameters.get("scheme"),
                    allParameters.get("host"),
                    allParameters.get("path"),
                    allParameters.get("port"),
                    allParameters.get("requestCharset"),
                    parameter
                    );

            String result = new String(bytes, allParameters.get("responseCharset"));
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
