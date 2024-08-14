package com.hslee.fingerprint

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class AppWidgetSmall : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        Log.e(TAG, "onUpdate")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.e(TAG, "onReceive")

        context ?: return
        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            onUpdate(
                context,
                appWidgetManager,
                appWidgetManager.getAppWidgetIds(
                    ComponentName(
                        context.packageName,
                        AppWidgetSmall::class.java.name
                    )
                )
            )
        }
    }

    override fun onEnabled(context: Context) {
        Log.d(TAG, "onEnabled")
    }

    override fun onDisabled(context: Context) {
        Log.d(TAG, "onDisabled")
    }

    fun startApplication(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_small)
        views.setOnClickPendingIntent(R.id.btn_start, startApplication(context))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        internal val TAG = "WIDGET"
        internal val WIDGET_1 = "com.hslee.fingerprint.WIDGET_1"
    }
}



