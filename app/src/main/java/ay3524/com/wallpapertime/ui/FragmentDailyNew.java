package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.wallpapertime.utils.Constants.API_KEY;
import static ay3524.com.wallpapertime.utils.Constants.CLIENT_ID;
import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentDailyNew extends Fragment implements WallpaperAdapter.ListItemClickListener {

    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;

    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        ButterKnife.bind(this, rootView);

        recyclerView.setHasFixedSize(true);

        Constants.setGridLayoutManager(getActivity(), recyclerView);

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

        JsonArrayRequest req = new JsonArrayRequest(Constants.UNSPLASH_BASE_PHOTO + CLIENT_ID + API_KEY + Constants.ORDER_BY + Constants.LATEST +Constants.PER_PAGE+ Constants.PER_PAGE_COUNT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());
                        pb.setVisibility(View.VISIBLE);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();
                                JSONObject jsonObject = response.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));

                                wallpaperUnsplash.setLikes(jsonObject.getString(Constants.LIKES));


                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                String buildSingleListImageUrl = Constants.buildUrl(jsonObject4.getString(Constants.REGULAR), Constants.PHOTO_SIZE_300);
                                wallpaperUnsplash.setUrls_regular(buildSingleListImageUrl);
                                //wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.REGULAR));

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
                Toast.makeText(getActivity(), getString(R.string.slow_message_toast), Toast.LENGTH_SHORT).show();
                if (wallpapersList.size() == 0) {
                    getListOfWallpapers();
                }
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
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

        intent.putExtra(Constants.CLASS, Constants.FRAGMENT);
        intent.putExtra(Constants.ID, wallpapersList.get(clickedItemIndex).getId());
        //intent.putExtra(Constants.WIDTH, wallpapersList.get(clickedItemIndex).getWidth());
        //intent.putExtra(Constants.HEIGHT, wallpapersList.get(clickedItemIndex).getHeight());
        //intent.putExtra(Constants.COLOR, wallpapersList.get(clickedItemIndex).getColor());
        intent.putExtra(Constants.LIKES, wallpapersList.get(clickedItemIndex).getLikes());
        //intent.putExtra(Constants.USER_ID, wallpapersList.get(clickedItemIndex).getUser_id());
        //intent.putExtra(Constants.USER, wallpapersList.get(clickedItemIndex).getUsername());
        //intent.putExtra("name", wallpapersList.get(clickedItemIndex).getName());
        //intent.putExtra("first_name", wallpapersList.get(clickedItemIndex).getFirst_name());
        //intent.putExtra(Constants.SMALL_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_small());
        //intent.putExtra(Constants.MEDIUM_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_medium());
        //intent.putExtra(Constants.LARGE_PROFILE_IMAGE, wallpapersList.get(clickedItemIndex).getProfile_image_large());
        //intent.putExtra(Constants.RAW, wallpapersList.get(clickedItemIndex).getUrls_raw());
        //intent.putExtra(Constants.FULL, wallpapersList.get(clickedItemIndex).getUrls_full());
        intent.putExtra(Constants.REGULAR, wallpapersList.get(clickedItemIndex).getUrls_regular());
        //intent.putExtra(Constants.SMALL, wallpapersList.get(clickedItemIndex).getUrls_small());
        //intent.putExtra(Constants.THUMB, wallpapersList.get(clickedItemIndex).getUrls_thumb());

        startActivity(intent);
    }
}
