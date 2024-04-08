package com.tk.myprocess;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.app.PendingIntent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.appwidget.AppWidgetProvider;
import android.widget.RemoteViews;
import android.content.Intent;
import android.content.Context;

public class LanWidget extends AppWidgetProvider{
	static String CLICK_ACTION = "CLICKED";
	public static String REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
	private int x = 0;
	
	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		if(intent.getAction().equals(CLICK_ACTION)){
			commands(context, intent);
		}
	}
	
		
	@Override
	public void onUpdate(Context context, AppWidgetManager appManager, int[] appIds) {
		for (int widgetId : appIds) {
			updateAppWidget(context, appManager, widgetId);
		}
	}
	
	static void updateAppWidget(Context context, AppWidgetManager widgetManager, int widgetId){
		
		Intent i = new Intent(context, LanWidget.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0); 
		
		String rt = "Rede: "+ RunCommand("su -c /system/bin/ip route");
		String ifc = RunCommand("su -c /system/bin/ifconfig wlan0");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.back_widget);
		remoteViews.setTextViewText(R.id.txtitulo, rt);
		remoteViews.setTextViewText(R.id.txifcfg, ifc);
		remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
		
		widgetManager.updateAppWidget(widgetId, remoteViews);
	}
	
	static private PendingIntent getPenIntent(Context context) {
		Intent intent = new Intent(context, LanWidget.class);
		intent.setAction(REFRESH_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private void commands(Context con, Intent i){
		String rt = "Rede: "+ RunCommand("su -c /system/bin/ip route");
		String ifc = RunCommand("su -c /system/bin/ifconfig wlan0");
		
		i = new Intent(con, LanWidget.class);
		i.setAction(REFRESH_ACTION);
		PendingIntent pendingIntent = PendingIntent.getActivity(con, 0, i, 0); 
		i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		RemoteViews remoteViews = new RemoteViews(con.getPackageName(), R.layout.back_widget);
		remoteViews.setTextViewText(R.id.txtitulo, rt);
		remoteViews.setTextViewText(R.id.txifcfg, ifc);
		remoteViews.setOnClickPendingIntent(R.id.widget, getPenIntent(con));
		int[] ids = AppWidgetManager.getInstance(con).getAppWidgetIds(new ComponentName(con, LanWidget.class));
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
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