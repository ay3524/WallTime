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
import ay3524.com.wallpapertime.adapter.WallpaperCategoryAdapter;
import ay3524.com.wallpapertime.app.MyApplication;
import ay3524.com.wallpapertime.model.WallpaperCollection;
import ay3524.com.wallpapertime.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static ay3524.com.wallpapertime.utils.Constants.API_KEY;
import static ay3524.com.wallpapertime.utils.Constants.CLIENT_ID;
import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentCollections extends Fragment implements WallpaperCategoryAdapter.ListItemClickListener {

    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    WallpaperCategoryAdapter adapter;
    ArrayList<WallpaperCollection> wallpapersList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        ButterKnife.bind(this,rootView);

        recyclerView.setHasFixedSize(true);

        Constants.setGridLayoutManager(getActivity(), recyclerView);

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

        JsonArrayRequest req = new JsonArrayRequest(Constants.UNSPLASH_BASE_COLLECTION_CURATED + CLIENT_ID+API_KEY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("TAG", response.toString());
                        pb.setVisibility(View.VISIBLE);
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
                Toast.makeText(getActivity(), getString(R.string.slow_message_toast), Toast.LENGTH_SHORT).show();
                if (wallpapersList.size() == 0) {
                    getListOfCollections();
                }
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
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

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);
    }
}