package interface_test_framework_testng_maven.network;


import interface_test_framework_testng_maven.data.IByteDataSource;
import interface_test_framework_testng_maven.data.file.StringPathFileByteDataSource;
import interface_test_framework_testng_maven.util.common.network.RxUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//单例
public class MyHttpClient {
    private long connectTimeout = 6;
    private long writeTimeout = 6;
    private long readTimeout = 6;

    public static MyHttpClient instance = new MyHttpClient();

    private MyHttpClient() {
        init();
    }

    private void init() {
        try {
            IByteDataSource byteDataSource = new StringPathFileByteDataSource(String.format("classpath:%s/netconfig.json", "config"), this.getClass());
            String json = new String(byteDataSource.getData(), "utf-8");
            //net config
            JSONObject netConfig = new JSONObject(json);
            connectTimeout = Integer.valueOf(netConfig.getInt("connectTimeout"));
            writeTimeout = Integer.valueOf(netConfig.getInt("writeTimeout"));
            readTimeout = Integer.valueOf(netConfig.getInt("readTimeout"));
            JSONObject commonHeaders = netConfig.getJSONObject("commonHeaders");
            Set<String> set = commonHeaders.keySet();
            for (String s : set) {
                this.addCommonHeader(s, commonHeaders.getString(s));
            }

        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static MyHttpClient getInstance() {
        return instance;
    }

    private Map<String, String> commonHeader = new HashMap<>();
    private  ThreadLocal<MyCookieJar> myCookieJarThreadLocal=new ThreadLocal<MyCookieJar>(){
        @Override
        protected MyCookieJar initialValue() {
            try {
                return new MyCookieJar();
            }catch (Exception e){
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    };
    private ThreadLocal<OkHttpClient> threadLocal = new ThreadLocal<OkHttpClient>() {
        @Override
        protected OkHttpClient initialValue() {


            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(RxUtils.createSSLSocketFactory())
                        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                        .readTimeout(readTimeout, TimeUnit.SECONDS)
                        .cookieJar(myCookieJarThreadLocal.get())
                        .hostnameVerifier(new RxUtils.TrustAllHostnameVerifier())
                        .build();

                return client;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    };


    public void updateCookieJar(){
        myCookieJarThreadLocal.get().clear();
    }




    public Map<String, String> getCommonHeader() {
        return commonHeader;
    }

    public void setCommonHeader(Map<String, String> commonHeader) {
        this.commonHeader = commonHeader;
    }

    public void addCommonHeader(String name, String value) {
        this.commonHeader.put(name, value);
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }


    /**
     * 你需要在外调用request.body.bytes() 或者request.body.string()或者request.body.close()来回收连接
     *
     * @param request
     * @return
     * @throws IOException
     */
    public MyResponse request(MyRequest request) throws IOException {
        request.setCommonHeaderMap(this.commonHeader);
        Call call = this.threadLocal.get().newCall(request.buildRequest());
        Response response = call.execute();
        return new MyResponse(response);
    }
}
