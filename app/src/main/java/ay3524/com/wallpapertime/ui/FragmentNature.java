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
import android.widget.Toast;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.adapter.WallpaperAdapter;
import ay3524.com.wallpapertime.api.ApiClient;
import ay3524.com.wallpapertime.api.ApiInterface;
import ay3524.com.wallpapertime.api.WallpaperResponse;
import ay3524.com.wallpapertime.model.Wallpapers;
import ay3524.com.wallpapertime.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ashish on 31-12-2016.
 */

public class FragmentNature extends Fragment implements WallpaperAdapter.ListItemClickListener {
    private static final String STATE_WALLPAPERS = "state";
    private RecyclerView recyclerView;
    private ArrayList<Wallpapers> wallpapersList = new ArrayList<>();
    private WallpaperAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.single_tab_layout, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.item_list);

        //gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        if (savedInstanceState != null) {
            wallpapersList = savedInstanceState.getParcelableArrayList(STATE_WALLPAPERS);
            adapter = new WallpaperAdapter(wallpapersList, FragmentNature.this);
            recyclerView.setAdapter(adapter);
        }else{
            getListOfWallpapers();
        }


        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WALLPAPERS, wallpapersList);

    }

    private void getListOfWallpapers() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<WallpaperResponse> call = apiService.getWallpaper(Constants.api_key,
                Constants.response_group, Constants.nature, Constants.booleanImagesWithEditorsCoice,
                Constants.orderPopular, Constants.prettyBooleanValue);
        call.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {
                try {
                    if (response != null) {
                        wallpapersList = response.body().getWallpapers();
                        adapter = new WallpaperAdapter(wallpapersList, FragmentNature.this);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (NullPointerException ignored) {
                    Toast.makeText(getActivity(), "NPE Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
            Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
            intent.putExtra(Constants.PREVIEW_HEIGHT, wallpapersList.get(clickedItemIndex).getPreviewHeight());
            intent.putExtra(Constants.PREVIEW_HEIGHT, wallpapersList.get(clickedItemIndex).getPreviewHeight());
            intent.putExtra(Constants.PREVIEW_WIDTH, wallpapersList.get(clickedItemIndex).getPreviewWidth());
            intent.putExtra(Constants.WEB_FORMAT_HEIGHT, wallpapersList.get(clickedItemIndex).getWebformatHeight());
            intent.putExtra(Constants.WEB_FORMAT_WIDTH, wallpapersList.get(clickedItemIndex).getWebformatWidth());
            intent.putExtra(Constants.IMAGE_HEIGHT, wallpapersList.get(clickedItemIndex).getImageHeight());
            intent.putExtra(Constants.IMAGE_WIDTH, wallpapersList.get(clickedItemIndex).getImageWidth());

            intent.putExtra(Constants.LARGE_IMAGE_URL, wallpapersList.get(clickedItemIndex).getLargeImageURL());
            intent.putExtra(Constants.FULL_HD_URL, wallpapersList.get(clickedItemIndex).getFullHDURL());
            intent.putExtra(Constants.PREVIEW_URL, wallpapersList.get(clickedItemIndex).getPreviewURL());
            intent.putExtra(Constants.IMAGE_URL, wallpapersList.get(clickedItemIndex).getImageURL());
            intent.putExtra(Constants.USER_IMAGE_URL, wallpapersList.get(clickedItemIndex).getUserImageURL());
            intent.putExtra(Constants.WEB_FORMAT_URL, wallpapersList.get(clickedItemIndex).getWebformatURL());

            intent.putExtra(Constants.USER_ID, wallpapersList.get(clickedItemIndex).getUser_id());
            intent.putExtra(Constants.ID_HASH, wallpapersList.get(clickedItemIndex).getId_hash());
            intent.putExtra(Constants.TYPE, wallpapersList.get(clickedItemIndex).getType());
            intent.putExtra(Constants.USER, wallpapersList.get(clickedItemIndex).getUser());

            startActivity(intent);
        }
}

