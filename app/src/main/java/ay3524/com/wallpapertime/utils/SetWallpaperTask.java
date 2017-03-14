package ay3524.com.wallpapertime.utils;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ay3524.com.wallpapertime.R;

/**
 * Created by Ashish on 28-02-2017.
 */

public class SetWallpaperTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog pDialog;
    private String path1;

    public SetWallpaperTask(Context cxt, String path) {
        context = cxt;
        path1 = path;
        pDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage(context.getString(R.string.setting_wall_for_you));
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        pDialog.setCancelable(false);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        setAsWallpaper();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mWakeLock.release();
        pDialog.dismiss();
        Toast.makeText(context, R.string.successfully_set, Toast.LENGTH_SHORT).show();
    }

    private void setAsWallpaper() {
        try {
            File image_file = new File(path1);
            Bitmap bitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(bitmap);
                //Toast.makeText(ItemDetailActivity.this, "Successfully set as wallpaper :)", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                //Toast.makeText(ItemDetailActivity.this, getString(R.string.error_set_image), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (NullPointerException ignored) {
        }
    }
}
