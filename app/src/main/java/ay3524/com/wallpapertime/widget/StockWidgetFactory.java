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

/**
 * Created by Ashish on 11-12-2016.
 */

class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mWidgetId;
    File allFiles[];

    StockWidgetFactory(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        File folder = new File(Environment.getExternalStorageDirectory() + "/WallTime");
        if (folder.exists()) {
            allFiles = folder.listFiles();
        }
        /*dbHelper = new DbHelper(mContext);
             dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");*/
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public int getCount() {
        return allFiles.length;
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
            Bitmap bmp = Glide.with(mContext).load(allFiles[position]).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(200,100).get();
            rv.setImageViewBitmap(R.id.widget_single_image, bmp);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*if (mCursor.moveToPosition(position)) {
            rv.setTextViewText(R.id.symbol,
                    mCursor.getString(Contract.Quote.POSITION_SYMBOL));

            rv.setTextViewText(R.id.price,
                    dollarFormat.format(mCursor.getFloat(Contract.Quote.POSITION_PRICE)));

            float rawAbsoluteChange = mCursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            String changeString = dollarFormatWithPlus.format(rawAbsoluteChange);

            if (rawAbsoluteChange > 0) {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            rv.setTextViewText(R.id.change,
                    changeString);
        }*/
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
        File folder = new File(Environment.getExternalStorageDirectory() + "/WallTime");
        allFiles = folder.listFiles();
        /*if (mCursor != null) {
            mCursor.close();
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        mCursor = db.query(Contract.Quote.TABLE_NAME,
                new String[]{Contract.Quote._ID,Contract.Quote.COLUMN_SYMBOL, Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE},
                null,
                null,
                null,
                null,
                null);*/

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        /*if (mCursor != null) {
            mCursor.close();
        }*/
    }
}
