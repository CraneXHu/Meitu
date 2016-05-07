package me.pkhope.meitu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.pkhope.meitu.R;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pkhope on 2016/5/5.
 */
public class MeiTuAdapter extends RecyclerView.Adapter<MeiTuAdapter.MeiTuViewHolder>{

    private List<ImageData> mList;
    private Context mContext;

    private ImageDownloader mImageDownloader;

    public MeiTuAdapter(Context context,List<ImageData> list) {
        super();
        mContext = context;
        mImageDownloader = new ImageDownloader(mContext);
        mList = list;
    }

    @Override
    public MeiTuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meitu,parent,false);
        return new MeiTuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeiTuViewHolder holder, int position) {
        ImageData data = mList.get(position);

//        Glide.with(mContext)
//                .load(data.imageUrl)
//                .centerCrop()
//                .into(holder.imageView);

        mImageDownloader.getDownloader().load(data.imageUrl)
//                .resize(100,100)
//                .centerCrop()
                .into(holder.imageView);
//        holder.textView.setText(data.imageUrl);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MeiTuViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_iv)
        ImageView imageView;

//        @Bind(R.id.item_tv)
//        TextView textView;

        public MeiTuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
