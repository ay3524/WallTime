package ay3524.com.wallpapertime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.model.WallpaperCollection;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 21-01-2017.
 */

public class WallpaperCategoryAdapter  extends RecyclerView.Adapter<WallpaperCategoryAdapter.WallpaperViewHolder> {

    private ArrayList<WallpaperCollection> wallpaper;
    private Context context;
    private ListItemClickListener clickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public WallpaperCategoryAdapter(ArrayList<WallpaperCollection> wallpaperes, ListItemClickListener listener) {
        wallpaper = wallpaperes;
        clickListener = listener;
    }

    @Override
    public WallpaperCategoryAdapter.WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.single_collection_item, parent, shouldAttachToParentImmediately);
        return new WallpaperCategoryAdapter.WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperCategoryAdapter.WallpaperViewHolder holder, int position) {
        String buildSingleListImageUrl = wallpaper.get(position).getUrls_small();

        Glide.with(context).load(buildSingleListImageUrl).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.collection_cover);
    }

    @Override
    public int getItemCount() {
        return wallpaper.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.collection_cover)
        ImageView collection_cover;
        @BindView(R.id.collection_name)
        TextView collection_name;

        WallpaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }
}
