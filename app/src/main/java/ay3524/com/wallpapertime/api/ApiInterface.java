package ay3524.com.wallpapertime.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ashish on 29-12-2016.
 */

public interface ApiInterface {
    @GET("/api")
    Call<WallpaperResponse> getWallpaper(@Query("key")String apiKey,
                                         @Query("response_group")String highResResponse,
                                         @Query("category")String query,
                                         @Query("editors_choice")String booleanEditorChoice,
                                         @Query("order")String order,
                                         @Query("pretty")String prettyBooleanValue);
    @GET("/api")
    Call<WallpaperByIdResponse> getWallpaperById(@Query("key")String apiKey,
                                         @Query("response_group")String highResResponse,
                                         @Query("category")String query,
                                         @Query("editors_choice")String booleanEditorChoice,
                                         @Query("order")String order,
                                         @Query("pretty")String prettyBooleanValue);
}