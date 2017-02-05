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

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button dwnld, set;
    private String fileName, image_path_with_folder;
    private String urls_raw, urls_full, urls_small, urls_regular, urls_thumb;
    private String activityOrFragment;
    ImageView image;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("ItemDetailActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        checkPermissionForMarshmallowAndAbove();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            activityOrFragment = getIntent().getStringExtra("class");
            urls_raw = getIntent().getStringExtra(Constants.RAW);
            urls_full = getIntent().getStringExtra(Constants.FULL);
            urls_regular = getIntent().getStringExtra(Constants.REGULAR);
            urls_small = getIntent().getStringExtra(Constants.SMALL);
            urls_thumb = getIntent().getStringExtra(Constants.THUMB);
            String splitted[] = urls_raw.split("/");
            fileName = splitted[splitted.length - 1] + ".jpg";
            image_path_with_folder = Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName;
        }

        dwnld = (Button) findViewById(R.id.dwnld);
        dwnld.setOnClickListener(this);
        set = (Button) findViewById(R.id.set_as_wallpaper);
        set.setOnClickListener(this);

        /*Glide.with(this).load(getIntent().getStringExtra())
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);*/

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        image = (ImageView) findViewById(R.id.background);
        final String url = buildUrl(fileName,"800");
        Glide.with(getApplicationContext())
                .load(urls_thumb)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        // Do something with bitmap here.
                        image.setImageBitmap(bitmap);

                        Glide.with(getApplicationContext())
                                .load(url)
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
        /*Glide.with(getApplicationContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(image);*/
    }

    private String buildUrl(String fileName,String size) {
        StringBuilder stringBuilder = new StringBuilder(fileName);
        stringBuilder.delete(fileName.length() - 4, fileName.length());
        String url = "https://images.unsplash.com/"+stringBuilder.toString()+"?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w="+size+"&fit=max&s=c9cabfb90c6a844b59176db42be9ec0c";
        return url;
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
                        builder.setMessage("To get storage access you have to allow us access to your sd card content.");
                        builder.setTitle("Storage");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                Toast.makeText(ItemDetailActivity.this, "Error While Image Setting", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (activityOrFragment.equalsIgnoreCase("Fragment")) {
                Intent intent = new Intent(this, ItemListActivity.class);
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

                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(buildUrl(fileName,"1100"));
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Image Already Downloaded", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_as_wallpaper:

                if (!(new File(image_path_with_folder).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(buildUrl(fileName,"1100"));
                    setAsWallpaper();

                } else {
                    setAsWallpaper();
                    Toast.makeText(ItemDetailActivity.this, "Setting wallpaper successfull", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
