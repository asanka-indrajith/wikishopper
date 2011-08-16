package com.ai.android.database;

import java.util.ArrayList;
import java.util.List;

import com.ai.android.productsearch.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryDetails extends ListActivity {

	protected TextView nameText;
	protected TextView titleText;
	protected Button backButtton;
	protected List<CategoryAction> actions;
	protected CategoryActionAdapter adapter;
	protected int categoryId;
	protected int managerId;
	

	private final String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_details);

		categoryId = getIntent().getIntExtra("CATEGORY_ID", 0);
		SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();
		backButtton=(Button)findViewById(R.id.category_back_button);
		// getting attributes of the selected category from the Database
		Cursor cursor = db
				.rawQuery(
						"SELECT _id, category, preference_1, preference_2, preference_3 FROM category   WHERE _id = ?",
						new String[] { "" + categoryId });

		// checking whether only raw was bring out from the Database.
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();

			nameText = (TextView) findViewById(R.id.employeeName);
			nameText.setText("CATEGORY -");

			titleText = (TextView) findViewById(R.id.title);
			titleText.setText(cursor.getString(cursor
					.getColumnIndex("category")));

			actions = new ArrayList<CategoryAction>();

			/*
			 * every preference is put into a CategoryAction object. so They can
			 * be view in the List and take actions according to the item
			 * selected in the list.
			 */

			
			
	
			String preference1 = cursor.getString(cursor
					.getColumnIndex("preference_1"));
			if (preference1 != null) {
				actions.add(new CategoryAction("Preference 1", preference1,
						CategoryAction.ACTION_PREF1));

			}

			String preference2 = cursor.getString(cursor
					.getColumnIndex("preference_2"));
			if (preference2 != null) {
				actions.add(new CategoryAction("Preference 2", preference2,
						CategoryAction.ACTION_PREF2));
			}
			String preference3 = cursor.getString(cursor
					.getColumnIndex("preference_3"));
			if (preference3 != null) {
				actions.add(new CategoryAction("Preference 3", preference3,
						CategoryAction.ACTION_PREF3));
			}

			adapter = new CategoryActionAdapter();
			setListAdapter(adapter);
			cursor.close();
			db.close();
		}
		
		backButtton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				doReturnToCategoryList();
			}
			
		});
		
	}
	// actions taken when List item clicked - changing the selected preference.
	@Override
	public void onListItemClick(ListView parent, View view, int position,
			long id) {

		CategoryAction action = actions.get(position);

		Intent intent;
		switch (action.getType()) {

		case CategoryAction.ACTION_PREF1:
			intent = new Intent(this, ChangePreferences.class);
			intent.putExtra("PREF",
					new String[] { "PREF1", Integer.toString(categoryId),
							action.getData() });
			startActivity(intent);
			break;

		case CategoryAction.ACTION_PREF3:
			intent = new Intent(this, ChangePreferences.class);
			intent.putExtra("PREF",
					new String[] { "PREF3", Integer.toString(categoryId),
							action.getData() });
			startActivity(intent);
			break;

		case CategoryAction.ACTION_PREF2:
			intent = new Intent(this, ChangePreferences.class);
			intent.putExtra("PREF",
					new String[] { "PREF2", Integer.toString(categoryId),
							action.getData() });
			startActivity(intent);
			break;

		}
		
	
	}
	
	private void doReturnToCategoryList(){
		
		Intent intent=new Intent(CategoryDetails.this,CategoryList.class);
		startActivity(intent);
	}	
	/*
	 * Adapter class responsible for viewing category preferences. overrides the
	 * getView method to achieve this.
	 */
	class CategoryActionAdapter extends ArrayAdapter<CategoryAction> {

		CategoryActionAdapter() {
			super(CategoryDetails.this, R.layout.action_list_item, actions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CategoryAction action = actions.get(position);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.action_list_item, parent,
					false);
			TextView label = (TextView) view.findViewById(R.id.label);
			label.setText(action.getLabel());
			TextView data = (TextView) view.findViewById(R.id.data);
			data.setText(action.getData());
			return view;
		}

	}

}