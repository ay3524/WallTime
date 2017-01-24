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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.utils.ImageDownloadTask;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView userImage;
    Button dwnld, set;
    String fileName, image_path_with_folder;
    String tagsList[];
    TextView firstTag, secondTag, thirdTag;
    String hash = "#";
    private String urls_raw;
    private String urls_full;
    private String urls_small;
    private String urls_regular;
    private String urls_thum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        checkPermissionForMarshmallowAndAbove();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            urls_raw = getIntent().getStringExtra("urls_raw");
            urls_full = getIntent().getStringExtra("urls_full");
            urls_regular = getIntent().getStringExtra("urls_regular");
            urls_small = getIntent().getStringExtra("urls_small");
            urls_thum = getIntent().getStringExtra("urls_thumb");
            String splitted[] = urls_raw.split("/");
            fileName = splitted[splitted.length - 1]+".jpg";
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

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final ImageView image = (ImageView) findViewById(R.id.image);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("urls_regular")).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
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
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.fab:
                Snackbar.make(v, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;*/
            case R.id.dwnld:

                if (!(new File(image_path_with_folder).exists())) {

                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(urls_full);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Image Already Downloaded", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_as_wallpaper:

                if (!(new File(image_path_with_folder).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(urls_full);
                    setAsWallpaper();

                } else {
                    setAsWallpaper();
                    Toast.makeText(ItemDetailActivity.this, "Setting wallpaper successfull", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
