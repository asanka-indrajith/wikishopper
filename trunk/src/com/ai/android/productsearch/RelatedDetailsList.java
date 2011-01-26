package com.ai.android.productsearch;

import java.util.ArrayList;
import java.util.Arrays;

import com.ai.android.model.Product;

import com.ai.android.services.GenericSeeker;
import com.ai.android.services.ProductByIDSeeker;
import com.ai.android.services.RelatedProductSeeker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RelatedDetailsList extends Activity{
	
	private ListView mainListView ;
	  private ArrayAdapter<String> listAdapter ;
	 
	  Product newProduct;
	  private final String TAG = getClass().getSimpleName();
	 
	  private GenericSeeker<Product> relatedsearch = new RelatedProductSeeker();
	
	    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.product_details);
	    
	    
	    
	    mainListView = (ListView) findViewById( R.id.mainListView );
	    newProduct = (Product )getIntent().getSerializableExtra("singleproduct");
	    
	    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	    	
	    	      @Override  
	    	       public void onItemClick( AdapterView<?> parent, View item,   
	    	                                int position, long id) {  
	    	    	  
	    	    	
	    	    	  displayProductOption(position);
	    	         
	    	       }  
	    	     }); 
	    
	         String[] menuNames = new String[] { "PRODUCT COLLABORATIVE REVIEW - PROS", "PRODUCT COLLABORATIVE REVIEW - CONS", "DESCRIPTION","KEY FEATURES" ,"PRODUCT IMAGE", "MORE NFORMATION"};  
            ArrayList<String> menuList = new ArrayList<String>();
             menuList.addAll( Arrays.asList(menuNames) );

                       // Create ArrayAdapter using the menu list.
             listAdapter = new ArrayAdapter<String>(this, R.layout.product_details_row, menuList);


                  // Set the ArrayAdapter as the ListView's adapter.
               mainListView.setAdapter( listAdapter ); 

	    }
	    
	    
	
	
	
	
	/* facilitating actions according to the option chosen by user.
	 * by detecting the position of the selected item.
	 * */
	 
	public void displayProductOption(int position){
		if(position==0){
			Intent intent = new Intent(RelatedDetailsList.this, ProductProsList.class);
			intent.putExtra("productpros", newProduct);
			
			
	        startActivity(intent);
	       
		}
		else if(position==1){
			
			Intent intent = new Intent(RelatedDetailsList.this, ProductConssList.class);
			intent.putExtra("productcons", newProduct);
			
	        startActivity(intent);
		}
		else if(position==2){
			
			Intent intent = new Intent(RelatedDetailsList.this, ProductDescription.class);
			intent.putExtra("product_description", newProduct.description);
			
	        startActivity(intent);
		    
		        
		}
		
		else if(position==3){
			Intent intent = new Intent(RelatedDetailsList.this, KeyFeaturesList.class);
  		intent.putExtra("products_key", newProduct.keyFeatures);
  		
          startActivity(intent);
		    
			
		}
		else if(position==4){
			
			Intent intent = new Intent(RelatedDetailsList.this, ProductWebView.class);
			intent.putExtra("product_url", newProduct.retrieveLargeImage());
			
	        startActivity(intent);
		    
		}
		else if(position==5){
			
			Intent intent = new Intent(RelatedDetailsList.this, ProductWebView.class);
			intent.putExtra("product_url", newProduct.url);
			
	        startActivity(intent);
		    
		}
		
		
	}

}
