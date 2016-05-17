package me.pkhope.meitu;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.pkhope.meitu.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private MeiTuAdapter mMeiTuAdapter;
    private List<ImageData> mImageList;

    private MeiTuRetrofit mMeiTuRetrofit;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.app_name));

        mImageList = new ArrayList<>();

        mMeiTuRetrofit = new MeiTuRetrofit();

        mCurrentPage = 1;

 //       loadData(mCurrentPage);
        setUpSwipeRefreshLayout();
        setUpRecyclerView();
    }

    protected void loadData(int page){
        mMeiTuRetrofit.getMeituServerce().getImageData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ImageData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ImageData> imageDatas) {
                        mImageList.addAll(imageDatas);
                        mMeiTuAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setUpSwipeRefreshLayout(){

        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                R.color.refresh_progress_2, R.color.refresh_progress_1);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override public void onRefresh() {
                        loadData(mCurrentPage++);
                    }
                });

    }

    protected void setUpRecyclerView(){
        final StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mMeiTuAdapter = new MeiTuAdapter(this,mImageList);
        mRecyclerView.setAdapter(mMeiTuAdapter);

        mMeiTuAdapter.setOnPicTouchListener(getPicTouchListener());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1];
                boolean isBottom = lastVisibleItem >= mMeiTuAdapter.getItemCount() - 4;

                if (isBottom && dy > 0) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadData(mCurrentPage++);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        }, 300);

        loadData(mCurrentPage++);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public OnPicTouchListener getPicTouchListener(){
        return new OnPicTouchListener() {
            @Override
            public void onTouch(View v, View imageView, ImageData data) {
                if (data == null){
                    return;
                }
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("image_url",data.imageUrlLarge);
                ActivityOptionsCompat optionsCompat
                        = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, imageView, DetailsActivity.TRANSIT_PIC);
                try {
                    ActivityCompat.startActivity(MainActivity.this, intent,
                            optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
            }
        };
    }
}