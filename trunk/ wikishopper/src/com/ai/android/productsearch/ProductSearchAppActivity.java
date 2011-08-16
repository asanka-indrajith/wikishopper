package com.ai.android.productsearch;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ai.android.database.DatabaseHelper;

import com.ai.android.model.Product;
import com.ai.android.services.GenericSeeker;
import com.ai.android.services.ProductByNameSeeker;
import com.ai.android.services.ProductByNumberSeeker;

public class ProductSearchAppActivity extends Activity {

	private static final String EMPTY_STRING = "";

	private EditText searchEditText;
	private RadioButton nameSearchRadioButton;
	private RadioButton upcSearchRadioButton;
	private RadioButton eanSearchRadioButton;
	private RadioButton mpnSearchRadioButton;
	private RadioGroup searchRadioGroup;
	private Button searchButton;
	private CheckBox checkBox;
	private String type;
	protected SQLiteDatabase db;
	ArrayAdapter<String> ArrAdpt;
	Spinner spinner;
	private final int upc = 1;
	private final int ean = 2;
	private final int notupc = 3;
	private final int notean = 4;

	private final String TAG = getClass().getSimpleName();

	private GenericSeeker<Product> byNameSeeker = new ProductByNameSeeker();
	private GenericSeeker<Product> byNumberSeeker = new ProductByNumberSeeker();

	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = (new DatabaseHelper(this)).getWritableDatabase();

		this.findAllViewsById();

		performSpinner();

		nameSearchRadioButton.setOnClickListener(radioButtonListener);
		upcSearchRadioButton.setOnClickListener(radioButtonListener);
		eanSearchRadioButton.setOnClickListener(radioButtonListener);
		mpnSearchRadioButton.setOnClickListener(radioButtonListener);

		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String query = searchEditText.getText().toString();

