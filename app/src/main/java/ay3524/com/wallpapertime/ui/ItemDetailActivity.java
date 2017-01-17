package ay3524.com.wallpapertime.ui;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.utils.CircleTransform;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    String image_title, user_image_url, tags, user_profile_pixabay_link, image_pixabay_link, user;
    int id, download_count, view_count, like_count;
    TextView title, downloads, views, likes, user_name;
    ImageView userImage;
    private ProgressDialog pDialog;
    Button dwnld,set;
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

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading Image. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(true);

        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFileFromURL().execute(user_image_url);
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().toString() + fileName);
                if(file.exists()){
                    File sd = Environment.getExternalStorageDirectory();
                    File image = new File(sd+fileName, fileName);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        wallpaperManager.setBitmap(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(ItemDetailActivity.this, "IOException While Image Setting", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    new DownloadFileFromURL().execute(user_image_url);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                UUID uuid = UUID.randomUUID();
                fileName = uuid.toString();
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pDialog.dismiss();

            Toast.makeText(ItemDetailActivity.this, "Image Downloaded", Toast.LENGTH_SHORT).show();
        }
    }
}
