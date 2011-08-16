package com.ai.android.productsearch;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelloWebViewClient extends WebViewClient{

	private final String TAG = getClass().getSimpleName();
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.d(TAG, "came here too");
	view.loadUrl(url);
	return true;
	}
}
