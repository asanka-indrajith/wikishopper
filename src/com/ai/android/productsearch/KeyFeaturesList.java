package com.ai.android.productsearch;

import java.util.ArrayList;
import java.util.HashMap;

import com.ai.android.ui.KeyFeaturesAdapter;



import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class KeyFeaturesList extends ListActivity{
	private final String TAG = getClass().getSimpleName();
	private ArrayList<String> featuresList = new ArrayList<String>();
	private ArrayList<String> featuresDupList = new ArrayList<String>();
	private HashMap<String,String> key =new HashMap<String, String>();
	private KeyFeaturesAdapter featuresAdapter;
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_features_layout);

        featuresAdapter = new KeyFeaturesAdapter(this, R.layout.key_features_data_row, featuresList);
        key = (HashMap<String, String>) getIntent().getSerializableExtra("products_key");            
        
        for (String feature : key.keySet()){
			
			String combind= feature + "1011"+key.get(feature);
			featuresDupList.add(combind);
			
        }
        featuresList=featuresDupList;
       
        setListAdapter(featuresAdapter);                                                        //associate our ListActivity with the ArrayAdapter-ProductsAdapter
        
        
        if (featuresList!=null && !featuresList.isEmpty()) {
        	
        	
        	featuresAdapter.notifyDataSetChanged();                                            // to notify the attached View that the underlying data has been changed and it should refresh itself.
        	
        	featuresAdapter.clear();
        	
        	int j= featuresList.size();
        	
    		for (int i = 0; i < featuresList.size(); i++) {
    			
    			featuresAdapter.add(featuresList.get(i));
    			
    		}
        }
        
        featuresAdapter.notifyDataSetChanged();
        
    }
	/*
	 * When item in the list is selected,retrieve the Movie object that is associated with the list item using the ArrayAdapter.
	 * Then parse that abject to ProductDeatilsList class.
	 * */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		
	}
	
	public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
