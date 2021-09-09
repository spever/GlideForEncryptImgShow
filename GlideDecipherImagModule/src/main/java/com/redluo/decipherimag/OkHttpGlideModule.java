package com.redluo.decipherimag;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@GlideModule
public class OkHttpGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));

        registry.append(EncryptedSrcDataBean.class, InputStream.class, new DeEncryptedModelLoader.Factory());
    }

    private static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder request = chain.request().newBuilder();

            /******** 可添加相关header *********/

            return chain.proceed(request.build());
        }
    }


    public static class UnsafeOkHttpClient {

        private static OkHttpClient okHttpClient = null;

        public static OkHttpClient getUnsafeOkHttpClient() {

            try {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    // Create a trust manager that does not validate certificate chains
                    final TrustManager[] trustAllCerts = new TrustManager[]{
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                }

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                                }

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return new java.security.cert.X509Certificate[]{};
                                }
                            }
                    };

                    // Install the all-trusting trust manager
                    final SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                    // Create an ssl socket factory with our all-trusting manager
                    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


                    builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                    builder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

                    builder.connectTimeout(60, TimeUnit.SECONDS);
                    builder.readTimeout(60, TimeUnit.SECONDS);
                    builder.addInterceptor(new HeaderInterceptor());
                    okHttpClient = builder.build();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return okHttpClient;
        }
    }

}
