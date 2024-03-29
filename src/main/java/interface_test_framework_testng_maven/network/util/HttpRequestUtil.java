package interface_test_framework_testng_maven.network.util;


import interface_test_framework_testng_maven.network.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtil {
    public static final String POST_URLENCODED = "POST_URLENCODED";

    public static final String POST_FORM_DATA = "POST_FORM_DATA";
    public static final String POST_RAW = "POST_RAW";

    private static MyRequest build(
            String url,
            String scheme,
            String host,
            String path,
            String port,
            String requestCharset,
            MyRequest.Method method,
            MyRequest.PostMethod postMethod,
            Map<String, String> data,
            Map<String, MultiText> formTextData,
            Map<String, MultiFile> formFileData,
            Raw raw
    ) throws IOException {


        if(null == data)
            data = new HashMap<>();
        if(null == formTextData)
            formTextData = new HashMap<>();
        if(null == formFileData)
            formFileData = new HashMap<>();
        if(null == raw)
            raw = new Raw();
        MyRequest myRequest = new MyRequest();
        myRequest.setUrl(url).setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .setPort(port)
                .setCharset(requestCharset)
                .setMethod(method)
                .setPostMethod(postMethod)
                .setMap(data)
                .setMultiMap(formTextData)
                .setFileMap(formFileData)
                .setRaw(raw);
        return myRequest;
    }
    public MyRequest buildRequest(
            String url,
            String scheme,
            String host,
            String path,
            String port,
            String requestCharset,
            MyRequest.Method method,
            MyRequest.PostMethod postMethod,
            Map<String, String> data,
            Map<String, MultiText> formTextData,
            Map<String, MultiFile> formFileData,
            Raw raw) throws IOException{
        return build(url,scheme, host, path, port, requestCharset, method, postMethod, data, formTextData, formFileData, raw);
    }
    public static MyRequest buildGetRequest(
            String url,
            String scheme,
            String host,
            String path,
            String port,
            String requestCharset,
            Map<String, String> data) throws IOException{
        return build(url, scheme, host, path, port, requestCharset, MyRequest.Method.GET, null, data, null, null, null);
    }
    public static MyRequest buildPostRequest(
            String url,
            String scheme,
            String host,
            String path,
            String port,
            String requestCharset,
            String postMethod,
            Map<String, String> data,
            Map<String, MultiText> formTextData,
            Map<String, MultiFile> formFileData,
            Raw raw) throws IOException{
        MyRequest.PostMethod pm = null;
        MyRequest request = null;
        switch (postMethod){
            case POST_URLENCODED:
                pm = MyRequest.PostMethod.URL_ENCODED;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, data, null, null, null);
                break;
            case POST_FORM_DATA:
                pm = MyRequest.PostMethod.FORM_DATA;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, null, formTextData, formFileData, null);
                break;
            case POST_RAW:
                pm = MyRequest.PostMethod.RAW;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, null, null, null, raw);
                break;
        }
        return request;
    }
    public static MyRequest buildPostRequest(String url, String scheme,
                                             String host,
                                             String path,
                                             String port,
                                             String requestCharset,
                                             Map<String, String> data) throws IOException {
        return buildPostRequest(url, scheme, host, path, port, requestCharset, POST_URLENCODED, data, null, null, null);
    }
    public static MyRequest buildPostRequest(String url, String scheme,
                                             String host,
                                             String path,
                                             String port,
                                             String requestCharset,
                                             Raw raw) throws IOException {
        return buildPostRequest(url, scheme, host, path, port, requestCharset, POST_RAW, null, null, null, raw);
    }
    public static byte[] get(String url, String scheme,
                             String host,
                             String path,
                             String port,
                             String requestCharset,
                             Map<String, String> data
    ) throws IOException {
        MyRequest request = buildGetRequest(url, scheme, host, path, port, requestCharset, data);
        MyResponse response = MyHttpClient.getInstance().request(request);
        return response.bytes();
    }
    public static byte[] get(String url) throws IOException {
        return get(url, null, null, null, null, null, null);
    }
    public static byte[] post(String url,String requestCharset,  Map<String, String> data) throws IOException {
        return post(url,null, null, null, null, requestCharset, data);
    }
    public static byte[] post(String url, String scheme,
                              String host,
                              String path,
                              String port,
                              String requestCharset,
                              String postMethod,
                              Map<String, String> data,
                              Map<String, MultiText> formTextData,
                              Map<String, MultiFile> formFileData,
                              Raw raw
    ) throws IOException {

        MyRequest.PostMethod pm = null;
        MyRequest request = null;
        switch (postMethod){
            case POST_URLENCODED:
                pm = MyRequest.PostMethod.URL_ENCODED;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, data, null, null, null);
                break;
            case POST_FORM_DATA:
                pm = MyRequest.PostMethod.FORM_DATA;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, null, formTextData, formFileData, null);
                break;
            case POST_RAW:
                pm = MyRequest.PostMethod.RAW;
                request = build(url, scheme, host, path, port, requestCharset, MyRequest.Method.POST, pm, null, null, null, raw);
                break;
        }

        MyResponse response = MyHttpClient.getInstance().request(request);
        return response.bytes();
    }

    /**
     * post urlencoded
     * @param scheme
     * @param host
     * @param path
     * @param port
     * @param requestCharset
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] post(String url, String scheme,
                              String host,
                              String path,
                              String port,
                              String requestCharset,
                              Map<String, String> data
    ) throws IOException {
        return post(url, scheme, host, path, port, requestCharset, POST_URLENCODED, data, null, null, null);
    }

    /**
     * raw
     * @param scheme
     * @param host
     * @param path
     * @param port
     * @param requestCharset
     * @param raw
     * @return
     * @throws IOException
     */
    public static byte[] post(String url,
                              String scheme,
                              String host,
                              String path,
                              String port,
                              String requestCharset,
                              Raw raw
    ) throws IOException {
        return post(url, scheme, host, path, port, requestCharset, POST_RAW, null, null, null, raw);
    }

    public static byte[] request(
            MyRequest request
    ) throws IOException {
        MyResponse response =MyHttpClient.getInstance().request(request);
        return response.bytes();
    }
    public static MyResponse requestResWithoutR(
            MyRequest request
    ) throws IOException {
        MyResponse response = MyHttpClient.getInstance().request(request);
        return response;
    }
}
