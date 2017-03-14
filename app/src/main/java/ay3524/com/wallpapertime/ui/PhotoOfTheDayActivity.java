package ay3524.com.wallpapertime.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.utils.Constants;
import ay3524.com.wallpapertime.utils.ImageDownloadTask;
import ay3524.com.wallpapertime.utils.SetWallpaperTask;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;

public class PhotoOfTheDayActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.background)
    SubsamplingScaleImageView today_photo;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.dwnld)
    Button dwnld;
    @BindView(R.id.set_as_wallpaper)
    Button set;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    private String fileName;
    private String image_path_with_folder;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkDetailActivity));
        }
        setContentView(R.layout.activity_photo_of_the_day);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setLogo(R.drawable.coollogo);
        }

        today_photo = (SubsamplingScaleImageView) findViewById(R.id.background);
        today_photo.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);

        dwnld.setOnClickListener(this);
        set.setOnClickListener(this);

        checkPermissionForMarshmallowAndAbove();

        getListOfPhotos();
    }

    private void getListOfPhotos() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.BING_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray(Constants.IMAGES);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String titleString = jsonObject.getString(Constants.COPYRIGHT);
                                title.setText(titleString);
                                url = jsonObject.getString(Constants.URL);

                                String split[] = url.split(Constants.SEPERATOR);
                                fileName = split[1] + Constants.JPG;
                                image_path_with_folder = Environment.getExternalStorageDirectory().toString()
                                        + Constants.WALLTIME_PATH_DOUBLE + fileName;

                                Glide.with(getApplicationContext())
                                        .load(buildURL(url, Constants.PHOTO_WDTH_240, Constants.PHOTO_HEIGHT_320))
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                // Do something with bitmap here.
                                                today_photo.setImage(ImageSource.bitmap(bitmap));

                                                Glide.with(getApplicationContext())
                                                        .load(buildURL(url, Constants.PHOTO_WDTH_720, Constants.PHOTO_HEIGHT_1280))
                                                        .asBitmap()
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .into(new SimpleTarget<Bitmap>() {
                                                            @Override
                                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                                // Do something with bitmap here.
                                                                today_photo.setImage(ImageSource.bitmap(bitmap));
                                                            }
                                                        });
                                            }
                                        });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq,
                Constants.TAG_JSON_OBJECT);

    }

    private String buildURL(String url, String width, String height) {

        StringBuilder stringBuilder = new StringBuilder(url);

        stringBuilder.delete(url.length() - 13, url.length());
        return Constants.BING_BASE_URL + stringBuilder.toString() + width + Constants.X + height + Constants.JPG;
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
                checkPermissionForMarshmallowAndAbove();
                if (url != null) {
                    if (!(new File(image_path_with_folder).exists())) {

                        new ImageDownloadTask(PhotoOfTheDayActivity.this, fileName,false)
                                .execute(buildURL(url, getString(R.string.imageWidth_1920), getString(R.string.imageWidth_1080)));
                    } else {
                        Toast.makeText(PhotoOfTheDayActivity.this, getString(R.string.dwnld_image), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.set_as_wallpaper:
                checkPermissionForMarshmallowAndAbove();
                if (url != null) {
                    if (!(new File(image_path_with_folder).exists())) {
                        new ImageDownloadTask(PhotoOfTheDayActivity.this, fileName,true)
                                .execute(buildURL(url, getString(R.string.imageWidth_1920), getString(R.string.imageWidth_1080)));

                    } else {
                        new SetWallpaperTask(PhotoOfTheDayActivity.this,image_path_with_folder).execute();
                    }
                }
                break;
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
                        builder.setMessage(getString(R.string.permission_message));
                        builder.setTitle(getString(R.string.storage));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PhotoOfTheDayActivity.this, storage_permissions, 0);
                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(this, storage_permissions, 0);
                    }
                } else {
                    ActivityCompat.requestPermissions(PhotoOfTheDayActivity.this,
                            storage_permissions,
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }

            }
        }
    }
}
