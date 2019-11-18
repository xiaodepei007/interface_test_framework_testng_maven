package interface_test_framework_testng_maven.context;

import interface_test_framework_testng_maven.data.IByteDataSource;
import interface_test_framework_testng_maven.data.file.StringPathFileByteDataSource;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * context上下文类
 */
public class Context {

    public static final String REQUEST_PARSED_TYPE = "REQUEST_PARSED_TYPE";   //请求语法分析类型
    public static final String RESPONSE_PARSED_TYPE = "RESPONSE_PARSED_TYPE";  //相应语法分析类型
    private String key;     //名字
    private String pathPrefix; //路径前缀
    private  ContextObservable contextObservable = new ContextObservable() ;//一个被订阅类
    public   Map<String,String> mapProperties = new HashMap<>();   //存放配置的map


    public Context(String key, String pathPrefix) {
        this.key = key;
        this.pathPrefix = pathPrefix;
    }

    //初始化，读套件名下面的配置的方法
    public void init(){
        loadConfig();
    }

    public void loadConfig(){

        try{
            IByteDataSource byteDataSource = new StringPathFileByteDataSource(String.format("classpath:%s/%s/config.json",pathPrefix, "context"), this.getClass());
            String json = new String(byteDataSource.getData(), "utf-8");
            //先初始化一下请求工具
            dealWithConfigJson(json);

            byteDataSource = new StringPathFileByteDataSource(String.format("classpath:%s/%s/config.properties",pathPrefix, "context"), this.getClass());
            Properties p = new Properties();
            InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(byteDataSource.getData()),"utf-8");
            p.load(reader);
            reader.close();
            for (String propertyName : p.stringPropertyNames()) {
                mapProperties.put(propertyName, p.getProperty(propertyName));
            }
        }catch (Throwable e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }
    private void dealWithConfigJson(String json){
        JSONObject root = new JSONObject(json);
        //properties
        JSONObject properties = root.getJSONObject("properties");
        for (String s : properties.keySet()) {
            mapProperties.put(s, properties.getString(s));
        }
    }
    public Set<String> propertyKeySet(){
        return this.mapProperties.keySet();
    }
    public String getProperty(String key){
        return mapProperties.get(key);
    }

    public Map<String, String> getMapProperties() {
        Map<String, String> temp = new HashMap<>();
        temp.putAll(mapProperties);
        return temp;
    }

    //给map添加配置方法
    public void addProperty(String key, String value){
        mapProperties.put(key, value);
    }

    //增加观察者类
    public void addObserver(String type, Observer observer){
        contextObservable.addObserver(type, observer);
    }
    //通知类
    public void notice(String type, Object arg){
        contextObservable.notice(type, arg);
    }
}
