package me.pkhope.meitu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by pkhope on 2016/5/4.
 */
public class MyResponseConverter<T> implements Converter<ResponseBody,T> {

    @Override
    public T convert(ResponseBody value) throws IOException {

        List<ImageData> imgs = new ArrayList<>();
        String html = value.string();
        Document doc = Jsoup.parse(html);
        Elements links = doc.getElementsByTag("img");
        for (Element link : links){
            if (link.attr("id").contains("item")){
                String url = link.attr("src");
                if (url.contains(".gif")){
                    url = link.attr("data-original");
                }
                ImageData img = new ImageData();
                img.imageUrl = url;
                imgs.add(img);
            }

        }
        return (T)imgs;
    }
}
