package com.ai.android.productsearch;

import com.ai.android.model.Product;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class ProductDescription extends Activity {
	private String imageUrl;
	private String productUrl;
	private Product product;
	private Button descriptionButton;
	private Button imageButton;
	private Button moreButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_engine_view);

		product = (Product) getIntent().getSerializableExtra(
				"product_description");

		descriptionButton = (Button) findViewById(R.id.button1);
		imageButton = (Button) findViewById(R.id.button2);
		moreButton = (Button) findViewById(R.id.button3);

		displayDescription();

		descriptionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				displayDescription();

			}
		});
		imageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dispalyImage();

			}
		});
		moreButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				dispalyMore();

			}
		});

	}

	private void displayDescription() {

		WebView engine = (WebView) findViewById(R.id.web_engine);
		// engine.loadUrl("http://mobile.tutsplus.com");
		engine.getSettings().setJavaScriptEnabled(true);
		String description = product.description;
		description = description.replace('"', ' '); // removing unnecessary '"
														// ' marks from the
														// string value
		engine.loadData(description, "text/html", "UTF-8");

	}

	private void dispalyImage() {
		WebView engine = (WebView) findViewById(R.id.web_engine);
		engine.loadUrl(product.retrieveLargeImage());

		engine.getSettings().setJavaScriptEnabled(true);
		//

	}

	private void dispalyMore() {
		WebView engine = (WebView) findViewById(R.id.web_engine);
		engine.loadUrl(product.url);
		engine.setWebViewClient(new HelloWebViewClient());
		engine.getSettings().setJavaScriptEnabled(true);

	}
}
