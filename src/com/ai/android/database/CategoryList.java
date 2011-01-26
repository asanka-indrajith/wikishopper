package com.ai.android.database;

import com.ai.android.productsearch.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class CategoryList extends ListActivity {

	protected EditText searchText;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.categry_main);
        searchText = (EditText) findViewById(R.id.searchText);
    	db = (new DatabaseHelper(this)).getWritableDatabase();
    }
    
    //perform the action when a category is selected
    public void onListItemClick(ListView parent, View view, int position, long id) {
    	Intent intent = new Intent(this, CategoryDetails.class);
    	Cursor cursor = (Cursor) adapter.getItem(position);
    	intent.putExtra("CATEGORY_ID", cursor.getInt(cursor.getColumnIndex("_id")));
    	startActivity(intent);
    	cursor.close();
    }
    
    /*
     * retrieve category entities in which category names are equal or similar to the one user entered.
     * retrieved category names then put in a simple Cursor Adapter
     * */
    public void search(View view) {
    	// || is the concatenation operation in SQLite
		cursor = db.rawQuery("SELECT _id, category FROM category WHERE preference_1 || ' ' || category LIKE ?", 
						new String[]{"%" + searchText.getText().toString() + "%"});
		adapter = new SimpleCursorAdapter(
				this, 
				R.layout.category_list_item, 
				cursor, 
				new String[] {"category"}, 
				new int[] {R.id.firstName});
		setListAdapter(adapter);
		
		db.close();
		
    }
    
}