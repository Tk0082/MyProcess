package com.tk.myprocess;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.Math;
import java.lang.InterruptedException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class MainActivity extends Activity {

	private TextView ifconfig, uname, netstat, df, ps, who, ss, cal;
	private SwipeRefreshLayout refreshLayout;
	private ScrollView sv;
	private Timer timer;
	private TimerTask t;
	private ProgressBar progress;
	private int n, c;
	/*float[] lastEvent = null;
	float d = 0f; 	float newRot = 0f;
	float oldDist = 1f;
	private boolean isZoom;
	private boolean isOutSide;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float xCord, yCord;	*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		n = 0;
		refreshLayout = findViewById(R.id.refresh);
		refreshLayout.setColorScheme(R.color.limed, R.color.limel);
		sv = findViewById(R.id.scroll);
		ifconfig = findViewById(R.id.txifconfig);
		uname = findViewById(R.id.txuname);
		netstat = findViewById(R.id.txnetstat);
		df = findViewById(R.id.txdf);
		ps = findViewById(R.id.txps);
		who = findViewById(R.id.txwho);
		ss = findViewById(R.id.txss);
		cal = findViewById(R.id.txcal);

		sv.requestFocus(View.FOCUS_UP);
		sv.scrollTo(0, 0);

		if (n == 0) {
			commands();
			n = 1;
		} else {
			while (n!=0){
				t = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							@Deprecated
							public void run() {
								commands();
							}
						});
					}
				};
				timer.schedule(t, 15000);
				return;
			}
		}
		
		if (sv.getY() == 0.0) {
			refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					commands();
					sv.scrollTo(0, 0);
					refreshLayout.setRefreshing(false);
				}
			});
		} 
		//scroolZoom(ps); 
		
	}

	public void commands() {
		String rt = RunCommand("su -c /system/bin/ip route");
		uname.setText(RunCommand("/system/bin/uname -osmsr"));
		who.setText(RunCommand("/system/bin/whoami"));
		who.setTextColor(Color.parseColor("#FF20D600"));

		ifconfig.setText("# REDE =========\n\n" + rt + "\n" + RunCommand("su -c /system/bin/ifconfig wlan0") + "\n");
		cal.setText(RunCommand("/system/bin/cal"));
		netstat.setText("# NETSTAT =========\n\n" + RunCommand("su -c /system/bin/netstat -planetu"));
		df.setText("# DF =========\n\n" + RunCommand("/system/bin/df -h"));
		ss.setText("# SS =========\n\n" + RunCommand("su -c ss -traps"));
		ps.setText("# PS =========\n\n" + RunCommand("su -c /system/bin/ps | su -c sort"));
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

	@Override
	protected void onResume() {
		sv.requestFocus(View.FOCUS_UP);
		sv.scrollTo(0, 0);
		commands();
		super.onResume();
	}

	@Override
	protected void onRestart() {
		sv.requestFocus(View.FOCUS_UP);
		sv.scrollTo(0, 0);
		commands();
		super.onRestart();
	}
	
	@Override
	protected void onStop() {
		n = 0;
		super.onStop();
	}

	/*
	public void scroolZoom(View v) {
		TextView txv = (TextView) v;
		txv.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				view.bringToFront();
				viewTransform(view, event);
				return true;
			}
		});
	}

	private void viewTransform(View view, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				midPoint(mid, event);
				mode = ZOOM;
			}
			lastEvent = new float[10];
			lastEvent[0] = event.getX(0);
			lastEvent[1] = event.getX(1);
			lastEvent[2] = event.getY(0);
			lastEvent[3] = event.getY(1);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			lastEvent = null;
			break;
		case MotionEvent.ACTION_UP:
			isZoom = false;
			if (mode == DRAG) {
				float x = event.getX();
				float y = event.getY();
			}
			break;
		case MotionEvent.ACTION_DOWN:
			xCord = view.getX() - event.getRawX();
			yCord = view.getY() - event.getRawY();

			start.set(event.getX(), event.getY());
			isOutSide = false;
			mode = DRAG;
			lastEvent = null;
			break;
		case MotionEvent.ACTION_OUTSIDE:
			isOutSide = true;
			mode = NONE;
			lastEvent = null;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isOutSide) {
				if (mode == DRAG) {
					isZoom = false;
					view.animate().x(event.getRawX() + xCord).y(event.getRawY() + yCord).setDuration(0).start();
				}
				if (mode == ZOOM && event.getPointerCount() == 2) {
					float newDist1 = spacing(event);
					if (newDist1 > 10f) {
						float scale = newDist1 / oldDist * view.getScaleX();
						view.setScaleX(scale);
						view.setScaleY(scale);
					}
				}
			}
			break;
		}
		ps.setSelected(true);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (int) Math.sqrt(x * x + y * y);
	} 
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
	    return false;
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
	    return false;
	}
	*/ 
}