package com.example.shaw.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class mWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widget, pi);

        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        ComponentName me = new ComponentName(context, mWidget.class);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("STATICACTION"))
        {
            Bundle bundle = intent.getExtras();
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
            Intent i = new Intent(context, ItemDetail.class);
            i.addCategory(Intent.CATEGORY_DEFAULT);

            i.putExtra("itemName",intent.getExtras().getString("item"));
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, bundle.getString("item")+"仅售"+bundle.getString("price"));
            updateViews.setImageViewResource(R.id.appwidget_image, bundle.getInt("icon"));
            updateViews.setOnClickPendingIntent(R.id.widget, pi);
            ComponentName me = new ComponentName(context, mWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);

        }




    }
}

