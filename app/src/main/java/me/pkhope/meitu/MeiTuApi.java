package me.pkhope.meitu;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pkhope on 2016/4/28.
 */
public interface MeiTuApi {

    @GET("/")
    Observable<List<ImageData>> getImageData(@Query("p") int page);
}
