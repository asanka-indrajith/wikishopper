package com.ai.android.productsearch;



import com.ai.android.model.Product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ProsConsKeyTabActivity extends TabActivity {
	
	Product product;
	
	public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.tab_layout);
     
     product=(Product) getIntent().getSerializableExtra("product_prosconskey");
     TabHost tabHost=getTabHost();
     // no need to call TabHost.Setup()        
     
     //First Tab
     TabSpec pros=tabHost.newTabSpec("Pros");
     pros.setIndicator("Pros",getResources().getDrawable(R.drawable.ic_tab_thumbup5 ));
     Intent proActivity=new Intent(this, ProscoreList.class);
     proActivity.putExtra("productpros", product.prosm);
     pros.setContent(proActivity);
     
     TabSpec cons=tabHost.newTabSpec("Cons");
     cons.setIndicator("Cons",getResources().getDrawable(R.drawable.ic_tab_ic_tab_thumbdown ));
      Intent conActivity=new Intent(this, ProscoreList.class);
      conActivity.putExtra("productpros", product.consm);
      cons.setContent(conActivity);
      
      TabSpec key=tabHost.newTabSpec("Key Features");
      key.setIndicator("Key Features",getResources().getDrawable(R.drawable.ic_tab_keyfeatures ));
       Intent keyActivity=new Intent(this, KeyFeaturesList.class);
       keyActivity.putExtra("products_key", product.keyFeatures);
       key.setContent(keyActivity);
       
       tabHost.addTab(pros);
       tabHost.addTab(cons);
       tabHost.addTab(key);

}
}