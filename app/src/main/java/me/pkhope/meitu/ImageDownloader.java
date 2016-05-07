package me.pkhope.meitu;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pkhope on 2016/5/6.
 */
public class ImageDownloader {

    private Context mContext;
    private Picasso mPicasso;

    ImageDownloader(Context context){
        mContext = context;

        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        mPicasso = new Picasso.Builder(mContext)
                .downloader(new OkHttp3Downloader(httpClient))
                .build();
    }

    public Picasso getDownloader(){
        return mPicasso;
    }
}
