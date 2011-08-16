package com.ai.android.productsearch;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ai.android.model.Product;

import com.ai.android.services.ProductByIDSeeker;
import com.ai.android.ui.ProductsAdapter;

public class ProductsListActivity extends ListActivity {

	private final String TAG = getClass().getSimpleName();
	private ArrayList<Product> productsList = new ArrayList<Product>();
	private ProductsAdapter productsAdapter;
	public ProgressDialog progressDialog;
	public Product newProduct;
	public Boolean finishedRetriving = false;

	private static final int KEY_FEATURES = 0;
	private static final int ITEM_PROS = 1;
	private static final int ITEM_CONS = 2;
	private static final int ITEM_VIEW_FULL_IMAGE = 3;
	private static final int MORE_INFO = 4;
	private static final int RELATED = 5;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_layout);

		productsAdapter = new ProductsAdapter(this, R.layout.product_data_row,
				productsList);
		productsList = (ArrayList<Product>) getIntent().getSerializableExtra(
				"products");
		viewAdapter();

	}

	private void viewAdapter() {

		setListAdapter(productsAdapter); // associate our ListActivity with the
											// ArrayAdapter-ProductsAdapter

		if (productsList != null && !productsList.isEmpty()) {

			productsAdapter.notifyDataSetChanged(); // to notify the attached
													// View that the underlying
													// data has been changed and
													// it should refresh itself.
			productsAdapter.clear();
			for (int i = 0; i < productsList.size(); i++) {
				productsAdapter.add(productsList.get(i));
			}
		}

		productsAdapter.notifyDataSetChanged();

	}

	/*
	 * When item in the list is selected,retrieve the Movie object that is
	 * associated with the list item using the ArrayAdapter. Then parse that
	 * abject to ProductDeatilsList class.
	 */
	
	//Menu feature
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, KEY_FEATURES, 0,
				getString(R.string.keyfeatures_menu)).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(Menu.NONE, ITEM_PROS, 0, getString(R.string.pro_menu))
				.setIcon(R.drawable.ic_menu_happy);
		menu.add(Menu.NONE, ITEM_CONS, 0, getString(R.string.cons_menu))
				.setIcon(R.drawable.ic_menu_sad);
		menu.add(Menu.NONE, ITEM_VIEW_FULL_IMAGE, 0,
				getString(R.string.image_menu)).setIcon(
				android.R.drawable.ic_menu_gallery);
		menu.add(Menu.NONE, MORE_INFO, 0, getString(R.string.more_menu))
				.setIcon(android.R.drawable.ic_menu_more);
		menu.add(Menu.NONE, RELATED, 0, getString(R.string.related_menu))
				.setIcon(R.drawable.ic_menu_tag);

		return super.onCreateOptionsMenu(menu);
	}

//menu selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case KEY_FEATURES:
			viewProductKeyFeatures();
			return true;
		case ITEM_PROS:
			viewProductPros();
			return true;

		case ITEM_CONS:
			viewProductCons();
			return true;
		case ITEM_VIEW_FULL_IMAGE:

			return true;
		case MORE_INFO:

			return true;
		case RELATED:

			return true;

		}
		return false;
	}
*/
	
	//menu Pros
	/*
	private void viewProductPros() {

		Product product = retrieveSelectedProduct();
		int position = getSelectedItemPosition();
		Log.d(TAG, "aaaaaaaaaa", null);

		if (product == null) {
			Log.d(TAG, "product null", null);
			return;
		}

		if (product.description == null) {
			Log.d(TAG, "bbbbb", null);

			product = retriveDetails(product);
			Log.d(TAG, "ccccc", null);
		}

		if (Integer.parseInt(product.number_of_reviews) == 0
				|| Integer.parseInt(product.proscore) == -1) {
		} else {
			productsList.add(position, product);

			Intent intent = new Intent(ProductsListActivity.this,
					ProductProsList.class);
			intent.putExtra("productpros", product);

			startActivity(intent);
		}

	}
*/
	//menu cons
	/*
	private void viewProductCons() {

		Product product = retrieveSelectedProduct();
		int position = getSelectedItemPosition();
		Log.d(TAG, "aaaaaaaaaa", null);

		if (product == null) {
			Log.d(TAG, "product null", null);
			return;
		}

		if (product.description == null) {
			product = retriveDetails(product);
			Log.d(TAG, "ccccc", null);
		}

		if (Integer.parseInt(product.number_of_reviews) == 0
				|| Integer.parseInt(product.proscore) == -1) {
		} else {
			productsList.add(position, product);
			Intent intent = new Intent(ProductsListActivity.this,
					ProductConssList.class);
			intent.putExtra("productcons", product);

			startActivity(intent);

		}
	}
*/
	
	//menu features
	/*
	private void viewProductKeyFeatures() {

		Product product = retrieveSelectedProduct();
		int position = getSelectedItemPosition();
		Log.d(TAG, "aaaaaaaaaa", null);

		if (product == null) {
			Log.d(TAG, "product null", null);
			return;
		}

		if (product.description == null) {
			product = retriveDetails(product);
			Log.d(TAG, "ccccc", null);
		}

		productsList.add(position, product);
		Intent intent = new Intent(ProductsListActivity.this,
				KeyFeaturesList.class);
		intent.putExtra("products_key", product.keyFeatures);

		startActivity(intent);

	}
*/
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		Product product = productsAdapter.getItem(position);
		
			Intent intent = new Intent(ProductsListActivity.this,
					ProductOptionActivity.class);
			intent.putExtra("singleproduct", product);

			startActivity(intent);

		
	}

	public Product retriveDetails(Product product) {

		ProductByIDSeeker proseeker = new ProductByIDSeeker();
		return proseeker.find(product.id);
	}

	private Product retrieveSelectedProduct() {
		int position = getSelectedItemPosition();
		Log.d(TAG, "" + position, null);
		if (position == -1) {
			return null;
		}
		return productsAdapter.getItem(position);
	}

	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
