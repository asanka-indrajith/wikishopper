package com.ai.android.database;

import com.ai.android.productsearch.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChangePreferences extends Activity {

	protected String[] preference;
	protected EditText changeText;
	protected SQLiteDatabase db;

	protected Button change_button;
	protected Button return_button;
	protected String change;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_change);

		db = (new DatabaseHelper(this)).getWritableDatabase();

		/*
		 * Passed String Array contains only three items. first one is the
		 * preference No second one is the category ID third one is the last
		 * preference value that need to update
		 */
		preference = getIntent().getStringArrayExtra("PREF");

		changeText = (EditText) findViewById(R.id.Change_text);
		change_button = (Button) findViewById(R.id.change_button);
		return_button = (Button) findViewById(R.id.return_button);
		changeText.setText(preference[2]);

		change_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				change = changeText.getText().toString();
				changePreference();
			}
		});

		return_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doReturn();
			}
		});
	}

	public void doReturn() {

		Intent intent = new Intent(this, CategoryDetails.class);
		intent.putExtra("CATEGORY_ID", Integer.parseInt(preference[1]));
		startActivity(intent);

	}

	// change selected preference of the category in the database.
	public void changePreference() {

		if (preference[0].equals("PREF1")) {
			ContentValues args = new ContentValues();

			args.put("preference_1", change);

			db.update("category", args, "_id" + "=" + preference[1], null);

		}
		if (preference[0].equals("PREF2")) {
			ContentValues args = new ContentValues();

			args.put("preference_2", change);

			db.update("category", args, "_id" + "=" + preference[1], null);

		}
		if (preference[0].equals("PREF3")) {
			ContentValues args = new ContentValues();

			args.put("preference_3", change);

			db.update("category", args, "_id" + "=" + preference[1], null);

		}

		db.close();

		Intent intent = new Intent(this, CategoryDetails.class);
		intent.putExtra("CATEGORY_ID", Integer.parseInt(preference[1]));
		startActivity(intent);
	}
}
