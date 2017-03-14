package ay3524.com.wallpapertime.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Ashish on 31-12-2016.
 */

public class Constants {

    public static final String PHOTO_SIZE_300 = "300";

    public static final String PHOTO_SIZE_200 = "200";

    public static final String PHOTO_SIZE_800 = "800";

    public static final String PHOTO_SIZE_1920 = "1920";

    public static final String PIXABAY_URI = "https://pixabay.com/api/?key=4038248-09fb84ad761a396aba067e2cf&response_group=high_resolution&category=";

    public static final String UNSPLASH_BASE_COLLECTION_CURATED = "https://api.unsplash.com/collections/curated";

    public static final String UNSPLASH_BASE_PHOTO = "https://api.unsplash.com/photos";

    public static final String ORDER_BY = "&order_by=";

    public static final String POPULAR = "popular";

    public static final String LATEST = "latest";

    public static final String PER_PAGE = "&per_page=";

    public static final String PER_PAGE_COUNT = "30";

    public static final String CLIENT_ID = "?client_id=";

    public static final String API_KEY = "1d6adf7ef9a462a70dca375dd1f8faf911481ea8e2715bf2666984671dbc4d39";

    public static final String PIXABAY_API_KEY = "4038248-09fb84ad761a396aba067e2cf";

    public static final String PHOTO_CLIENT_ID = "/photos";

    public static final int TWO = 2;

    public static final int FOUR = 4;

    public static final String CLASS = "class";
    public static final String ACTIVITY = "Activity";
    public static final String FRAGMENT = "Fragment";
    public static final String SEARCHACTIVITY = "SearchActivity";

    public static final String URL_FILE = "url_file";

    public static final String IMAGE_TYPE = "image/*";

    public static final String JPG = ".jpg";

    public static final String WALLTIME_PATH = "/WallTime";

    public static final String DEFAULT_ID = "134";

    public static final String ID = "id";

    public static final String TIME = "time";
    public static final String AUTOMATION = "automation";

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
    public static final String QUERY = "query";
    public static final String BING_URL = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US";
    public static final String IMAGES = "images";
    public static final String COPYRIGHT = "copyright";
    public static final String URL = "url";
    public static final java.lang.String SEPERATOR = "_";
    public static final String WALLTIME_PATH_DOUBLE = "/WallTime/";
    public static final String PHOTO_WDTH_240 = "240";
    public static final String PHOTO_HEIGHT_320 = "320";
    public static final String PHOTO_WDTH_720 = "720";
    public static final String PHOTO_HEIGHT_1280 = "1280";
    public static final String BING_BASE_URL = "http://www.bing.com";
    public static final String X = "x";
    public static final String RESULTS = "results";
    public static final String CATEGORY = "category";
    public static final int DEFAULT_CATEGORY = 0;

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String buildUrl(String regular_url, String size) {
        String split[] = regular_url.split("&");
        String url = split[0] + "&" + split[1] + "&" + split[2] + "&" + split[3] + "&" + split[4] + "&" + "w=" + size + "&" + split[6] + "&" + split[7];
        return url;
    }

    public static void refreshSystemMediaScanDataBase(Context context, String docPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(docPath));
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void setGridLayoutManager(Context context, RecyclerView recyclerView){

        GridLayoutManager gridLayoutManager;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(context, Constants.TWO);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(context, Constants.FOUR);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    public static void sortAllFilesNewFirst(File allFiles[]){

        Arrays.sort(allFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
    }
    public static ArrayList<String> getCategoryList(){

        ArrayList<String> spinner_collection_list = new ArrayList<>();
        spinner_collection_list.add("Animals");
        spinner_collection_list.add("Backgrounds");
        spinner_collection_list.add("Buildings");
        spinner_collection_list.add("Business");
        spinner_collection_list.add("Computer");
        spinner_collection_list.add("Education");
        spinner_collection_list.add("Fashion");
        spinner_collection_list.add("Feelings");
        spinner_collection_list.add("Food");
        spinner_collection_list.add("Health");
        spinner_collection_list.add("Industry");
        spinner_collection_list.add("Music");
        spinner_collection_list.add("Nature");
        spinner_collection_list.add("People");
        spinner_collection_list.add("Places");
        spinner_collection_list.add("Religion");
        spinner_collection_list.add("Science");
        spinner_collection_list.add("Sports");
        spinner_collection_list.add("Transportation");
        spinner_collection_list.add("Travel");
        return spinner_collection_list;
    }
    public static boolean isServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
