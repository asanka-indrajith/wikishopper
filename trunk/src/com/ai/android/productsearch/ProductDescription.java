package com.ai.android.productsearch;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ProductDescription extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_engine_view);

        
   String    description = (String)getIntent().getSerializableExtra("product_description");
   
        WebView engine = (WebView) findViewById(R.id.web_engine);
        //engine.loadUrl("http://mobile.tutsplus.com");
       engine.getSettings().setJavaScriptEnabled(true);
        
       description=description.replace('"', ' ');                            //removing unnecessary  '" ' marks from the string value
        engine.loadData(description, "text/html", "UTF-8");                  //declaring the string source and type of the source and type of the conversion
    }

}
