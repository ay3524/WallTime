package ay3524.com.wallpapertime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ashish on 31-12-2016.
 */

public class Constants {

    public static final String ID = "id";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String COLOR = "color";
    public static final String LIKES = "likes";
    public static final String USER = "user";
    public static final String USER_ID = "user_id";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String SMALL_PROFILE_IMAGE = "small";
    public static final String MEDIUM_PROFILE_IMAGE = "medium";
    public static final String LARGE_PROFILE_IMAGE = "large";
    public static final String URLS = "urls";
    public static final String RAW = "raw";
    public static final String FULL = "full";
    public static final String REGULAR = "regular";
    public static final String SMALL = "small";
    public static final String THUMB = "thumb";

    public static final String TITLE = "title";
    public static final String TOTAL_PHOTOS = "total_photos";
    public static final String COVER_PHOTOS = "cover_photo";

    public static final String TAG_JSON_ARRAY = "TAG_JSON_ARRAY";
    public static final String STATE_WALLPAPERS = "state";
    public static final String TAG_JSON_OBJECT = "TAG_JSON_OBJECT";

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String buildUrl(String regular_url, String size) {
        String split[] = regular_url.split("&");
        String url = split[0] +"&"+ split[1] +"&"+ split[2] +"&"+ split[3] +"&"+ split[4] +"&"+ "w=" + size + "&" + split[6] +"&"+ split[7];
        return url;
    }

}
