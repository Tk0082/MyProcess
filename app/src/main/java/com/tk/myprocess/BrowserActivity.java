/*
 * Copyright 2024 Alan Souza(tk0082@hotmail.com - github.com/Tk0082)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.tk.myprocess;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.HashMap;
import java.util.Map;

public class BrowserActivity extends Activity {
	
	WebView webview;
	Intent i;
	TextView uname, who;
	SwipeRefreshLayout swp;
	
	@Override
	protected void onCreate(Bundle savedInstanceSatate) {
		super.onCreate(savedInstanceSatate);
		setContentView(R.layout.activity_browser);
		
		webview = findViewById(R.id.webv);
		swp = findViewById(R.id.swp);
		uname = findViewById(R.id.txuname);
		who = findViewById(R.id.txwho);
		
		webview.setWebViewClient(new WebViewClient());
		final String nm = getIntent().getStringExtra("nm");
		final String id = getIntent().getStringExtra("id");
		uname.setText(nm);
		who.setText(id);
		init();
		
		webview.loadUrl("https://github.com/Tk0082");
		
		swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				webview.reload();
			}
		});
		
	}
	
	public void init(){
		webview.setWebViewClient(new MyBrowser());
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);
        webview.setLongClickable(true);
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.getJavaScriptCanOpenWindowsAutomatically();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
	}
	
	@Override
	public boolean onKeyDown(int code, KeyEvent event) {
		if (code == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
			i = new Intent(getApplicationContext(), MainActivity.class);
 	       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
  	      startActivity(i);
  	      finish();
		}
		return super.onKeyDown(code, event);
	}
	
	private class MyBrowser extends WebViewClient {
		MyBrowser(){}

		private final Map<String, Boolean> loadedUrls = new HashMap<>();
		
		@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
		
		@Deprecated
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return  super.shouldOverrideUrlLoading(view, request);
        }
		
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
			swp.setRefreshing(false);
		}
		
		@Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
	}
}