package ay3524.com.wallpapertime.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ay3524.com.wallpapertime.ui.SingleWallpaperDetails;

/**
 * Created by Ashish on 03-01-2017.
 */
public class WallpaperByIdResponse {
    @SerializedName("totalHits")
    @Expose
    private Integer totalHits;
    @SerializedName("hits")
    @Expose
    private ArrayList<SingleWallpaperDetails> hits = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public ArrayList<SingleWallpaperDetails> getHits() {
        return hits;
    }

    public void setHits(ArrayList<SingleWallpaperDetails> hits) {
        this.hits = hits;
    }

    public Integer getTotal() {
        return total;
    }
}