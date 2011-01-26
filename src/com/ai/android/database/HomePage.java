package com.ai.android.database;



import com.ai.android.productsearch.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomePage extends Activity{
	
	protected Button newCategory;
	protected Button editCategory;
	protected Button deleteCategory;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        
      newCategory=(Button) findViewById(R.id.new_category_button);
       editCategory= (Button) findViewById(R.id.show_category_button) ;
      deleteCategory = (Button) findViewById(R.id.delete_category_button) ;
      
       //direct use to the Add category.
       newCategory.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getApplicationContext(),AddCategory.class);
				startActivity(i);
			}
		});
       
       //direct user to edit category details.
       editCategory.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getApplicationContext(),CategoryList.class);
				startActivity(i);
			}
		});
       
         // direct user to delete category.
       deleteCategory.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getApplicationContext(),DeleteCategory.class);
				startActivity(i);
			}
		}); 
	}
	
	
	
	
}
