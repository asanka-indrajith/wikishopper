package com.ai.android.productsearch;

import com.ai.android.model.Product;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class DescriptionImageMoreTabActivity extends TabActivity{
	
	
Product product;
	
	public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.tab_layout);
     
     product=(Product) getIntent().getSerializableExtra("product_description_image_more");
     TabHost tabHost=getTabHost();
     // no need to call TabHost.Setup()        
     
    
     TabSpec description=tabHost.newTabSpec("Description");
     description.setIndicator("Description",getResources().getDrawable(R.drawable.ic_tab_description ));
     Intent descriptionActivity=new Intent(this, ProductDescription.class);
     descriptionActivity.putExtra("product_description", product);
     description.setContent(descriptionActivity);
     
     TabSpec image=tabHost.newTabSpec("Close Look");
     image.setIndicator("Close Look",getResources().getDrawable(R.drawable.ic_tab_image ));
      Intent imageActivity=new Intent(this, ProductWebView.class);
      imageActivity.putExtra("product_url", product.retrieveLargeImage());
      image.setContent(imageActivity);
      
      TabSpec more=tabHost.newTabSpec("More Detail");
      more.setIndicator("More Detail",getResources().getDrawable(R.drawable.ic_tab_more ));
       Intent moreActivity=new Intent(this, ProductWebView.class);
       moreActivity.putExtra("product_url", product.url);
       more.setContent(moreActivity);
       
       tabHost.addTab(description);
       tabHost.addTab(image);
       tabHost.addTab(more);

}
}