package com.ai.android.database;

import com.ai.android.productsearch.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends Activity {

	protected SQLiteDatabase db;
	protected Button addCategory;
	protected EditText categoryName;
	protected EditText preference1;
	protected EditText preference2;
	protected EditText preference3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_category);

		db = (new DatabaseHelper(this)).getWritableDatabase();

		findView();
		addCategory.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addCategory();
			}
		});

	}

	protected void findView() {

		addCategory = (Button) findViewById(R.id.add_category_button);
		categoryName = (EditText) findViewById(R.id.category_edit);
		preference1 = (EditText) findViewById(R.id.pref1_edit);
		preference2 = (EditText) findViewById(R.id.pref2_edit);
		preference3 = (EditText) findViewById(R.id.pref3_edit);
	}

	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/*
	 * Inserting a new category with preferences Also checks whether already
	 * there is an category name equal to the new input . If there is inform
	 * user.
	 */
	protected void addCategory() {
		String category = categoryName.getText().toString();
		String pref1 = preference1.getText().toString();
		String pref2 = preference2.getText().toString();
		String pref3 = preference3.getText().toString();

		category = category.toUpperCase();
		ContentValues initialValues = new ContentValues();
		initialValues.put("category", category);
		initialValues.put("preference_1", pref1);
		initialValues.put("preference_2", pref2);
		initialValues.put("preference_3", pref3);

		ContentValues initialValues2 = new ContentValues();
		initialValues2.put("category", category);

		// checks whether already there is an category name equal to the new
		// input.
		Cursor cursr = db
				.rawQuery(
						"SELECT _id, category, preference_1, preference_2, preference_3 FROM category   WHERE category = ?",
						new String[] { "" + category });

		if (cursr.getCount() == 0) {
			db.insert("category", null, initialValues);
			db.close();
			cursr.close();
			Intent intent = new Intent(this, HomePage.class);

			startActivity(intent);

		} else {

			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage(category
					+ " already contains.Try a different name ");
			alertbox.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {

						}
					});

			alertbox.show();

		}
	}

}
