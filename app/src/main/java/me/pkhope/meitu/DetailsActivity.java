package me.pkhope.meitu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import me.pkhope.meitu.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pkhope on 2016/5/17.
 */
public class DetailsActivity extends AppCompatActivity{

    static final String TRANSIT_PIC = "picture";

//    @Bind(R.id.appbar)
    private AppBarLayout mAppBarLayout;
//
//    @Bind(R.id.toolbar)
    private Toolbar mToolbar;

    private boolean mIsHidden = false;

    @Bind(R.id.picture)
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToolbar.setTitle(getString(R.string.app_name));

        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);

        String imageUrl = getIntent().getStringExtra("image_url");
        new ImageDownloader(this).getDownloader().load(imageUrl)
                .into(mImageView);

        mAppBarLayout.setAlpha(0.7f);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowToolbar();
            }
        });
    }

    protected void hideOrShowToolbar() {
        mAppBarLayout.animate()
                .translationY(mIsHidden ? 0 : -mAppBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();

        mIsHidden = !mIsHidden;
    }
}
