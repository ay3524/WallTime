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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import ay3524.com.wallpapertime.utils.CircleTransform;
import ay3524.com.wallpapertime.utils.Constants;
import ay3524.com.wallpapertime.utils.ImageDownloadTask;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    String image_title, user_image_url, tags, user_profile_pixabay_link, image_pixabay_link, user, web_format_url, preview_url, image_720p_link;
    int id, download_count, view_count, like_count;
    TextView title, downloads, views, likes, user_name;
    ImageView userImage;
    Button dwnld, set;
    String fileName, image_path_with_folder;
    String tagsList[];
    TextView firstTag, secondTag, thirdTag;
    String hash = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        checkPermissionForMarshmallowAndAbove();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.imageTitle);
        downloads = (TextView) findViewById(R.id.downloads);
        views = (TextView) findViewById(R.id.views);
        likes = (TextView) findViewById(R.id.likes);
        user_name = (TextView) findViewById(R.id.user_name);
        userImage = (ImageView) findViewById(R.id.userImage);
        firstTag = (TextView) findViewById(R.id.firstTag);
        secondTag = (TextView) findViewById(R.id.secondTag);
        thirdTag = (TextView) findViewById(R.id.thirdTag);

        if (getIntent().getExtras() != null) {
            id = getIntent().getIntExtra(Constants.IMAGE_ID, 0);
            download_count = getIntent().getIntExtra(Constants.DOWNLOADS, 0);
            view_count = getIntent().getIntExtra(Constants.VIEWS, 0);
            like_count = getIntent().getIntExtra(Constants.LIKES, 0);

            tags = getIntent().getStringExtra(Constants.TAGS);
            tagsList = tags.split(",");
            image_title = tagsList[0];
            image_pixabay_link = getIntent().getStringExtra(Constants.PIXABAY_PAGE_URL);
            user_image_url = getIntent().getStringExtra(Constants.USER_IMAGE_URL);
            user = getIntent().getStringExtra(Constants.USER);
            web_format_url = getIntent().getStringExtra(Constants.WEB_FORMAT_URL);
            preview_url = getIntent().getStringExtra(Constants.PREVIEW_URL);
            String splitted[] = preview_url.split("/");
            fileName = splitted[splitted.length - 1];
            image_path_with_folder = Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName;
            image_720p_link = buildHDURL();
        }
        title.setText(image_title);
        downloads.setText(String.valueOf(download_count));
        views.setText(String.valueOf(view_count));
        likes.setText(String.valueOf(like_count));
        user_name.setText(user);
        firstTag.setText(hash.concat(tagsList[0].trim()));
        secondTag.setText(hash.concat(tagsList[1].trim()));
        thirdTag.setText(hash.concat(tagsList[2].trim()));
        dwnld = (Button) findViewById(R.id.dwnld);
        dwnld.setOnClickListener(this);
        set = (Button) findViewById(R.id.set_as_wallpaper);
        set.setOnClickListener(this);
        Glide.with(this).load(user_image_url)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final ImageView image = (ImageView) findViewById(R.id.image);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Constants.WEB_FORMAT_URL)).crossFade()
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
        File image_file = new File(image_path_with_folder);
        Bitmap bitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(ItemDetailActivity.this, "Error While Image Setting", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String buildHDURL() {
        StringBuilder myName = new StringBuilder(preview_url);
        myName.setCharAt(preview_url.length() - 7, '1');
        myName.setCharAt(preview_url.length() - 6, '2');
        myName.setCharAt(preview_url.length() - 5, '8');
        myName.insert(preview_url.length() - 4, '0');
        return myName.toString();
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
            case R.id.fab:
                Snackbar.make(v, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.dwnld:

                if (!(new File(image_path_with_folder).exists())) {

                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(image_720p_link);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Image Already Downloaded", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_as_wallpaper:

                if (!(new File(image_path_with_folder).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(image_720p_link);
                    setAsWallpaper();

                } else {
                    setAsWallpaper();
                    Toast.makeText(ItemDetailActivity.this, "Setting wallpaper successfull", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
