package ay3524.com.wallpapertime.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashish on 17-01-2017.
 */

public class WallpaperWithInfo implements Parcelable {
    @SerializedName("previewHeight")
    @Expose
    private Integer previewHeight;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("favorites")
    @Expose
    private Integer favorites;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("webformatHeight")
    @Expose
    private Integer webformatHeight;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("webformatWidth")
    @Expose
    private Integer webformatWidth;
    @SerializedName("previewWidth")
    @Expose
    private Integer previewWidth;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("downloads")
    @Expose
    private Integer downloads;
    @SerializedName("pageURL")
    @Expose
    private String pageURL;
    @SerializedName("previewURL")
    @Expose
    private String previewURL;
    @SerializedName("webformatURL")
    @Expose
    private String webformatURL;
    @SerializedName("imageWidth")
    @Expose
    private Integer imageWidth;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;
    @SerializedName("imageHeight")
    @Expose
    private Integer imageHeight;

    public Integer getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(Integer previewHeight) {
        this.previewHeight = previewHeight;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(Integer webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(Integer webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public Integer getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(Integer previewWidth) {
        this.previewWidth = previewWidth;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.previewHeight);
        dest.writeValue(this.likes);
        dest.writeValue(this.favorites);
        dest.writeString(this.tags);
        dest.writeValue(this.webformatHeight);
        dest.writeValue(this.views);
        dest.writeValue(this.webformatWidth);
        dest.writeValue(this.previewWidth);
        dest.writeValue(this.comments);
        dest.writeValue(this.downloads);
        dest.writeString(this.pageURL);
        dest.writeString(this.previewURL);
        dest.writeString(this.webformatURL);
        dest.writeValue(this.imageWidth);
        dest.writeValue(this.userId);
        dest.writeString(this.user);
        dest.writeString(this.type);
        dest.writeValue(this.id);
        dest.writeString(this.userImageURL);
        dest.writeValue(this.imageHeight);
    }

    public WallpaperWithInfo() {
    }

    protected WallpaperWithInfo(Parcel in) {
        this.previewHeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.likes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favorites = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tags = in.readString();
        this.webformatHeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.views = (Integer) in.readValue(Integer.class.getClassLoader());
        this.webformatWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.previewWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.comments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.downloads = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pageURL = in.readString();
        this.previewURL = in.readString();
        this.webformatURL = in.readString();
        this.imageWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user = in.readString();
        this.type = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userImageURL = in.readString();
        this.imageHeight = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<WallpaperWithInfo> CREATOR = new Parcelable.Creator<WallpaperWithInfo>() {
        @Override
        public WallpaperWithInfo createFromParcel(Parcel source) {
            return new WallpaperWithInfo(source);
        }

        @Override
        public WallpaperWithInfo[] newArray(int size) {
            return new WallpaperWithInfo[size];
        }
    };
}