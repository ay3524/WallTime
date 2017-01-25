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
import ay3524.com.wallpapertime.adapter.WallpaperCategoryAdapter;
import ay3524.com.wallpapertime.app.AppController;
import ay3524.com.wallpapertime.model.WallpaperCollection;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentCategories extends Fragment implements WallpaperCategoryAdapter.ListItemClickListener{


    private static final String STATE_WALLPAPERS = "state";
    RecyclerView recyclerView;
    WallpaperCategoryAdapter adapter;
    ArrayList<WallpaperCollection> wallpapersList = new ArrayList<>();
    private String tag_json_arry = "TAG_JSON_ARRAY";
    private GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.item_list);

        recyclerView.setHasFixedSize(true);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        if (savedInstanceState != null) {
            wallpapersList = savedInstanceState.getParcelableArrayList(STATE_WALLPAPERS);
            adapter = new WallpaperCategoryAdapter(wallpapersList, FragmentCategories.this);
            recyclerView.setAdapter(adapter);
        } else {
            getListOfCollections();
        }

        return rootView;

    }

    private void getListOfCollections() {

        JsonArrayRequest req = new JsonArrayRequest("https://api.unsplash.com/collections/curated?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperCollection wallpaperUnsplash = new WallpaperCollection();
                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString("id"));
                                wallpaperUnsplash.setTitle(jsonObject.getString("title"));
                                wallpaperUnsplash.setTotal_photos(jsonObject.getString("total_photos"));

                                JSONObject jsonObject3 = jsonObject.getJSONObject("cover_photo");
                                JSONObject jsonObject4 = jsonObject3.getJSONObject("urls");
                                wallpaperUnsplash.setUrls_raw(jsonObject4.getString("raw"));
                                wallpaperUnsplash.setUrls_full(jsonObject4.getString("full"));
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString("regular"));
                                wallpaperUnsplash.setUrls_small(jsonObject4.getString("small"));
                                wallpaperUnsplash.setUrls_thumb(jsonObject4.getString("thumb"));

                                /*wallpaperUnsplash.setId(jsonObject.getString("id"));
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
                                wallpaperUnsplash.setUrls_thumb(jsonObject4.getString("thumb"));*/

                                wallpapersList.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new WallpaperCategoryAdapter(wallpapersList, FragmentCategories.this);
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
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);
    }
}

