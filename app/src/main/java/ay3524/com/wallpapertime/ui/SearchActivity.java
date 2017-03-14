package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
import static ay3524.com.wallpapertime.utils.Constants.PIXABAY_API_KEY;
import static ay3524.com.wallpapertime.utils.Constants.STATE_WALLPAPERS;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, WallpaperAdapter.ListItemClickListener {

    String query;
    String url;
    private ArrayList<WallpaperUnsplash> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String search_base_url = "https://api.unsplash.com/search/photos?page=1&query=";
    String client_id = "client_id=".concat(API_KEY);
    String and = "&";
    private String total_photos;
    @BindView(R.id.empty_subtitle_text)
    TextView subtitle_text;
    private String pixabay_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setLogo(R.drawable.coollogo);
        }

        recyclerView.setHasFixedSize(true);

        Constants.setGridLayoutManager(getApplicationContext(), recyclerView);

        if (getIntent().getExtras() != null) {
            query = getIntent().getStringExtra(Constants.QUERY);
            url = search_base_url + query + and + client_id;
            pixabay_url = "https://pixabay.com/api/?key="+PIXABAY_API_KEY+"&q="+query+"&image_type=photo&pretty=true&per_page=30";
        }

        if (savedInstanceState != null) {
            wallpapersList = savedInstanceState.getParcelableArrayList(STATE_WALLPAPERS);
            adapter = new WallpaperAdapter(wallpapersList, SearchActivity.this);
            recyclerView.setAdapter(adapter);
        } else {
            if (Constants.isConnected(getApplicationContext())) {
                pb.setVisibility(View.VISIBLE);
                getListOfWallpapers(url);
                emptyView.setVisibility(View.GONE);
            } else {
                subtitle_text.setText(R.string.there_might_be_some_problem_with_internet_connection);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getListOfWallpapers(final String url) {
        emptyView.setVisibility(View.GONE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            total_photos = response.getString("total");
                            //Toast.makeText(SearchActivity.this, total_photos, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = response.getJSONArray(Constants.RESULTS);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));
                                wallpaperUnsplash.setLikes(jsonObject.getString(Constants.LIKES));

                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                String buildSingleListImageUrl = Constants.buildUrl(jsonObject4.getString(Constants.REGULAR), Constants.PHOTO_SIZE_300);
                                wallpaperUnsplash.setUrls_regular(buildSingleListImageUrl);

                                wallpapersList.add(wallpaperUnsplash);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pb.setVisibility(View.GONE);
                        if(total_photos.equals("0")){
                            emptyView.setVisibility(View.VISIBLE);
                        }else{
                            adapter = new WallpaperAdapter(wallpapersList, SearchActivity.this);
                            recyclerView.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                emptyView.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.query_wrong), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq,
                Constants.TAG_JSON_OBJECT);
    }

    private void getListOfPixabayWallpapers(final String url) {
        emptyView.setVisibility(View.GONE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            total_photos = response.getString("total");
                            //Toast.makeText(SearchActivity.this, total_photos, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = response.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                WallpaperUnsplash wallpaperUnsplash = new WallpaperUnsplash();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                wallpaperUnsplash.setId(jsonObject.getString(Constants.ID));
                                wallpaperUnsplash.setLikes(jsonObject.getString(Constants.LIKES));

                                JSONObject jsonObject4 = jsonObject.getJSONObject(Constants.URLS);
                                wallpaperUnsplash.setUrls_regular(jsonObject4.getString(Constants.REGULAR));

                                wallpapersList.add(wallpaperUnsplash);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pb.setVisibility(View.GONE);
                        if(total_photos.equals("0")){
                            emptyView.setVisibility(View.VISIBLE);
                        }else{
                            adapter = new WallpaperAdapter(wallpapersList, SearchActivity.this);
                            recyclerView.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                emptyView.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.query_wrong), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjReq,
                Constants.TAG_JSON_OBJECT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setQuery(query, false);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String newQuery) {

        if (!query.equals(newQuery.trim())) {
            if (Constants.isConnected(getApplicationContext())) {
                wallpapersList.clear();
                pb.setVisibility(View.VISIBLE);
                String newURL = search_base_url + newQuery + and + client_id;
                getListOfWallpapers(newURL);
                emptyView.setVisibility(View.GONE);
                query = newQuery;
            } else {
                subtitle_text.setText(R.string.there_might_be_some_problem_with_internet_connection);
                emptyView.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, getString(R.string.try_diff), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
        intent.putExtra(Constants.CLASS, Constants.SEARCHACTIVITY);
        intent.putExtra(Constants.ID, wallpapersList.get(clickedItemIndex).getId());
        intent.putExtra(Constants.LIKES, wallpapersList.get(clickedItemIndex).getLikes());
        intent.putExtra(Constants.REGULAR, wallpapersList.get(clickedItemIndex).getUrls_regular());

        startActivity(intent);

        searchView.clearFocus();
    }
}
