package me.pkhope.meitu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import me.pkhope.meitu.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(DetailsActivity.this)
                        .setMessage(getString(R.string.save_picture))
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();

                                        String imageUrl = getIntent().getStringExtra("image_url");
                                        rx.Observable.just(imageUrl).map(new Func1<String, Bitmap>() {
                                            @Override
                                            public Bitmap call(String s) {
                                                Bitmap bitmap = null;
                                                try {
                                                    bitmap = new ImageDownloader(DetailsActivity.this).getDownloader().load(s).get();

                                                }catch (IOException e){
                                                    e.printStackTrace();
                                                }
                                                return bitmap;
                                            }
                                        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Bitmap>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(Bitmap bitmap) {

                                                if (bitmap == null){
                                                    Toast.makeText(DetailsActivity.this, R.string.save_failed, Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                File appDir = new File(Environment.getExternalStorageDirectory(), "Meitu");
                                                if (!appDir.exists()) {
                                                    appDir.mkdir();
                                                }

                                                SimpleDateFormat df = new SimpleDateFormat("yyMMdd-HHmmss");
                                                Date date = new Date();
                                                String fileName = df.format(date) + ".jpg";
                                                File file = new File(appDir, fileName);
                                                try {
                                                    FileOutputStream fos = new FileOutputStream(file);
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                    fos.flush();
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(DetailsActivity.this, R.string.save_succeed, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                        .show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_details,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideOrShowToolbar() {
        mAppBarLayout.animate()
                .translationY(mIsHidden ? 0 : -mAppBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();

        mIsHidden = !mIsHidden;
    }
}
