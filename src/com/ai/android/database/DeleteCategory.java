package com.ai.android.database;



import com.ai.android.productsearch.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class DeleteCategory extends Activity{
    private final String TAG = getClass().getSimpleName();
    protected Button deleteButton;
    ArrayAdapter<String> ArrAdpt ;
    Spinner spinner;
    SQLiteDatabase db;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_category);
        
        db = (new DatabaseHelper(this)).getWritableDatabase();
        
       
        
        performSpinner();
        
        deleteButton =(Button) findViewById(R.id.delete_button);
        
        deleteButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				deleteCategory();
				
				
				
			}
		});
        
    }
    
	
	//spinner Listener class.
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	      
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
	// Load current category names from the database into the spinner.
	public void performSpinner(){
		
		Cursor cursor = db.rawQuery("SELECT  category FROM category", 	null);
		   
        
        
        String[] cat = new String[cursor.getCount()];
        
        cursor.moveToFirst();
        
        
        //putting cursor objects in to a String array
        for(int i=0;i<cursor.getCount();i++){
        	cat[i]=cursor.getString(cursor.getColumnIndex("category"));
        	cursor.moveToNext();
        }
        
      
        
         spinner = (Spinner) findViewById(R.id.delete_spinner);    
         ArrAdpt = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,cat);       
        ArrAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        spinner.setAdapter(ArrAdpt);       
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        cursor.close();
	}
	
	
	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
		//delete selected category from the database.
	public void deleteCategory(){
		
		
		 
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage("Are you sure you want to delete this category and preferences from Database?");

        // set a  yes button and create a listener
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	
            public void onClick(DialogInterface arg0, int arg1) {
            	String category = ArrAdpt.getItem(spinner.getSelectedItemPosition());
        		db.execSQL("Delete from category where category = ?", new String[]{""+category});
        		performSpinner();
        		goBack();
        		
        		
            }
        });

        // set a negative/no button and create a listener
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

           
            public void onClick(DialogInterface arg0, int arg1) {
            	
                
            }
        });
      
        alertbox.show();
		
		
       
    	
	}
	public void goBack(){
		db.close();
		Intent intent=new Intent(getApplicationContext(),HomePage.class);
		startActivity(intent);
		
		
	}
	
}
