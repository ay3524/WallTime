package ay3524.com.wallpapertime.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ay3524.com.wallpapertime.ui.Wallpapers;

/**
 * Created by Ashish on 29-12-2016.
 */
public class WallpaperResponse {

    @SerializedName("totalHits")
    String totalHits;
    @SerializedName("hits")
    ArrayList<Wallpapers> wallpapers;
    @SerializedName("total")
    String total;

    public String getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(String totalHits) {
        this.totalHits = totalHits;
    }

    public ArrayList<Wallpapers> getWallpapers() {
        return wallpapers;
    }

    public void setWallpapers(ArrayList<Wallpapers> wallpapers) {
        this.wallpapers = wallpapers;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
