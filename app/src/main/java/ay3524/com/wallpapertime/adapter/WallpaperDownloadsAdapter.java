package ay3524.com.wallpapertime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import ay3524.com.wallpapertime.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 02-02-2017.
 */

public class WallpaperDownloadsAdapter extends RecyclerView.Adapter<WallpaperDownloadsAdapter.WallpaperViewHolder> {

    private List<File> wallpaperDownloads;
    private Context context;
    private ListItemClickListener clickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public WallpaperDownloadsAdapter(List<File> wallpaperDownload, ListItemClickListener listener) {
        wallpaperDownloads = wallpaperDownload;
        clickListener = listener;

    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.single_collection_item, parent, false);
        return new WallpaperDownloadsAdapter.WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {
        Glide.with(context)
                .load(wallpaperDownloads.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.collection_cover);
    }

    @Override
    public int getItemCount() {
        return wallpaperDownloads == null ? 0 : wallpaperDownloads.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.collection_cover)
        ImageView collection_cover;
        @BindView(R.id.collection_name)
        TextView collection_name;

        WallpaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            collection_name.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }
}
