package ay3524.com.wallpapertime.ui;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
public class ItemDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    String image_title, user_image_url, tags, user_profile_pixabay_link, image_pixabay_link, user, web_format_url, preview_url, image_720p_link;
    int id, download_count, view_count, like_count;
    TextView title, downloads, views, likes, user_name;
    ImageView userImage;
    //private ProgressDialog pDialog;
    Button dwnld, set;
    String fileName;
    //private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.imageTitle);
        downloads = (TextView) findViewById(R.id.downloads);
        views = (TextView) findViewById(R.id.views);
        likes = (TextView) findViewById(R.id.likes);
        user_name = (TextView) findViewById(R.id.user_name);
        userImage = (ImageView) findViewById(R.id.userImage);

        if (getIntent().getExtras() != null) {
            id = getIntent().getIntExtra(Constants.IMAGE_ID, 0);
            download_count = getIntent().getIntExtra(Constants.DOWNLOADS, 0);
            view_count = getIntent().getIntExtra(Constants.VIEWS, 0);
            like_count = getIntent().getIntExtra(Constants.LIKES, 0);

            tags = getIntent().getStringExtra(Constants.TAGS);
            String[] tagsList = tags.split(",");
            image_title = tagsList[0];
            image_pixabay_link = getIntent().getStringExtra(Constants.PIXABAY_PAGE_URL);
            user_image_url = getIntent().getStringExtra(Constants.USER_IMAGE_URL);
            user = getIntent().getStringExtra(Constants.USER);
            web_format_url = getIntent().getStringExtra(Constants.WEB_FORMAT_URL);
            preview_url = getIntent().getStringExtra(Constants.PREVIEW_URL);
            String splitted[] = preview_url.split("/");
            fileName = splitted[splitted.length - 1];
            //Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
            image_720p_link = buildHDURL();

        }
        title.setText(image_title);
        downloads.setText(String.valueOf(download_count));
        views.setText(String.valueOf(view_count));
        likes.setText(String.valueOf(like_count));
        user_name.setText(user);
        dwnld = (Button) findViewById(R.id.dwnld);
        set = (Button) findViewById(R.id.set_as_wallpaper);
        Glide.with(this).load(user_image_url)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);

        //window = getWindow();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final ImageView image = (ImageView) findViewById(R.id.image);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra(Constants.WEB_FORMAT_URL)).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(image);

        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(new File(Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(image_720p_link);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Image Already Downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(new File(Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName).exists())) {
                    new ImageDownloadTask(ItemDetailActivity.this, fileName).execute(image_720p_link);
                    setAsWallpaper();

                } else {
                    setAsWallpaper();
                    Toast.makeText(ItemDetailActivity.this, "Setting wallpaper successfull", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setAsWallpaper() {
        File image_file = new File(Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName);
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
        Toast.makeText(this, myName.toString(), Toast.LENGTH_LONG).show();
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
}
