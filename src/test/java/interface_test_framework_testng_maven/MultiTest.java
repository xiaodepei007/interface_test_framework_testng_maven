package interface_test_framework_testng_maven;


import interface_test_framework_testng_maven.test.CommonBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

public class MultiTest extends CommonBase {

    @BeforeClass
    public void bc(){
        System.out.println("before class:"+new Date());
    }

    @Test
    public void t1(){
        System.out.println("before t1:"+getMethodName());
    }
    @Test
    public void t2(){
        System.out.println("before t2:"+getMethodName());
    }
}
