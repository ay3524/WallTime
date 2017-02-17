package ay3524.com.wallpapertime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Environment;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.concurrent.ExecutionException;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.utils.Constants;

/**
 * Created by Ashish on 11-12-2016.
 */

class WallTimeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private File allFiles[];

    WallTimeWidgetFactory(Context context, Intent intent) {
        mContext = context;
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        File folder = new File(Environment.getExternalStorageDirectory() + Constants.WALLTIME_PATH);
        if (folder.exists()) {
            allFiles = folder.listFiles();
        }
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public int getCount() {
        if (allFiles != null)
            return allFiles.length;
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.single_widget_layout);
        try {
            if (allFiles != null) {
                Bitmap bmp = Glide.with(mContext).load(allFiles[position]).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(200, 100).get();
                rv.setImageViewBitmap(R.id.widget_single_image, bmp);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        File folder = new File(Environment.getExternalStorageDirectory() + Constants.WALLTIME_PATH);
        allFiles = folder.listFiles();

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }
}
