package com.ai.android.productsearch;

import java.util.ArrayList;
import java.util.Arrays;

import com.ai.android.model.Product;

import com.ai.android.services.GenericSeeker;
import com.ai.android.services.ProductByIDSeeker;
import com.ai.android.services.RelatedProductSeeker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProductHalfDetailedList extends Activity {
	 private ListView mainListView ;
	  private ArrayAdapter<String> listAdapter ;
	  Product product;
	  Product newProduct;
	  private final String TAG = getClass().getSimpleName();
	  private ProgressDialog progressDialog;
	  private GenericSeeker<Product> relatedsearch = new RelatedProductSeeker();
	
	
	    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.product_details);
	    
	    
	    
	    mainListView = (ListView) findViewById( R.id.mainListView );
	    product = (Product )getIntent().getSerializableExtra("singleproduct");
	    newProduct=retriveDetails();
	    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	    	
	    	      @Override  
	    	       public void onItemClick( AdapterView<?> parent, View item,   
	    	                                int position, long id) {  
	    	    	  
	    	    	
	    	    	  displayProductOption(position);
	    	         
	    	       }  
	    	     }); 
	    
	         String[] menuNames = new String[] {"DESCRIPTION","KEY FEATURES" ,"PRODUCT IMAGE", "MORE NFORMATION","RELATED PRODUCTS"};  
             ArrayList<String> menuList = new ArrayList<String>();
              menuList.addAll( Arrays.asList(menuNames) );

                        // Create ArrayAdapter using the menu list.
              listAdapter = new ArrayAdapter<String>(this, R.layout.product_details_row, menuList);


                   // Set the ArrayAdapter as the ListView's adapter.
                mainListView.setAdapter( listAdapter ); 

	    }
	    
	    
	// retrieve full details of a product.
	public Product retriveDetails(){
		
		ProductByIDSeeker proseeker= new ProductByIDSeeker();
		 return proseeker.find(product.id);
	  }
	
	
	/* facilitating actions according to the option chosen by user.
	 * by detecting the position of the selected item.
	 * */
	 
	public void displayProductOption(int position){
		if(position==0){
			
			Intent intent = new Intent(ProductHalfDetailedList.this, ProductDescription.class);
			intent.putExtra("product_description", newProduct.description);
			
	        startActivity(intent);
		}
		else if(position==1){
			Intent intent = new Intent(ProductHalfDetailedList.this, KeyFeaturesList.class);
    		intent.putExtra("products_key", newProduct.keyFeatures);
    		
            startActivity(intent);
			
		}
		else if(position==2){
			
			Intent intent = new Intent(ProductHalfDetailedList.this, ProductWebView.class);
			intent.putExtra("product_url", newProduct.retrieveLargeImage());
			
	        startActivity(intent);
			
		    
		        
		}
		else if(position==3){
			Intent intent = new Intent(ProductHalfDetailedList.this, ProductWebView.class);
			intent.putExtra("product_url", newProduct.url);
			
	        startActivity(intent);
		    
		        
		}
		else if(position==4){
			
			progressDialog = ProgressDialog.show(ProductHalfDetailedList.this,
            		"Please wait...", "Retrieving data...", true, true);
			PerformProductRelatedSearchTask task = new PerformProductRelatedSearchTask();
        	task.execute(newProduct.id);                                                                  // starting the Task - PerformProductbyNameSearchTask
        	
        	progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
    
		        
		}
		
		
		
	}
	private class CancelTaskOnCancelListener implements OnCancelListener {              
    	private AsyncTask<?, ?, ?> task;  
    	
    	/*
    	 * AsyncTask enables proper and easy use of the UI thread. 
    	 * This class allows to perform background operations and -
    	 * publish results on the UI thread without having to manipulate threads or handlers.
    	 * */
    	
    	public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
    		this.task = task;
    	}
    	@Override
		public void onCancel(DialogInterface dialog) {
    		if (task!=null) {
        		task.cancel(true);
        	}
		}
    }
    
    
	/*
	 * Task class used for perform Search Request-Request by a Product Name
	 *the type of the parameters sent to the task upon execution are of type String. 
	 * void - no progress units will be published during the background computation
	 * the result of the background computation is of type List which holds Product objects
	 * */
    
	private class PerformProductRelatedSearchTask extends AsyncTask<String, Void, ArrayList<Product>> {  

		@Override
		protected ArrayList<Product> doInBackground(String... params) {     // perform the actual HTTP data retrieval
			String query = params[0];
			return relatedsearch.find(query, 6);
		}
		
		@Override
		protected void onPostExecute(final ArrayList<Product> result) {	    //present the results by calling a another activity.		
			runOnUiThread(new Runnable() {
		    	@Override
		    	public void run() {
		    		if (progressDialog!=null) {
		    			progressDialog.dismiss();
		    			progressDialog = null;
		    		}
		    		Intent intent = new Intent(ProductHalfDetailedList.this, ProductRelatedListActivity.class);
		    		intent.putExtra("products", result);
		    		
		            startActivity(intent);
		            
		    	}
		    });
		}
		
	}
	
	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
