package ay3524.com.wallpapertime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import ay3524.com.wallpapertime.R;


/**
 * Created by Ashish on 10-12-2016.
 */

public class WidgetActivity extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        setList(rv, context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.list_view);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, StockWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.list_view, adapter);
    }
}
