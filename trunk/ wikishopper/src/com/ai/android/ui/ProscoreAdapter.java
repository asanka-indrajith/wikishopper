package com.ai.android.ui;

import java.util.ArrayList;

import com.ai.android.productsearch.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProscoreAdapter extends ArrayAdapter<String> {
	private ArrayList<String> featuresDataItems;
	private final String TAG = getClass().getSimpleName();

	private Activity context;

	/*
	 * A ListAdapter that manages a ListView backed by an array of Product
	 * objects. Also Initialized with the products list retrieved from the List
	 * activity.
	 */

	public ProscoreAdapter(Activity context, int textViewResourceId,
			ArrayList<String> productDataItems) {
		super(context, textViewResourceId, productDataItems);
		this.context = context;
		this.featuresDataItems = productDataItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // Using
																		// LayoutInflater
																		// service
																		// to
																		// retrieve
																		// an
																		// XML
																		// layout
																		// during
																		// runtime
			view = vi.inflate(R.layout.proscore_data_row, null);
		}

		String feature = featuresDataItems.get(position);
		String[] values = feature.split("1011", 2);

		if (feature != null) {

			// feature
			TextView titleTextView = (TextView) view
					.findViewById(R.id.feature_text_view); // Take reference of
															// each of the views
															// widgets
			titleTextView.setText("" + (position + 1) + " )  " + values[0]); // providing
																				// the
																				// relevant
																				// text,

			// values
			// TextView categoryTextView = (TextView)
			// view.findViewById(R.id.value_text_view);
			// categoryTextView.setText("Score: " + values[1]);

			RatingBar myRatingBar = (RatingBar) view
					.findViewById(R.id.ratingBar1);
			myRatingBar.setRating(returnRating(values[1]));

		}
		return view;
	}

	private float returnRating(String rating) {

		float rate = Float.parseFloat(rating) / 10;

		return rate;
	}
}
