package ay3524.com.wallpapertime.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.IOException;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.utils.Constants;
import ay3524.com.wallpapertime.utils.ImageDownloadTask;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.wallpapertime.utils.Constants.buildUrl;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String fileName, image_path_with_folder;
    private String urls_regular;
    private String activityOrFragment;
    @BindView(R.id.background)
    ImageView image;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.dwnld)
    Button dwnld;
    @BindView(R.id.set_as_wallpaper)
    Button set;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkDetailActivity));
        }
        setContentView(R.layout.item_detail);

        ButterKnife.bind(this);

        checkPermissionForMarshmallowAndAbove();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            activityOrFragment = getIntent().getStringExtra(Constants.CLASS);
            String id = getIntent().getStringExtra(Constants.ID);
            urls_regular = getIntent().getStringExtra(Constants.REGULAR);

            fileName = id + Constants.JPG;
            image_path_with_folder = Environment.getExternalStorageDirectory().toString() + Constants.WALLTIME_PATH + fileName;
        }

        dwnld.setOnClickListener(this);

        set.setOnClickListener(this);


        Glide.with(getApplicationContext())
                .load(buildUrl(urls_regular, Constants.PHOTO_SIZE_200))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        image.setImageBitmap(bitmap);

                        Glide.with(getApplicationContext())
                                .load(buildUrl(urls_regular, Constants.PHOTO_SIZE_800))
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                        // Do something with bitmap here.
                                        image.setImageBitmap(bitmap);
                                    }
                                });
                    }
                });
    }

    private void checkPermissionForMarshmallowAndAbove() {
        final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
        final String[] storage_permissions =
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(getString(R.string.permission_message));
                        builder.setTitle(getString(R.string.storage));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ItemDetailActivity.this, storage_permissions, 0);
                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(this, storage_permissions, 0);
                    }
                } else {
                    ActivityCompat.requestPermissions(ItemDetailActivity.this,
                            storage_permissions,
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }

            }
        }
    }

    private void setAsWallpaper() {
        try {
            File image_file = new File(image_path_with_folder);
            Bitmap bitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                //Toast.makeText(ItemDetailActivity.this, getString(R.string.error_set_image), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (activityOrFragment.equalsIgnoreCase(Constants.FRAGMENT)) {
                Intent intent = new Intent(this, ItemListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                navigateUpTo(intent);
                return true;
            }
            if (activityOrFragment.equalsIgnoreCase(Constants.SEARCHACTIVITY)) {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                navigateUpTo(intent);
                return true;
            } else {
                Intent intent = new Intent(this, CollectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                navigateUpTo(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dwnld:

                if (!(new File(image_path_with_folder).exists())) {

                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(buildUrl(urls_regular, Constants.PHOTO_SIZE_1100));
                } else {
                    Toast.makeText(ItemDetailActivity.this, getString(R.string.dwnld_image), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_as_wallpaper:

                if (!(new File(image_path_with_folder).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(buildUrl(urls_regular, Constants.PHOTO_SIZE_1100));
                    setAsWallpaper();

                } else {
                    setAsWallpaper();
                }
                break;
        }
    }
}
