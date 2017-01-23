package ay3524.com.wallpapertime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashish on 23-01-2017.
 */

public class WallpaperUnsplash implements Parcelable {

    String id,height,width,color,likes;
    String user_id,username,name,first_name,last_name,profile_image_large,profile_image_medium,profile_image_small;
    String urls_raw,urls_full,urls_thumb,urls_small;
    String category_id,category_title,photo_count,category_link,photo_link;
    String link_self,link_html,link_download_link,download_location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(String photo_count) {
        this.photo_count = photo_count;
    }

    public String getCategory_link() {
        return category_link;
    }

    public void setCategory_link(String category_link) {
        this.category_link = category_link;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public String getLink_self() {
        return link_self;
    }

    public void setLink_self(String link_self) {
        this.link_self = link_self;
    }

    public String getLink_html() {
        return link_html;
    }

    public void setLink_html(String link_html) {
        this.link_html = link_html;
    }

    public String getLink_download_link() {
        return link_download_link;
    }

    public void setLink_download_link(String link_download_link) {
        this.link_download_link = link_download_link;
    }

    public String getDownload_location() {
        return download_location;
    }

    public void setDownload_location(String download_location) {
        this.download_location = download_location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.height);
        dest.writeString(this.width);
        dest.writeString(this.color);
        dest.writeString(this.likes);
        dest.writeString(this.user_id);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.profile_image_large);
        dest.writeString(this.profile_image_medium);
        dest.writeString(this.profile_image_small);
        dest.writeString(this.urls_raw);
        dest.writeString(this.urls_full);
        dest.writeString(this.urls_thumb);
        dest.writeString(this.urls_small);
        dest.writeString(this.category_id);
        dest.writeString(this.category_title);
        dest.writeString(this.photo_count);
        dest.writeString(this.category_link);
        dest.writeString(this.photo_link);
        dest.writeString(this.link_self);
        dest.writeString(this.link_html);
        dest.writeString(this.link_download_link);
        dest.writeString(this.download_location);
    }

    public WallpaperUnsplash() {
    }

    protected WallpaperUnsplash(Parcel in) {
        this.id = in.readString();
        this.height = in.readString();
        this.width = in.readString();
        this.color = in.readString();
        this.likes = in.readString();
        this.user_id = in.readString();
        this.username = in.readString();
        this.name = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.profile_image_large = in.readString();
        this.profile_image_medium = in.readString();
        this.profile_image_small = in.readString();
        this.urls_raw = in.readString();
        this.urls_full = in.readString();
        this.urls_thumb = in.readString();
        this.urls_small = in.readString();
        this.category_id = in.readString();
        this.category_title = in.readString();
        this.photo_count = in.readString();
        this.category_link = in.readString();
        this.photo_link = in.readString();
        this.link_self = in.readString();
        this.link_html = in.readString();
        this.link_download_link = in.readString();
        this.download_location = in.readString();
    }

    public static final Parcelable.Creator<WallpaperUnsplash> CREATOR = new Parcelable.Creator<WallpaperUnsplash>() {
        @Override
        public WallpaperUnsplash createFromParcel(Parcel source) {
            return new WallpaperUnsplash(source);
        }

        @Override
        public WallpaperUnsplash[] newArray(int size) {
            return new WallpaperUnsplash[size];
        }
    };
}
