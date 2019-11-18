package interface_test_framework_testng_maven;


import interface_test_framework_testng_maven.annotation.NamedParam;
import interface_test_framework_testng_maven.test.CommonBase;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class NetParamTest extends CommonBase {
    @Test
    public void no(){
        System.out.println(getNetParams());
    }
    @Test
    public void has(@NamedParam("net_config_param")ITestContext iTestContext){
        System.out.println(getNetParams());
    }
    @Test
    public void has_no(@NamedParam("no_config_param")ITestContext iTestContext){
        System.out.println(getNetParams());
    }
}
