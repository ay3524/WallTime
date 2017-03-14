package ay3524.com.wallpapertime.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Ashish on 06-01-2017.
 */

public class WallpaperIntentService extends IntentService{

    public WallpaperIntentService() {
        super("WallpaperIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new WallpaperSyncTask().setWallpaper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WallpaperSyncUtils.stopFirebaseJobDispatcher(getApplicationContext());
    }
}