				performSearch(query);

			}
		});

		searchEditText.setOnFocusChangeListener(new DftTextOnFocusListener(
				getString(R.string.search)));

		int id = searchRadioGroup.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) findViewById(id);

	}

	/*
	 * Method that used to get existing Product categories in the Database and
	 * insert their names to the spinner. String Array category used to store
	 * category names.
	 */
	public void performSpinner() {
		Cursor cursor = db.rawQuery("SELECT category FROM category", null);

		String[] category = new String[cursor.getCount()]; // Initiating the
															// category Array

		cursor.moveToFirst();

		for (int i = 0; i < cursor.getCount(); i++) { // put all cursor
														// objects-category
														// names to the Array
														// category
			category[i] = cursor.getString(cursor.getColumnIndex("category")); // adding
																				// String
																				// category
																				// name
																				// to
																				// String
																				// Array
																				// category.
			cursor.moveToNext();
		}

		spinner = (Spinner) findViewById(R.id.category_spinner01);
		ArrAdpt = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_spinner_item, category); // uses built
																	// in
																	// layouts
																	// for the
																	// spinner.
		ArrAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(ArrAdpt); // displaying the constructed spinner
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		cursor.close();
	}

	// OnItemSelectedListner Class for the Spinner
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	/*
	 * This method used to get the user preferences of the selected product
	 * category from the Database table. combine preferences to creating a
	 * string representing all preferences. Method Returns this string.
	 */
	public String getCategoryPreferences() {

		String preferences = null;

		String category = ArrAdpt.getItem(spinner.getSelectedItemPosition());

		Cursor cursor = db
				.rawQuery(
						"SELECT _id, category, preference_1, preference_2, preference_3 FROM category   WHERE category = ?",
						new String[] { "" + category });

		StringBuffer sb = new StringBuffer();
		if (cursor.getCount() == 1) {

			cursor.moveToFirst();

			String pref1 = cursor.getString(cursor
					.getColumnIndex("preference_1"));
			String pref2 = cursor.getString(cursor
					.getColumnIndex("preference_2"));
			String pref3 = cursor.getString(cursor
					.getColumnIndex("preference_3"));

			sb.append(URLEncoder.encode(pref1));

			sb.append("+");
			sb.append(URLEncoder.encode(pref2));
			sb.append("+");

			sb.append(URLEncoder.encode(pref3));

		}
		cursor.close();
		preferences = sb.toString();

		return preferences;
	}

	private void findAllViewsById() {
		searchEditText = (EditText) findViewById(R.id.search_edit_text);
		nameSearchRadioButton = (RadioButton) findViewById(R.id.name_search_radio_button);
		upcSearchRadioButton = (RadioButton) findViewById(R.id.upc_search_radio_button);
		eanSearchRadioButton = (RadioButton) findViewById(R.id.ean_search_radio_button);
		mpnSearchRadioButton = (RadioButton) findViewById(R.id.mpn_search_radio_button);
		searchRadioGroup = (RadioGroup) findViewById(R.id.search_radio_group);
		
		searchButton = (Button) findViewById(R.id.search_button);
		checkBox = (CheckBox) findViewById(R.id.CheckBox01);
	}

	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private OnClickListener radioButtonListener = new OnClickListener() {
		public void onClick(View v) {
			RadioButton radioButton = (RadioButton) v;
			// searchTypeTextView.setText(radioButton.getText());

		}
	};

	private class DftTextOnFocusListener implements OnFocusChangeListener {

		private String defaultText;

		public DftTextOnFocusListener(String defaultText) {
			this.defaultText = defaultText;
		}

		/*
		 * to able the typed query remains there whether the text box has the
		 * focus or not. And when the text box is empty and loses focus, a
		 * predefined message appears.
		 */

		public void onFocusChange(View v, boolean hasFocus) {
			if (v instanceof EditText) {
				EditText focusedEditText = (EditText) v;
				// handle obtaining focus
				if (hasFocus) {
					if (focusedEditText.getText().toString()
							.equals(defaultText)) {
						focusedEditText.setText(EMPTY_STRING);
					}
				}
				// handle losing focus
				else {
					if (focusedEditText.getText().toString()
							.equals(EMPTY_STRING)) {
						focusedEditText.setText(defaultText);
					}
				}
			}
		}

	}

	private void performSearch(String query) {

		if (nameSearchRadioButton.isChecked()) {
			progressDialog = ProgressDialog.show(ProductSearchAppActivity.this,
					"Please wait...", "Retrieving Product data...", true, true);
			String preference = getCategoryPreferences();
			String preferedQuery = null;// get the preferences of the requested
										// product category.
			if (checkBox.isChecked()) {
				StringBuffer sb = new StringBuffer();
				sb.append(query);
				sb.append("+");
				sb.append(preference);
				preferedQuery = sb.toString(); // constructing the search query
			} else {
				

				preferedQuery = query;
			}
			PerformProductbyNameSearchTask task = new PerformProductbyNameSearchTask();
			task.execute(preferedQuery); // starting the Task -
											// PerformProductbyNameSearchTask

			progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(
					task)); // sole purpose of implementing OnCancelListener of
							// the progress dialog is user is able to cancel the
							// relevant task.
		} else if (upcSearchRadioButton.isChecked()) {

			if (!checkQueryType(query)) {
				createDialog(upc);
			} else if (!checkNoOfDigits(upc, query)) {
				createDialog(notupc);
			} else {
				progressDialog = ProgressDialog.show(
						ProductSearchAppActivity.this, "Please wait...",
						"Retrieving data...", true, true);
				type = "UPC";
				PerformProductbyNumberSearchTask task = new PerformProductbyNumberSearchTask();
				task.execute(query);
				progressDialog
						.setOnCancelListener(new CancelTaskOnCancelListener(
								task));

			}
		} else if (eanSearchRadioButton.isChecked()) {

			if (!checkQueryType(query)) {
				createDialog(ean);
			} else if (!checkNoOfDigits(ean, query)) {
				createDialog(notean);
			} else {
				progressDialog = ProgressDialog.show(
						ProductSearchAppActivity.this, "Please wait...",
						"Retrieving data...", true, true);
				type = "EAN";
				PerformProductbyNumberSearchTask task = new PerformProductbyNumberSearchTask();
				task.execute(query);
				progressDialog
						.setOnCancelListener(new CancelTaskOnCancelListener(
								task));

			}
		} else if (mpnSearchRadioButton.isChecked()) {

			progressDialog = ProgressDialog.show(ProductSearchAppActivity.this,
					"Please wait...", "Retrieving data...", true, true);
			type = "MPN";
			PerformProductbyNumberSearchTask task = new PerformProductbyNumberSearchTask();
			task.execute(query);
			progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(
					task));
		}

	}

	private boolean checkQueryType(String query) {
		boolean typeInteger = true;
		double i = 0;
		try {
			i = Double.parseDouble(query);

		} catch (NumberFormatException ex) {
			ex.printStackTrace();

			typeInteger = false;

		}

		return typeInteger;

	}

	private void createDialog(int id) {

		if (id == 1) {

			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("You have entered Other than Digits for the UPC Number.Check the UPC Number You Entered.");

			// add a neutral button to the alert box and assign a click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// the button was clicked

							searchEditText.setText(null);
						}
					});

			alertbox.show();
		} else if (id == 2) {

			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("You have entered Other than Digits for the EAN Number.Check the EAN Number You Entered.");

			// add a neutral button to the alert box and assign a click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// the button was clicked

							searchEditText.setText(null);
						}
					});

			alertbox.show();
		} else if (id == 3) {
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("You have entered Less or More than 12 Digits. UPC Number contains only 12 digits.Check the UPC Number You Entered.");

			// add a neutral button to the alert box and assign a click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// the button was clicked

						}
					});

			alertbox.show();
		} else if (id == 4) {
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("You have entered Less or More than 13 Digits. EAN Number contains only 13 digits.Check the EAN Number You Entered.");

			// add a neutral button to the alert box and assign a click listener
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// the button was clicked

						}
					});

			alertbox.show();
		}
	}

	private boolean checkNoOfDigits(int type, String query) {
		boolean isRightNoOfDigits = true;

		if (type == 1) {
			if (query.length() == 12) {
				// do nothing
			} else {
				isRightNoOfDigits = false;
			}

		}

		else if (type == 2) {
			if (query.length() == 13) {
				// do nothing
			} else {
				isRightNoOfDigits = false;
			}

		}

		return isRightNoOfDigits;
	}

	// method that enable the user to cancel the Task before it executes
	// completely.

	private class CancelTaskOnCancelListener implements OnCancelListener {
		private AsyncTask<?, ?, ?> task;

		/*
		 * AsyncTask enables proper and easy use of the UI thread. This class
		 * allows to perform background operations and - publish results on the
		 * UI thread without having to manipulate threads or handlers.
		 */

		public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
			this.task = task;
		}

		public void onCancel(DialogInterface dialog) {
			if (task != null) {
				task.cancel(true);
			}
		}
	}

	/*
	 * Task class used for perform Search Request-Request by a Product Namethe
	 * type of the parameters sent to the task upon execution are of type
	 * String. void - no progress units will be published during the background
	 * computation the result of the background computation is of type List
	 * which holds Product objects
	 */

	private class PerformProductbyNameSearchTask extends
			AsyncTask<String, Void, ArrayList<Product>> {

		@Override
		protected ArrayList<Product> doInBackground(String... params) { // perform
																		// the
																		// actual
																		// HTTP
																		// data
																		// retrieval
			String query = params[0];
			return byNameSeeker.find(query, 5);
		}

		@Override
		protected void onPostExecute(final ArrayList<Product> result) { // present
																		// the
																		// results
																		// by
																		// calling
																		// a
																		// another
																		// activity.
			runOnUiThread(new Runnable() {
				public void run() {
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					Intent intent = new Intent(ProductSearchAppActivity.this,
							ProductsListActivity.class);
					intent.putExtra("products", result);

					startActivity(intent);

				}
			});
		}

	}

	/*
	 * Task class used for perform Product Request-Request by a Product IDthe
	 * type of the parameters sent to the task upon execution are of type
	 * String. void - no progress units will be published during the background
	 * computation the result of the background computation is of type List
	 * which holds Product objects
	 */

	private class PerformProductbyNumberSearchTask extends
			AsyncTask<String, Void, ArrayList<Product>> {

		@Override
		protected ArrayList<Product> doInBackground(String... params) {
			String query = params[0];
			return byNumberSeeker.find(query, type, 3);
		}

		@Override
		protected void onPostExecute(final ArrayList<Product> result) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}

					Intent intent = new Intent(ProductSearchAppActivity.this,
							ProductsListActivity.class);
					intent.putExtra("products", result);

					startActivity(intent);

				}
			});
		}

	}

}
