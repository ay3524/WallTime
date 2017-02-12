package ay3524.com.wallpapertime.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import ay3524.com.wallpapertime.R;

public class DownloadDetailActivity extends AppCompatActivity {

    SubsamplingScaleImageView imageView;
    String fileURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (SubsamplingScaleImageView) findViewById(R.id.image);

        //imageView.setPanLimit(PAN_LIMIT_OUTSIDE);
        //imageView.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
        //imageView.setPanEnabled(false);

        if (getIntent().getExtras() != null) {
            fileURL = getIntent().getStringExtra("url_file");
        }
        Glide.with(getApplicationContext())
                .load(fileURL)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        imageView.setImage(ImageSource.bitmap(bitmap));
                    }
                });
    }

}
