package com.ai.android.productsearch;



import android.app.Activity;

import android.os.Bundle;


import android.webkit.WebView;


public class ProductWebView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_engine_view);

		String url = (String) getIntent().getSerializableExtra("product_url");

		WebView engine = (WebView) findViewById(R.id.web_engine);
		 engine.setWebViewClient(new HelloWebViewClient());
		 engine.getSettings().setJavaScriptEnabled(true);
		engine.loadUrl(url);

		
	}
	
}
