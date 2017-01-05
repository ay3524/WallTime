package ay3524.com.wallpapertime.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashish on 29-12-2016.
 */
public class Wallpapers implements Parcelable {
    @SerializedName("previewHeight")
    String previewHeight;
    @SerializedName("largeImageURL")
    String largeImageURL;
    @SerializedName("fullHDURL")
    String fullHDURL;
    @SerializedName("webformatHeight")
    String webformatHeight;
    @SerializedName("webformatWidth")
    String webformatWidth;
    @SerializedName("previewURL")
    String previewURL;
    @SerializedName("imageWidth")
    String imageWidth;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("imageURL")
    String imageURL;
    @SerializedName("previewWidth")
    String previewWidth;
    @SerializedName("userImageURL")
    String userImageURL;
    @SerializedName("imageHeight")
    String imageHeight;
    @SerializedName("webformatURL")
    String webformatURL;
    @SerializedName("id_hash")
    String id_hash;
    @SerializedName("type")
    String type;
    @SerializedName("user")
    String user;

    public String getPreviewHeight() {
        return previewHeight;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public String getFullHDURL() {
        return fullHDURL;
    }

    public String getWebformatHeight() {
        return webformatHeight;
    }

    public String getWebformatWidth() {
        return webformatWidth;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPreviewWidth() {
        return previewWidth;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public String getId_hash() {
        return id_hash;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.previewHeight);
        dest.writeString(this.largeImageURL);
        dest.writeString(this.fullHDURL);
        dest.writeString(this.webformatHeight);
        dest.writeString(this.webformatWidth);
        dest.writeString(this.previewURL);
        dest.writeString(this.imageWidth);
        dest.writeString(this.user_id);
        dest.writeString(this.imageURL);
        dest.writeString(this.previewWidth);
        dest.writeString(this.userImageURL);
        dest.writeString(this.imageHeight);
        dest.writeString(this.webformatURL);
        dest.writeString(this.id_hash);
        dest.writeString(this.type);
        dest.writeString(this.user);
    }

    public Wallpapers() {
    }

    protected Wallpapers(Parcel in) {
        this.previewHeight = in.readString();
        this.largeImageURL = in.readString();
        this.fullHDURL = in.readString();
        this.webformatHeight = in.readString();
        this.webformatWidth = in.readString();
        this.previewURL = in.readString();
        this.imageWidth = in.readString();
        this.user_id = in.readString();
        this.imageURL = in.readString();
        this.previewWidth = in.readString();
        this.userImageURL = in.readString();
        this.imageHeight = in.readString();
        this.webformatURL = in.readString();
        this.id_hash = in.readString();
        this.type = in.readString();
        this.user = in.readString();
    }

    public static final Parcelable.Creator<Wallpapers> CREATOR = new Parcelable.Creator<Wallpapers>() {
        @Override
        public Wallpapers createFromParcel(Parcel source) {
            return new Wallpapers(source);
        }

        @Override
        public Wallpapers[] newArray(int size) {
            return new Wallpapers[size];
        }
    };
}
