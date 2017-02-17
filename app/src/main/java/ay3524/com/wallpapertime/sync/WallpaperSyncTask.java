package ay3524.com.wallpapertime.sync;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.model.WallpaperUnsplash;
import ay3524.com.wallpapertime.utils.Constants;

import static android.content.Context.MODE_PRIVATE;
import static ay3524.com.wallpapertime.utils.Constants.API_KEY;
import static ay3524.com.wallpapertime.utils.Constants.PHOTO_CLIENT_ID;

/**
 * Created by Ashish on 06-01-2017.
 */

class WallpaperSyncTask {

    private ArrayList<WallpaperUnsplash> wallpapersArrayList = new ArrayList<>();
    private Context mContext;

    synchronized void setWallpaper(Context context) {
        mContext = context;
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        String id = sharedPreferences.getString(Constants.ID, Constants.DEFAULT_ID);

        String urls_collection_list = Constants.UNSPLASH_BASE_COLLECTION_CURATED + id + PHOTO_CLIENT_ID + API_KEY;
        getListOfWallpapers(urls_collection_list);
        //new GetBitmapTask().execute(url);
    }

    private int getRandomNo(int size) {
        Random r = new Random();
        return r.nextInt(size);
    }

    private void getListOfWallpapers(String url) {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();
                                JSONObject jsonObject = response.getJSONObject(i);

                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                wallpaperUnsplash.setUrls_full(jsonObject4.getString(Constants.FULL));

                                wallpapersArrayList.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        new GetBitmapTask().execute(wallpapersArrayList.get(getRandomNo(wallpapersArrayList.size())).getUrls_full());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
    }

    /*private String buildUrl(String fileName, String size) {
        StringBuilder stringBuilder = new StringBuilder(fileName);
        stringBuilder.delete(fileName.length() - 4, fileName.length());
        String url = "https://images.unsplash.com/" + stringBuilder.toString() + "?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=" + size + "&fit=max&s=c9cabfb90c6a844b59176db42be9ec0c";
        return url;
    }*/

    private class GetBitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap result = null;
            try {
                //Log.e("TAG",params[0]);
                result = Glide.with(mContext)
                        .load(params[0])
                        .asBitmap()
                        .into(-1, -1). // Width and height
                        get();

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);

                wallpaperManager.setBitmap(result);

            } catch (IOException | ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }
            return result;
        }
    }
}
