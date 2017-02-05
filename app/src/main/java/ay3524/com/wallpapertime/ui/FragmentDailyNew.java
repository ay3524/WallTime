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
import ay3524.com.wallpapertime.adapter.WallpaperAdapter;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.model.WallpaperUnsplash;
import ay3524.com.wallpapertime.utils.Constants;

import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentDailyNew extends Fragment implements WallpaperAdapter.ListItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    ProgressBar pb;

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("FragmentDailyNew");
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
            adapter = new WallpaperAdapter(wallpapersList, FragmentDailyNew.this);
            recyclerView.setAdapter(adapter);
        } else {
            if (Constants.isConnected(getActivity())) {
                pb.setVisibility(View.VISIBLE);
                getListOfWallpapers();
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
        return rootView;
    }

    private void getListOfWallpapers() {

        JsonArrayRequest req = new JsonArrayRequest("https://api.unsplash.com/photos?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39&order_by=latest&per_page=30",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();
                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));
                                wallpaperUnsplash.setWidth(jsonObject.getString(Constants.WIDTH));
                                wallpaperUnsplash.setHeight(jsonObject.getString(Constants.HEIGHT));
                                wallpaperUnsplash.setColor(jsonObject.getString(Constants.COLOR));
                                wallpaperUnsplash.setLikes(jsonObject.getString(Constants.LIKES));

                                JSONObject jsonObject2 = jsonObject.getJSONObject(Constants.USER);
                                wallpaperUnsplash.setUser_id(jsonObject2.getString(Constants.ID));
                                JSONObject jsonObject3 = jsonObject2.getJSONObject(Constants.PROFILE_IMAGE);
                                wallpaperUnsplash.setProfile_image_small(jsonObject3.getString(Constants.SMALL_PROFILE_IMAGE));
                                wallpaperUnsplash.setProfile_image_medium(jsonObject3.getString(Constants.MEDIUM_PROFILE_IMAGE));
                                wallpaperUnsplash.setProfile_image_large(jsonObject3.getString(Constants.LARGE_PROFILE_IMAGE));

                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                wallpaperUnsplash.setUrls_raw(jsonObject4.getString(Constants.RAW));
                                wallpaperUnsplash.setUrls_full(jsonObject4.getString(Constants.FULL));
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.REGULAR));
                                wallpaperUnsplash.setUrls_small(jsonObject4.getString(Constants.SMALL));
                                wallpaperUnsplash.setUrls_thumb(jsonObject4.getString(Constants.THUMB));

                                wallpapersList.add(wallpaperUnsplash);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new WallpaperAdapter(wallpapersList, FragmentDailyNew.this);
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

        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);

        intent.putExtra("class", "Fragment");
        intent.putExtra(Constants.ID, wallpapersList.get(clickedItemIndex).getId());
        intent.putExtra(Constants.WIDTH, wallpapersList.get(clickedItemIndex).getWidth());
        intent.putExtra(Constants.HEIGHT, wallpapersList.get(clickedItemIndex).getHeight());
        intent.putExtra(Constants.COLOR, wallpapersList.get(clickedItemIndex).getColor());
        intent.putExtra(Constants.LIKES, wallpapersList.get(clickedItemIndex).getLikes());
        intent.putExtra(Constants.USER_ID, wallpapersList.get(clickedItemIndex).getUser_id());
        intent.putExtra(Constants.USER, wallpapersList.get(clickedItemIndex).getUsername());
        //intent.putExtra("name", wallpapersList.get(clickedItemIndex).getName());
        //intent.putExtra("first_name", wallpapersList.get(clickedItemIndex).getFirst_name());
        intent.putExtra(Constants.SMALL_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_small());
        intent.putExtra(Constants.MEDIUM_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_medium());
        intent.putExtra(Constants.LARGE_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_large());
        intent.putExtra(Constants.RAW, wallpapersList.get(clickedItemIndex).getUrls_raw());
        intent.putExtra(Constants.FULL, wallpapersList.get(clickedItemIndex).getUrls_full());
        intent.putExtra(Constants.REGULAR, wallpapersList.get(clickedItemIndex).getUrls_regular());
        intent.putExtra(Constants.SMALL, wallpapersList.get(clickedItemIndex).getUrls_small());
        intent.putExtra(Constants.THUMB, wallpapersList.get(clickedItemIndex).getUrls_thumb());

        startActivity(intent);
    }
}
