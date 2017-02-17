package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import static ay3524.com.wallpapertime.utils.Constants.PHOTO_CLIENT_ID;
import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

public class CollectionActivity extends AppCompatActivity implements WallpaperAdapter.ListItemClickListener {


    private String title;
    private String urls_collection_list;

    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        recyclerView.setHasFixedSize(true);

        if (getIntent().getExtras() != null) {

            String id = getIntent().getStringExtra(Constants.ID);
            title = getIntent().getStringExtra(Constants.TITLE);

            //String total_photos = getIntent().getStringExtra(Constants.TOTAL_PHOTOS);
            urls_collection_list = Constants.UNSPLASH_BASE_COLLECTION_CURATED +"/"+ id + PHOTO_CLIENT_ID + CLIENT_ID + API_KEY;
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        Constants.setGridLayoutManager(getApplicationContext(),recyclerView);

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
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));

                                wallpaperUnsplash.setLikes(jsonObject.getString(Constants.LIKES));

                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.REGULAR));

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
                //VolleyLog.d("TAG", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req,
                Constants.TAG_JSON_ARRAY);
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

        intent.putExtra(Constants.CLASS, Constants.ACTIVITY);
        intent.putExtra(Constants.ID, wallpapersList.get(clickedItemIndex).getId());

        intent.putExtra(Constants.LIKES, wallpapersList.get(clickedItemIndex).getLikes());

        intent.putExtra(Constants.REGULAR, wallpapersList.get(clickedItemIndex).getUrls_regular());


        startActivity(intent);
    }
}
