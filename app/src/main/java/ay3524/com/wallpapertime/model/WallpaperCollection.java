package ay3524.com.wallpapertime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashish on 24-01-2017.
 */

public class WallpaperCollection implements Parcelable {
    String id,title,description,published_at,total_photos;
    String user_id,username,name,profile_image_large,profile_image_medium,profile_image_small;
    String urls_raw,urls_full,urls_thumb,urls_small,urls_regular;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(String total_photos) {
        this.total_photos = total_photos;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image_large() {
        return profile_image_large;
    }

    public void setProfile_image_large(String profile_image_large) {
        this.profile_image_large = profile_image_large;
    }

    public String getProfile_image_medium() {
        return profile_image_medium;
    }

    public void setProfile_image_medium(String profile_image_medium) {
        this.profile_image_medium = profile_image_medium;
    }

    public String getProfile_image_small() {
        return profile_image_small;
    }

    public void setProfile_image_small(String profile_image_small) {
        this.profile_image_small = profile_image_small;
    }

    public String getUrls_raw() {
        return urls_raw;
    }

    public void setUrls_raw(String urls_raw) {
        this.urls_raw = urls_raw;
    }

    public String getUrls_full() {
        return urls_full;
    }

    public void setUrls_full(String urls_full) {
        this.urls_full = urls_full;
    }

    public String getUrls_thumb() {
        return urls_thumb;
    }

    public void setUrls_thumb(String urls_thumb) {
        this.urls_thumb = urls_thumb;
    }

    public String getUrls_small() {
        return urls_small;
    }

    public void setUrls_small(String urls_small) {
        this.urls_small = urls_small;
    }

    public String getUrls_regular() {
        return urls_regular;
    }

    public void setUrls_regular(String urls_regular) {
        this.urls_regular = urls_regular;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.published_at);
        dest.writeString(this.total_photos);
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.profile_image_large);
        dest.writeString(this.profile_image_medium);
        dest.writeString(this.profile_image_small);
        dest.writeString(this.urls_raw);
        dest.writeString(this.urls_full);
        dest.writeString(this.urls_thumb);
        dest.writeString(this.urls_small);
        dest.writeString(this.urls_regular);
    }

    public WallpaperCollection() {
    }

    protected WallpaperCollection(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.published_at = in.readString();
        this.total_photos = in.readString();
        this.user_id = in.readString();
        this.username = in.readString();
        this.name = in.readString();
        this.profile_image_large = in.readString();
        this.profile_image_medium = in.readString();
        this.profile_image_small = in.readString();
        this.urls_raw = in.readString();
        this.urls_full = in.readString();
        this.urls_thumb = in.readString();
        this.urls_small = in.readString();
        this.urls_regular = in.readString();
    }

    public static final Parcelable.Creator<WallpaperCollection> CREATOR = new Parcelable.Creator<WallpaperCollection>() {
        @Override
        public WallpaperCollection createFromParcel(Parcel source) {
            return new WallpaperCollection(source);
        }

        @Override
        public WallpaperCollection[] newArray(int size) {
            return new WallpaperCollection[size];
        }
    };
}
