package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class CollectionActivity extends AppCompatActivity implements WallpaperAdapter.ListItemClickListener {

    private String urls_raw;
    private String urls_full;
    private String urls_regular;
    private String urls_small;
    private String urls_thumb;
    private String title;
    private String id;
    private String total_photos;
    private String urls_collection_list;

    private static final String STATE_WALLPAPERS = "state";
    private RecyclerView recyclerView;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private String tag_json_arry = "TAG_JSON_ARRAY";
    ProgressBar pb;
    private RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyView = (RelativeLayout) findViewById(R.id.empty_view);

        pb = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);

        if (getIntent().getExtras() != null) {

            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            total_photos = getIntent().getStringExtra("total_photos");
            urls_raw = getIntent().getStringExtra("urls_raw");
            urls_full = getIntent().getStringExtra("urls_full");
            urls_regular = getIntent().getStringExtra("urls_regular");
            urls_small = getIntent().getStringExtra("urls_small");
            urls_thumb = getIntent().getStringExtra("urls_thumb");
            urls_collection_list = "https://api.unsplash.com/collections/curated/" + id + "/photos?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39";
            //String splitted[] = urls_raw.split("/");
            //fileName = splitted[splitted.length - 1]+".jpg";
            //image_path_with_folder = Environment.getExternalStorageDirectory().toString() + "/WallTime/" + fileName;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        if (savedInstanceState != null) {
            wallpapersList = savedInstanceState.getParcelableArrayList(STATE_WALLPAPERS);
            adapter = new WallpaperAdapter(wallpapersList, CollectionActivity.this);
            recyclerView.setAdapter(adapter);
        } else {
            if (Constants.isConnected(getApplicationContext())) {
                pb.setVisibility(View.VISIBLE);
                getListOfWallpapers();
                emptyView.setVisibility(View.GONE);
            } else {

                emptyView.setVisibility(View.VISIBLE);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    private void getListOfWallpapers() {

        JsonArrayRequest req = new JsonArrayRequest(urls_collection_list,
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
                        adapter = new WallpaperAdapter(wallpapersList, CollectionActivity.this);
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
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ItemListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);

        intent.putExtra("class", "Activity");
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
