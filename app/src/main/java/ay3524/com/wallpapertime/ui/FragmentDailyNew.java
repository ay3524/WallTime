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
import ay3524.com.wallpapertime.app.AppController;
import ay3524.com.wallpapertime.model.WallpaperUnsplash;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentDailyNew extends Fragment implements WallpaperAdapter.ListItemClickListener {
    private static final String STATE_WALLPAPERS = "state";
    private RecyclerView recyclerView;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private String tag_json_arry = "TAG_JSON_ARRAY";
    ProgressBar pb;
    private RelativeLayout emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_view);

        pb = (ProgressBar) rootView.findViewById(R.id.progressBar);

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

        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
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
        intent.putExtra("id", wallpapersList.get(clickedItemIndex).getId());
        intent.putExtra("width", wallpapersList.get(clickedItemIndex).getWidth());
        intent.putExtra("height", wallpapersList.get(clickedItemIndex).getHeight());
        intent.putExtra("color", wallpapersList.get(clickedItemIndex).getColor());
        intent.putExtra("likes", wallpapersList.get(clickedItemIndex).getLikes());
        intent.putExtra("user_id", wallpapersList.get(clickedItemIndex).getUser_id());
        intent.putExtra("username", wallpapersList.get(clickedItemIndex).getUsername());
        intent.putExtra("name", wallpapersList.get(clickedItemIndex).getName());
        intent.putExtra("first_name", wallpapersList.get(clickedItemIndex).getFirst_name());
        intent.putExtra("profile_image_small", wallpapersList.get(clickedItemIndex).getProfile_image_small());
        intent.putExtra("profile_image_medium", wallpapersList.get(clickedItemIndex).getProfile_image_medium());
        intent.putExtra("profile_image_large", wallpapersList.get(clickedItemIndex).getProfile_image_large());
        intent.putExtra("urls_raw", wallpapersList.get(clickedItemIndex).getUrls_raw());
        intent.putExtra("urls_full", wallpapersList.get(clickedItemIndex).getUrls_full());
        intent.putExtra("urls_regular", wallpapersList.get(clickedItemIndex).getUrls_regular());
        intent.putExtra("urls_small", wallpapersList.get(clickedItemIndex).getUrls_small());
        intent.putExtra("urls_thumb", wallpapersList.get(clickedItemIndex).getUrls_thumb());

        startActivity(intent);
    }
}
