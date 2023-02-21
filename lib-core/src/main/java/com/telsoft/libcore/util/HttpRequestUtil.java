package com.telsoft.libcore.util;

import com.google.gson.Gson;
import com.telsoft.libcore.constant.ResultCode;
import com.telsoft.libcore.dto.ResponseMessageDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpRequestUtil {
    public enum REQUEST_TYPE {
        GET, POST, PUT, DELETE
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String CHAR_SET = "UTF-8";
    public static final String TPL_START_REQUEST = "HTTP REQUEST [{}] {} start";
    public static final String TPL_RESPONSE = "HTTP RESPONSE [{}] in {}ms, RESPONSE CODE {}";

    public static final int DEFAULT_TIMEOUT = 60;
    public int timeout;

    OkHttpClient client = new OkHttpClient();

    public String get(String url, String token) throws Exception {
        log.info("SEND GET url: {}", url);
        return executeToString(url, null, token, REQUEST_TYPE.GET);
    }

    public String post(String url, RequestBody formBody, String token) throws Exception {
        log.info("SEND POST url: {}", url);
        return executeToString(url, formBody, token, REQUEST_TYPE.POST);
    }

    public String put(String url, RequestBody formBody, String token) throws Exception {
        log.info("SEND PUT url: {}", url);
        return executeToString(url, formBody, token, REQUEST_TYPE.PUT);
    }

    public String delete(String url, RequestBody formBody, String token) throws Exception {
        log.info("SEND DELETE url: {}", url);
        return executeToString(url, formBody, token, REQUEST_TYPE.DELETE);
    }

    public String executeToString(String url, RequestBody formBody, String token, REQUEST_TYPE requestType) throws Exception {
        Response response = execute(url, formBody, token, requestType);

        String responseText = response.body().string();

//        Gson gson = Utils.getGsonUtc();
//        ResponseMessageDTO responseMessageDTO = gson.fromJson(responseText,ResponseMessageDTO.class);
        return responseText;
    }

    public Response execute(String url, RequestBody formBody, String token, REQUEST_TYPE requestType) throws Exception {
        long start = System.currentTimeMillis();
        log.info(TPL_START_REQUEST, start, requestType.name());

        String schema = url.split(":")[0];

        if ("https".equalsIgnoreCase(schema)) {
            long startHttps = System.currentTimeMillis();
            log.info("Start config HTTPS");
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

            }}, new java.security.SecureRandom());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            client = new OkHttpClient().newBuilder()
                    .callTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .sslSocketFactory(sslcontext.getSocketFactory(), trustManager)
                    .readTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .build();
            log.info("end config HTTPS: {}ms", System.currentTimeMillis() - startHttps);
        } else {
            client = new OkHttpClient().newBuilder()
                    .callTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout == 0 ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS)
                    .build();
        }
        Headers headers = null;
        if (token != null) {
            headers = new Headers.Builder()
                    .add("Accept", "application/json")
                    .add("Content-Type", "application/json")
//                .add("Internal-Request-Pwd", Const.INTERNAL_HTTP_REQUEST_PASSWORD)
                    .add("Authorization", token)
                    .build();

        } else {
            headers = new Headers.Builder()
                    .add("Accept", "*/*")
                    .add("Content-Type", "application/x-www-form-urlencoded")
//                .add("Internal-Request-Pwd", Const.INTERNAL_HTTP_REQUEST_PASSWORD)
//                    .add("Authorization", token)
                    .build();
        }


        Request request = null;

        switch (requestType) {
            case POST:
                request = new Request.Builder()
                        .url(url)
                        .headers(headers)
                        .post(formBody)
                        .build();
                break;
            case PUT:
                request = new Request.Builder()
                        .url(url)
                        .headers(headers)
                        .put(formBody)
                        .build();
                break;
            case DELETE:
                request = new Request.Builder()
                        .url(url)
                        .headers(headers)
                        .delete(formBody)
                        .build();
                break;
            case GET:
                request = new Request.Builder()
                        .url(url)
                        .headers(headers)
                        .build();
        }

        Response response = client.newCall(request).execute();
        log.info(TPL_RESPONSE, start, System.currentTimeMillis() - start, response.code());
        return response;
    }
}
