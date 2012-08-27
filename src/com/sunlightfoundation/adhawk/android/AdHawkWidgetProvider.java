package com.sunlightfoundation.adhawk.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AdHawkWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {		
		// Perform this loop procedure for each widget that belongs to this provider
        final int length = appWidgetIds.length;
        for (int i=0; i<length; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = buildView(context);
            manager.updateAppWidget(appWidgetId, views);
        }
	}
	
	public static RemoteViews buildView(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		Intent intent = new Intent(context, AdHawk.class)
			.putExtra("autostart", true);
		views.setOnClickPendingIntent(R.id.widget_button, PendingIntent.getActivity(context, 0, intent, 0));
		
        return views;
	}
}
