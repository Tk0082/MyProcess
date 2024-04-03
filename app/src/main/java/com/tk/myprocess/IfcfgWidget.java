package com.tk.myprocess;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.widget.TextView;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.StringBuffer;
import java.lang.Process;

public class IfcfgWidget extends AppWidgetProvider {
	
	TextView ifc, rt;
	Intent intent;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appManager, int[] appIds) {
		
		for (int widgetId : appIds) {
			
			intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); //PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appIds);
			
			rt.setText("Rede: "+ RunCommand("su -c /system/bin/ip route"));
			ifc.setText(RunCommand("su -c /system/bin/ifconfig wlan0"));
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.back_widget);
			remoteViews.setTextViewText(R.id.txtitulo, rt.toString());
			remoteViews.setTextViewText(R.id.txifcfg, ifc.toString());
			
			appManager.updateAppWidget(appIds, remoteViews);
		}
	}
	
	public static String RunCommand(String cmd) {
		
		StringBuffer cmdOut = new StringBuffer();
		Process process;
		
		try {
			// process executa comando passado pela string
			process = Runtime.getRuntime().exec(cmd);

			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			BufferedReader bufReader = new BufferedReader(isr);
			char[] buf = new char[4096];
			int nReader = 0;
			while ((nReader = bufReader.read(buf)) > 0) {
				cmdOut.append(buf, 0, nReader);
			}
			bufReader.close();
			process.destroy();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmdOut.toString();
	}
}