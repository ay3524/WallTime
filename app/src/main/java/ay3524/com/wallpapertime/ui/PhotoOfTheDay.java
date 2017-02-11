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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.utils.Constants;
import ay3524.com.wallpapertime.utils.ImageDownloadTask;

public class PhotoOfTheDay extends AppCompatActivity implements View.OnClickListener {

    ImageView today_photo;
    TextView title;
    Button dwnld, set;
    private String fileName;
    private String image_path_with_folder;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_photo_of_the_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        today_photo = (ImageView) findViewById(R.id.background);
        title = (TextView) findViewById(R.id.title);
        dwnld = (Button) findViewById(R.id.dwnld);
        dwnld.setOnClickListener(this);
        set = (Button) findViewById(R.id.set_as_wallpaper);
        set.setOnClickListener(this);

        checkPermissionForMarshmallowAndAbove();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListOfPhotos();
    }

    private void getListOfPhotos() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("images");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String titleString = jsonObject.getString("copyright");
                                title.setText(titleString);
                                url = jsonObject.getString("url");

                                String split[] = url.split("_");
                                fileName = split[1];
                                image_path_with_folder = Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName;

                                Glide.with(getApplicationContext())
                                        .load(buildURL(url, "240", "320"))
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                // Do something with bitmap here.
                                                today_photo.setImageBitmap(bitmap);

                                                Glide.with(getApplicationContext())
                                                        .load(buildURL(url, "480", "640"))
                                                        .asBitmap()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                                // Do something with bitmap here.
                                                                today_photo.setImageBitmap(bitmap);
                                                            }
                                                        });
                                            }
                                        });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Log.d(TAG, response.toString());
                        //msgResponse.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq,
                Constants.TAG_JSON_OBJECT);

    }

    private String buildURL(String url, String width, String height) {

        StringBuilder stringBuilder = new StringBuilder(url);

        stringBuilder.delete(url.length() - 13, url.length());
        return "http://www.bing.com" + stringBuilder.toString() + width + "x" + height + ".jpg";
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
            case R.id.dwnld:
                if (url != null) {
                    if (!(new File(image_path_with_folder).exists())) {

                        new ImageDownloadTask(PhotoOfTheDay.this, fileName).execute(buildURL(url, "1280", "720"));
                    } else {
                        Toast.makeText(PhotoOfTheDay.this, "Image Already Downloaded", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.set_as_wallpaper:
                if (url != null) {
                    if (!(new File(image_path_with_folder).exists())) {
                        new ImageDownloadTask(PhotoOfTheDay.this, fileName).execute(buildURL(url, "1280", "720"));
                        setAsWallpaper();

                    } else {
                        setAsWallpaper();
                        Toast.makeText(PhotoOfTheDay.this, "Setting wallpaper successfull", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
                Toast.makeText(PhotoOfTheDay.this, "Error While Image Setting", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (NullPointerException ignored) {
        }
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
                                ActivityCompat.requestPermissions(PhotoOfTheDay.this, storage_permissions, 0);
                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(this, storage_permissions, 0);
                    }
                } else {
                    ActivityCompat.requestPermissions(PhotoOfTheDay.this,
                            storage_permissions,
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }

            }
        }
    }
}
