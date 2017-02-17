package ay3524.com.wallpapertime.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Ashish on 06-01-2017.
 */

public class WallpaperFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWallpaperTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchWallpaperTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                new WallpaperSyncTask().setWallpaper(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };

        mFetchWallpaperTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchWallpaperTask != null) {
            mFetchWallpaperTask.cancel(true);
        }
        return false;
    }
}
