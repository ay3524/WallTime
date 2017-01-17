package ay3524.com.wallpapertime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ashish on 31-12-2016.
 */

public class Constants {
    public static final String LIKES = "likes";
    public static final String FAVORITES = "favorites";
    public static final String VIEWS = "views";
    public static final String COMMENTS = "comments";
    public static final String DOWNLOADS = "downloads";
    public static final String TAGS = "tags";

    public static final String PIXABAY_PAGE_URL = "pageURL";
    public static final String IMAGE_ID = "id";
    public static String PREVIEW_HEIGHT = "previewHeight";
    public static String PREVIEW_WIDTH = "previewWidth";
    public static String WEB_FORMAT_HEIGHT = "webformatHeight";
    public static String WEB_FORMAT_WIDTH = "webformatWidth";
    public static String IMAGE_HEIGHT = "imageHeight";
    public static String IMAGE_WIDTH = "imageWidth";

    //public static String LARGE_IMAGE_URL = "largeImageURL";
    //public static String FULL_HD_URL = "fullHDURL";
    public static String PREVIEW_URL = "previewURL";
    //public static String IMAGE_URL = "imageURL";
    public static String USER_IMAGE_URL = "userImageURL";
    public static String WEB_FORMAT_URL = "webformatURL";

    public static String USER_ID = "user_id";
    //public static String ID_HASH = "id_hash";
    public static String TYPE = "type";
    public static String USER = "user";

    public static String api_key = "4038248-09fb84ad761a396aba067e2cf";
    public static String response_group_high_res = "high_resolution";
    public static String response_group_img_details = "image_details";
    public static String prettyBooleanValue = "true";

    public static String editors_choice = "editors_choice";
    public static String nature = "nature";
    public static String travel = "travel";
    public static String places = "places";

    public static String booleanImagesWithEditorsCoice = "true";
    public static String booleanImagesWithoutEditorsCoice = "false";

    public static String orderPopular = "popular";
    public static String orderLatest = "latest";

    public static String image_type_photo = "photo";

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
