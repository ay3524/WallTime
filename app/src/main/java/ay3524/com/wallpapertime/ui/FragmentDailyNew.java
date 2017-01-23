package ay3524.com.wallpapertime.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ay3524.com.wallpapertime.model.WallpaperWithInfo;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentDailyNew extends Fragment implements WallpaperAdapter.ListItemClickListener {
    private static final String STATE_WALLPAPERS = "state";
    private RecyclerView recyclerView;
    private ArrayList<WallpaperWithInfo> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private String tag_json_arry = "TAG_JSON_ARRAY";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.item_list);

        //gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        //recyclerView.setLayoutManager(gridLayoutManager);
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
            getListOfWallpapers();
        }

        return rootView;

    }

    private void getListOfWallpapers() {

        JsonArrayRequest req = new JsonArrayRequest("https://api.unsplash.com/collections/491510/photos?client_id=1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        //TextView textView = (TextView) findViewById(R.id.title);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                /*JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");*/

                                /*JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                                String id = jsonObject1.getString("id");*/
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                                String id = jsonObject1.getString("id");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("profile_image");
                                String url = jsonObject2.getString("small");
                                //textView.append(url + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //textView.setText(response.toString());


                        //msgResponse.setText(response.toString());
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
                Constants.orderLatest, Constants.prettyBooleanValue, "photo");
        call.enqueue(new Callback<WallpaperWithInfoResponse>() {
            @Override
            public void onResponse(Call<WallpaperWithInfoResponse> call, Response<WallpaperWithInfoResponse> response) {
                try {
                    if (response != null) {
                        wallpapersList = response.body().getHits();
                        adapter = new WallpaperAdapter(wallpapersList, FragmentDailyNew.this);
                        recyclerView.setAdapter(adapter);
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
        //Toast.makeText(getActivity(), "Phone", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);

        intent.putExtra(Constants.PREVIEW_HEIGHT, wallpapersList.get(clickedItemIndex).getPreviewHeight());
        intent.putExtra(Constants.PREVIEW_WIDTH, wallpapersList.get(clickedItemIndex).getPreviewWidth());
        intent.putExtra(Constants.WEB_FORMAT_HEIGHT, wallpapersList.get(clickedItemIndex).getWebformatHeight());
        intent.putExtra(Constants.WEB_FORMAT_WIDTH, wallpapersList.get(clickedItemIndex).getWebformatWidth());
        intent.putExtra(Constants.IMAGE_HEIGHT, wallpapersList.get(clickedItemIndex).getImageHeight());
        intent.putExtra(Constants.IMAGE_WIDTH, wallpapersList.get(clickedItemIndex).getImageWidth());

        intent.putExtra(Constants.LIKES, wallpapersList.get(clickedItemIndex).getLikes());
        intent.putExtra(Constants.FAVORITES, wallpapersList.get(clickedItemIndex).getFavorites());
        intent.putExtra(Constants.VIEWS, wallpapersList.get(clickedItemIndex).getViews());
        intent.putExtra(Constants.COMMENTS, wallpapersList.get(clickedItemIndex).getComments());
        intent.putExtra(Constants.DOWNLOADS, wallpapersList.get(clickedItemIndex).getDownloads());
        intent.putExtra(Constants.TAGS, wallpapersList.get(clickedItemIndex).getTags());

        intent.putExtra(Constants.PIXABAY_PAGE_URL, wallpapersList.get(clickedItemIndex).getPageURL());
        intent.putExtra(Constants.PREVIEW_URL, wallpapersList.get(clickedItemIndex).getPreviewURL());
        intent.putExtra(Constants.USER_IMAGE_URL, wallpapersList.get(clickedItemIndex).getUserImageURL());
        intent.putExtra(Constants.WEB_FORMAT_URL, wallpapersList.get(clickedItemIndex).getWebformatURL());

        intent.putExtra(Constants.USER_ID, wallpapersList.get(clickedItemIndex).getUserId());
        intent.putExtra(Constants.IMAGE_ID, wallpapersList.get(clickedItemIndex).getId());
        intent.putExtra(Constants.TYPE, wallpapersList.get(clickedItemIndex).getType());
        intent.putExtra(Constants.USER, wallpapersList.get(clickedItemIndex).getUser());

        startActivity(intent);
    }
}
