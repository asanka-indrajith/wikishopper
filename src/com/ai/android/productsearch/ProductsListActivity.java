package com.ai.android.productsearch;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.ai.android.model.Product;
import com.ai.android.ui.ProductsAdapter;

public class ProductsListActivity extends ListActivity {
	
	
	private final String TAG = getClass().getSimpleName();
	private ArrayList<Product> productsList = new ArrayList<Product>();
	private ProductsAdapter productsAdapter;
	
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_layout);

        productsAdapter = new ProductsAdapter(this, R.layout.product_data_row, productsList);
        productsList = (ArrayList<Product>) getIntent().getSerializableExtra("products");            
        
        setListAdapter(productsAdapter);                                                        //associate our ListActivity with the ArrayAdapter-ProductsAdapter
        
        if (productsList!=null && !productsList.isEmpty()) {
        	
        	productsAdapter.notifyDataSetChanged();                                            // to notify the attached View that the underlying data has been changed and it should refresh itself.
        	productsAdapter.clear();
    		for (int i = 0; i < productsList.size(); i++) {
    			productsAdapter.add(productsList.get(i));
    		}
        }
        
        productsAdapter.notifyDataSetChanged();
        
    }
	/*
	 * When item in the list is selected,retrieve the Movie object that is associated with the list item using the ArrayAdapter.
	 * Then parse that abject to ProductDeatilsList class.
	 * */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		Product product = productsAdapter.getItem(position);
		if(product.number_of_reviews.equals("0")|| product.proscore.equals("-1")){
			Intent intent = new Intent(ProductsListActivity.this, ProductHalfDetailedList.class);
			intent.putExtra("singleproduct", product);
			
	        startActivity(intent);
		
		}
		
		else{
			
			Intent intent = new Intent(ProductsListActivity.this, ProductDetailsList.class);
			intent.putExtra("singleproduct", product);
			
	        startActivity(intent);
			
		}
	}
	
	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
}
