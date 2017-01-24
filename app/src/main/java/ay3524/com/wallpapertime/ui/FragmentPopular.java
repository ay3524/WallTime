package ay3524.com.wallpapertime.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.WallpaperAdapter;
import ay3524.com.wallpapertime.app.AppController;
import ay3524.com.wallpapertime.model.WallpaperUnsplash;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentPopular extends Fragment implements WallpaperAdapter.ListItemClickListener {
    private static final String STATE_WALLPAPERS = "state";
    private RecyclerView recyclerView;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    private String tag_json_arry = "TAG_JSON_ARRAY";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.item_list);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        if (savedInstanceState != null) {
            wallpapersList = savedInstanceState.getParcelableArrayList(STATE_WALLPAPERS);
            //adapter = new WallpaperAdapter(wallpapersList, FragmentPopular.this);
           // recyclerView.setAdapter(adapter);
        } else {
            getListOfWallpapers();
        }

        return rootView;

    }

    private void getListOfWallpapers() {


        JsonArrayRequest req = new JsonArrayRequest("https://api.unsplash.com/photos?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39&order_by=popular&per_page=30",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();
                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString("id"));
                                wallpaperUnsplash.setWidth(jsonObject.getString("width"));
                                wallpaperUnsplash.setHeight(jsonObject.getString("height"));
                                wallpaperUnsplash.setColor(jsonObject.getString("color"));
                                wallpaperUnsplash.setLikes(jsonObject.getString("likes"));

                                JSONObject jsonObject2 = jsonObject.getJSONObject("user");
                                wallpaperUnsplash.setUser_id(jsonObject2.getString("id"));
                                JSONObject jsonObject3 = jsonObject2.getJSONObject("profile_image");
                                wallpaperUnsplash.setProfile_image_small(jsonObject3.getString("small"));
                                wallpaperUnsplash.setProfile_image_medium(jsonObject3.getString("medium"));
                                wallpaperUnsplash.setProfile_image_large(jsonObject3.getString("large"));

                                JSONObject jsonObject4 = jsonObject.getJSONObject("urls");
                                wallpaperUnsplash.setUrls_raw(jsonObject4.getString("raw"));
                                wallpaperUnsplash.setUrls_full(jsonObject4.getString("full"));
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString("regular"));
                                wallpaperUnsplash.setUrls_small(jsonObject4.getString("small"));
                                wallpaperUnsplash.setUrls_thumb(jsonObject4.getString("thumb"));

                                wallpapersList.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new WallpaperAdapter(wallpapersList, FragmentPopular.this);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        /*ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<WallpaperWithInfoResponse> call = apiService.getWallpaperPopularOrLatest(Constants.api_key,
                Constants.response_group_img_details, Constants.booleanImagesWithoutEditorsCoice,
                Constants.orderPopular, Constants.prettyBooleanValue, Constants.image_type_photo);
        call.enqueue(new Callback<WallpaperWithInfoResponse>() {
            @Override
            public void onResponse(Call<WallpaperWithInfoResponse> call, Response<WallpaperWithInfoResponse> response) {
                try {
                    if (response != null) {
                        wallpapersList = response.body().getHits();
                       // adapter = new WallpaperAdapter(wallpapersList, FragmentPopular.this);
                       // recyclerView.setAdapter(adapter);
                    }
                } catch (NullPointerException ignored) {
                    Toast.makeText(getActivity(), "NPE Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallpaperWithInfoResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}

