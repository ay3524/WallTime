package ay3524.com.wallpapertime.sync;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ay3524.com.wallpapertime.app.AppController;
import ay3524.com.wallpapertime.model.WallpaperUnsplash;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashish on 06-01-2017.
 */

public class WallpaperSyncTask {

    private ArrayList<WallpaperUnsplash> wallpapersArrayList = new ArrayList<>();
    private Context mContext;
    private String urls_collection_list;
    private String tag_json_arry = "TAG_JSON_ARRAY";
    String id;

    synchronized public void setWallpaper(Context context) {
        mContext = context;
        SharedPreferences sharedPreferences = context.
                getSharedPreferences("ay3524.com.wallpapertime", MODE_PRIVATE);

        id = sharedPreferences.getString("id", "134");

        urls_collection_list = "https://api.unsplash.com/collections/curated/" + id + "/photos?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39";
        getListOfWallpapers(urls_collection_list);
        //new GetBitmapTask().execute(url);
    }

    private int getRandomNo() {
        Random r = new Random();
        return r.nextInt(10);
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

                                JSONObject jsonObject4 = jsonObject.getJSONObject("urls");
                                wallpaperUnsplash.setUrls_raw(jsonObject4.getString("raw"));
                                //wallpaperUnsplash.setUrls_full(jsonObject4.getString("full"));
                                //wallpaperUnsplash.setUrls_regular(jsonObject4.getString("regular"));
                                //wallpaperUnsplash.setUrls_small(jsonObject4.getString("small"));
                                //wallpaperUnsplash.setUrls_thumb(jsonObject4.getString("thumb"));

                                wallpapersArrayList.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        wallpapersArrayList.get(getRandomNo()).getUrls_raw();

                        String splitted[] = wallpapersArrayList.get(getRandomNo()).getUrls_raw().split("/");
                        String photoName = splitted[splitted.length - 1] + ".jpg";
                        String url = buildUrl(photoName, "900");
                        new GetBitmapTask().execute(url);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pb.setVisibility(View.GONE);
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        /*ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<WallpaperResponse> call = apiService.getWallpaper(Constants.api_key,
                Constants.response_group, Constants.nature, Constants.booleanImagesWithEditorsCoice,
                Constants.orderPopular, Constants.prettyBooleanValue);
        call.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {
                try {
                    if (response != null) {
                        wallpapersArrayList = response.body().getWallpapers();
                        url = wallpapersArrayList.get(getRandomNo()).getLargeImageURL();
                        new GetBitmapTask().execute(url);
                        //Log.e("WallpaperSyncUtils",wallpapersArrayList.get(0).getFullHDURL());
                        //return wallpapersArrayList;
                    }
                } catch (NullPointerException ignored) {
                    //Toast.makeText(getActivity(), "NPE Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });*/
    }

    private String buildUrl(String fileName, String size) {
        StringBuilder stringBuilder = new StringBuilder(fileName);
        stringBuilder.delete(fileName.length() - 4, fileName.length());
        String url = "https://images.unsplash.com/" + stringBuilder.toString() + "?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=" + size + "&fit=max&s=c9cabfb90c6a844b59176db42be9ec0c";
        return url;
    }

    private class GetBitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap result = null;
            try {
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);

                wallpaperManager.setBitmap(bitmap);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
