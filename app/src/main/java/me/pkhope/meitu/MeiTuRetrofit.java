package me.pkhope.meitu;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pkhope on 2016/5/4.
 */
public class MeiTuRetrofit {

//    private static MeiTuRetrofit meiTuRetrofit = new MeiTuRetrofit();
    private MeiTuApi meituServerce;

    MeiTuRetrofit(){

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Cookie", "is_click=1")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.topit.me")
 //               .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new MyConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();
        meituServerce = retrofit.create(MeiTuApi.class);
    }

    public MeiTuApi getMeituServerce(){
         return meituServerce;
    }
}
