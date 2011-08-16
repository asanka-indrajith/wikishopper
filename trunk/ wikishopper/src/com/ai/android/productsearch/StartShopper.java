package com.ai.android.productsearch;

import com.ai.android.database.HomePage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartShopper extends Activity {

	protected Button productSearch;
	protected Button Category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopper);

		productSearch = (Button) findViewById(R.id.product_search_button);
		Category = (Button) findViewById(R.id.category_button);

		productSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						ProductSearchAppActivity.class); // display Search
															// interface
				startActivity(i);
			}
		});
		Category.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), HomePage.class); // display
																				// category
																				// references
																				// interface.
				startActivity(i);
			}
		});

	}

}
