package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.model.WallpaperCollection;
import ay3524.com.wallpapertime.utils.Constants;

import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentCollections extends Fragment implements WallpaperCategoryAdapter.ListItemClickListener {

    RecyclerView recyclerView;
    WallpaperCategoryAdapter adapter;
    ArrayList<WallpaperCollection> wallpapersList = new ArrayList<>();
    private ProgressBar pb;

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("FragmentCollections");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        RelativeLayout emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_view);

        pb = (ProgressBar) rootView.findViewById(R.id.progressBar);

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
            adapter = new WallpaperCategoryAdapter(wallpapersList, FragmentCollections.this);
            recyclerView.setAdapter(adapter);

        } else {
            if (Constants.isConnected(getActivity())) {
                pb.setVisibility(View.VISIBLE);
                getListOfCollections();
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
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
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));
                                wallpaperUnsplash.setTitle(jsonObject.getString(Constants.TITLE));
                                wallpaperUnsplash.setTotal_photos(jsonObject.getString(Constants.TOTAL_PHOTOS));

                                JSONObject jsonObject3 = jsonObject.getJSONObject(Constants.COVER_PHOTOS);
                                JSONObject jsonObject4 = jsonObject3.getJSONObject(Constants.URLS);
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.REGULAR));
                                //String splitted[] = jsonObject4.getString(Constants.REGULAR).split("/");
                                //String fileName = splitted[splitted.length - 1] + ".jpg";
                                //wallpaperUnsplash.setUrls_small(fileName);
                                //wallpaperUnsplash.setUrls_raw(jsonObject4.getString(Constants.RAW));
                                //wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.FULL));
                                //wallpaperUnsplash.setUrls_thumb(jsonObject4.getString(Constants.THUMB));

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
                        adapter = new WallpaperCategoryAdapter(wallpapersList, FragmentCollections.this);
                        recyclerView.setAdapter(adapter);
                        pb.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), CollectionActivity.class);

        intent.putExtra(Constants.ID, wallpapersList.get(clickedItemIndex).getId());
        intent.putExtra(Constants.TITLE, wallpapersList.get(clickedItemIndex).getTitle());
        intent.putExtra(Constants.TOTAL_PHOTOS, wallpapersList.get(clickedItemIndex).getTotal_photos());
        //intent.putExtra(Constants.RAW, wallpapersList.get(clickedItemIndex).getUrls_raw());
        //intent.putExtra(Constants.FULL, wallpapersList.get(clickedItemIndex).getUrls_full());
        //intent.putExtra(Constants.REGULAR, wallpapersList.get(clickedItemIndex).getUrls_regular());
        //intent.putExtra(Constants.SMALL, wallpapersList.get(clickedItemIndex).getUrls_small());
        //intent.putExtra(Constants.THUMB, wallpapersList.get(clickedItemIndex).getUrls_thumb());

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);
    }
}