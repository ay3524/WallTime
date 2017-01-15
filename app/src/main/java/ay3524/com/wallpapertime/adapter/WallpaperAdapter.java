package ay3524.com.wallpapertime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.model.Wallpapers;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 29-12-2016.
 */

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private ArrayList<Wallpapers> wallpaper;
    private Context context;
    private ListItemClickListener clickListener;
    private final static int FADE_DURATION = 1000;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public WallpaperAdapter(ArrayList<Wallpapers> wallpaperses,ListItemClickListener listener) {
        wallpaper = wallpaperses;
        clickListener = listener;
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.single_wallpaper,parent,shouldAttachToParentImmediately);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {
        /*ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        holder.wallpaperPoster.setImageUrl(wallpaper.get(position).getWebformatURL(), imageLoader);

        imageLoader.get(wallpaper.get(position).getWebformatURL(), ImageLoader.getImageListener(
                holder.wallpaperPoster, R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        //Cache cache = AppController.getInstance().getRequestQueue().getCache();
        //Cache.Entry entry = cache.get(wallpaper.get(position).getWebformatURL());*/

        Glide.with(context).load(wallpaper.get(position).getWebformatURL()).into(holder.wallpaperPoster);
    }

    @Override
    public int getItemCount() {
        return wallpaper.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.wallpaperPoster)
        ImageView wallpaperPoster;

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
