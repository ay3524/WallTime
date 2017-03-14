package ay3524.com.wallpapertime.sync;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashish on 06-01-2017.
 */

class WallpaperSyncTask {

    private Context mContext;
    private ArrayList<String> urlsList = new ArrayList<>();

    synchronized void setWallpaper(Context context) {
        mContext = context;
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        int position = sharedPreferences.getInt(Constants.CATEGORY, Constants.DEFAULT_CATEGORY);
        String category = Constants.getCategoryList().get(position);

        String url_for_a_category = Constants.PIXABAY_URI.concat(category).concat("&per_page=200");

        getListOfWallpapers(url_for_a_category);
    }

    private int getRandomNo(int size) {
        Random r = new Random();
        return r.nextInt(size);
    }

    private void getListOfWallpapers(String url) {


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("TAG", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String fullHDURL = jsonObject.getString("fullHDURL");

                                urlsList.add(fullHDURL);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new GetBitmapTask().execute(urlsList.get(getRandomNo(urlsList.size())));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
    }

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
