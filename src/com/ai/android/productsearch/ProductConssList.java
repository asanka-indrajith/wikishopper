package com.ai.android.productsearch;

import java.util.ArrayList;

import com.ai.android.model.Product;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProductConssList extends Activity {

	private ListView mainListView ;
	  private ArrayAdapter<String> listAdapter ;
	  Product product;
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.product_details);
	    
	    
	    
	    mainListView = (ListView) findViewById( R.id.mainListView );
	    product = (Product )getIntent().getSerializableExtra("productcons");
	    
	    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
	    	      @Override  
	    	       public void onItemClick( AdapterView<?> parent, View item,   
	    	                                int position, long id) {  
	    	    	  
	    	    	
	    	    	 
	    	         
	    	       }  
	    	     }); 
	    
	    				// assigning  product's cons Array List
            ArrayList<String> menuList = product.cons;
            menuList.add(0, "PRODUCT COLLABORATIVE REVIEW - CONS FOR : "+ product.title);

                //Create ArrayAdapter using the planet list.
            listAdapter = new ArrayAdapter<String>(this, R.layout.product_details_row, menuList);




                 //Set the ArrayAdapter as the ListView's adapter.
            mainListView.setAdapter( listAdapter );      
	}
	
	
}
