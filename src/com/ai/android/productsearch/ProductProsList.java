package com.ai.android.productsearch;
import java.util.ArrayList;
import java.util.Arrays;

import com.ai.android.model.Product;
import com.ai.android.productsearch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ProductProsList extends Activity{
	private ListView mainListView ;
	  private ArrayAdapter<String> listAdapter ;
	  Product product;
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.product_details);
	    
	    
	    
	    mainListView = (ListView) findViewById( R.id.mainListView );
	    product = (Product )getIntent().getSerializableExtra("productpros");
	    
	    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
	    	      @Override  
	    	       public void onItemClick( AdapterView<?> parent, View item,   
	    	                                int position, long id) {  
	    	    	  
	    	    	
	    	    	 
	    	         
	    	       }  
	    	     }); 
	    
	      ArrayList<String> menuList = new ArrayList<String>();  
	      
	                // assigning  product's cons Array List
          menuList.addAll(product.pros);
          menuList.add(0, "PRODUCT COLLABORATIVE REVIEW - PROS FOR : "+ product.title);

          		//Create ArrayAdapter using the menu list.
          listAdapter = new ArrayAdapter<String>(this, R.layout.product_details_row, menuList);




          		//Set the ArrayAdapter as the ListView's adapter.
          mainListView.setAdapter( listAdapter );      
	}

}
