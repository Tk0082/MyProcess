package com.tk.myprocess;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.widget.TextView;

public class IfcfgWidget extends AppWidgetProvider {
	TextView ifc;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appManager, int[] appIds) {
		for (int widgetId : appIds) {
			String rt = MainActivity.RunCommand("su -c /system/bin/ip route");
			ifc.setText("Rede:\n"+ rt + "\n"+MainActivity.RunCommand("su -c /system/bin/ifconfig wlan0"));
			
			/*Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			//intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appIds);
			
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.back_widget);
			remoteViews.setTextViewText(R.id.txwifcfg, ifc.toString());
			
			appManager.updateAppWidget(widgetId, remoteViews);*/
		}
		
	}
}