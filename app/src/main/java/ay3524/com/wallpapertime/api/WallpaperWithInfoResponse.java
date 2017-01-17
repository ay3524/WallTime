package ay3524.com.wallpapertime.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ay3524.com.wallpapertime.model.WallpaperWithInfo;

/**
 * Created by Ashish on 17-01-2017.
 */

public class WallpaperWithInfoResponse {
    @SerializedName("totalHits")
    @Expose
    private Integer totalHits;
    @SerializedName("hits")
    @Expose
    private ArrayList<WallpaperWithInfo> hits = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public ArrayList<WallpaperWithInfo> getHits() {
        return hits;
    }

    public void setHits(ArrayList<WallpaperWithInfo> hits) {
        this.hits = hits;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
