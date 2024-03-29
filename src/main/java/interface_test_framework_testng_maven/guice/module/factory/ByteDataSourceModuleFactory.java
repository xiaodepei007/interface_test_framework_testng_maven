package interface_test_framework_testng_maven.guice.module.factory;

import com.google.inject.Module;
import interface_test_framework_testng_maven.guice.module.ByteDataSourceModule;
import interface_test_framework_testng_maven.guice.module.annotation.GuiceByteDataSource;
import org.testng.IModuleFactory;
import org.testng.ITestContext;

public class ByteDataSourceModuleFactory implements IModuleFactory {
    @Override
    public Module createModule(ITestContext context, Class<?> testClass) {
        ByteDataSourceModule module = new ByteDataSourceModule();
        GuiceByteDataSource byteDataSource = testClass.getAnnotation(GuiceByteDataSource.class);
        if(null != byteDataSource){
            String filePath = byteDataSource.filePath();
            Class cls = byteDataSource.cls();
            module.setCls(cls);
            module.setFilePath(filePath);
        }
        return module;
    }
}
