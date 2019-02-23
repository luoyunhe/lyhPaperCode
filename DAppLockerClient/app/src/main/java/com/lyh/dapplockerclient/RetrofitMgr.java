package com.lyh.dapplockerclient;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMgr {
    private static RetrofitMgr retrofitMgr;
    private Retrofit retrofit;
    private RetrofitMgr() {
        initRetrofit();
    }

    public static synchronized RetrofitMgr getInstance() {
        if (retrofitMgr == null) {
            retrofitMgr = new RetrofitMgr();
        }
        return retrofitMgr;
    }

    private void initRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new  OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.sslSocketFactory(getSSLSocketFactory(), new CustomTrustManager());
        builder.hostnameVerifier(getHostnameVerifier());
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public <T> T createService(Class<T> Sevice) {
        return retrofit.create(Sevice);
    }


    public static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
    public static class CustomTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier   hostnameVerifier= (hostname, session) -> true;
        return hostnameVerifier;
    }
}
