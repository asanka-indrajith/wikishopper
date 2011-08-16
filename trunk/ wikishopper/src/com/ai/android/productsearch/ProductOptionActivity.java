package com.ai.android.productsearch;

import java.io.InputStream;
import java.util.ArrayList;
import com.ai.android.io.FlushedInputStream;
import com.ai.android.model.Product;
import com.ai.android.services.GenericSeeker;
import com.ai.android.services.HttpRetriever;
import com.ai.android.services.ProductByIDSeeker;
import com.ai.android.services.ProductCompetitiorsSeeker;
import com.ai.android.services.RelatedProductSeeker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductOptionActivity extends Activity {
	
	Button description;
	Button proscons;
	Button related;
	Button competitors;
	TextView name;
	TextView category;
	TextView price;
	TextView relaseDate;
	TextView proscore;
	TextView reviews;
	TextView manufacture;
	TextView lovit;
	TextView wantit;
	TextView haveit;
	Product product;
	Product newProduct;
	ImageView image;
	ImageView imageLovit;
	ImageView imageWantit;
	ImageView imageHaveit;
	
	private ProgressDialog progressDialog;
	private HttpRetriever httpRetriever = new HttpRetriever();
	private GenericSeeker<Product> relatedsearch = new RelatedProductSeeker();
	private GenericSeeker<Product> competitorssearch = new ProductCompetitiorsSeeker();
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_product_layout);
		product = (Product) getIntent().getSerializableExtra("singleproduct");
		//newProduct = retriveDetails();
		//newProduct=product;
		
		LoadData();
		findViews();
		//populateFields();
		
		description.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				

				Intent intent=new Intent(ProductOptionActivity.this, DescriptionImageMoreTabActivity.class );
				intent.putExtra("product_description_image_more", newProduct);
				startActivity(intent);
					
				
			}
		});
		
		proscons.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				method();
			}
		});
		
		related.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(ProductOptionActivity.this,
						"Please wait...", "Searching  Related products...", true,
						true);
				PerformProductRelatedSearchTask task = new PerformProductRelatedSearchTask();
				task.execute(newProduct.id); // starting the Task -
												// PerformProductbyNameSearchTask

				progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(
						task));

			}
		});

		competitors.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				progressDialog = ProgressDialog.show(ProductOptionActivity.this,
						"Please wait...", "Searching Competitors...", true, true);
				PerformProductCompetitorsSearchTask task = new PerformProductCompetitorsSearchTask();
				task.execute(newProduct.id); // starting the Task -
												// PerformProductbyNameSearchTask

				progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(
						task));

			}
		});

		
	}
  
	private void method(){
		
		if (product.number_of_reviews.equals("0")|| product.proscore.equals("-1")){
			AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			alertbox.setMessage("Sorry We coudn't find Pos and Cons for this Product.");
			alertbox.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
								
							Intent intent = new Intent(ProductOptionActivity.this,
									KeyFeaturesList.class);
							intent.putExtra("products_key", newProduct.keyFeatures);

							startActivity(intent);
						}
					});

			alertbox.show();
		}else{
			Intent intent=new Intent(ProductOptionActivity.this, ProsConsKeyTabActivity.class );
			intent.putExtra("product_prosconskey", newProduct);
			startActivity(intent);
			
			
		}	
	}
	
	private void findViews(){
		
		description=(Button) findViewById(R.id.descrption_button);
		proscons=(Button) findViewById(R.id.proscons_button);
		related=(Button) findViewById(R.id.related_button);	
		competitors=(Button) findViewById(R.id.competitors_button);	
		name=(TextView) findViewById(R.id.name_text_view);
		category=(TextView)findViewById(R.id.category_text_view);
		price=(TextView) findViewById(R.id.price_text_view);
		relaseDate=(TextView) findViewById(R.id.releasedate_text_view);
		proscore=(TextView) findViewById(R.id.proscore_text_view);
		reviews=(TextView) findViewById(R.id.reviews_text_view);
		manufacture=(TextView) findViewById(R.id.manufacture_text_view);
		lovit= (TextView) findViewById(R.id.lovit_text_view);
		wantit=(TextView) findViewById(R.id.wantit_text);
		haveit=(TextView) findViewById(R.id.haveit_text);
		image=(ImageView)findViewById(R.id.product_thumb_icon);
		imageLovit=(ImageView) findViewById(R.id.image_loveit);
		imageWantit=(ImageView) findViewById(R.id.image_wantit);
		imageHaveit=(ImageView)findViewById(R.id.image_haveit);
			
	}
	private void LoadData(){
		
		progressDialog = ProgressDialog.show(ProductOptionActivity.this,
        		"Please wait...", "Retrieving data...", true, true);
		
		
		final ProductDetailTask dtask=new ProductDetailTask();
		dtask.execute();
		
		
		
		
	progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(
				dtask)); 
		
	}
	private void populateFields(){
		
		String value=newProduct.keyFeatures.get("MSRP");
		String date=newProduct.keyFeatures.get("Release Date");
		String manu=newProduct.keyFeatures.get("Manufacturer");
		String lov=newProduct.tags.get("love it");
		String want=newProduct.tags.get("want it");
		String hav=newProduct.tags.get("have it");
		price.setVisibility(0);
		relaseDate.setVisibility(0);
		manufacture.setVisibility(0);
	
		name.setText(newProduct.title);
		
		proscore.setText("Proscore: "+ newProduct.proscore);
		
		reviews.setText("No of Reviews: "+newProduct.number_of_reviews);
		
		category.setText("Category: "+ newProduct.category);
		if(value!=null){
		price.setText("Price: $"+newProduct.keyFeatures.get("MSRP"));	
		}
		if(date!=null){
		relaseDate.setText("Release Date: "+newProduct.keyFeatures.get("Release Date"));
		}
		if(manu!=null){
		manufacture.setText("Manufacturer: "+newProduct.keyFeatures.get("Manufacturer"));
		}
			lovit.setVisibility(0);
		if(lov!=null){
			lovit.setText(" "+newProduct.tags.get("love it"));
			}
			wantit.setVisibility(0);
			if(want!=null){
			wantit.setText(" "+newProduct.tags.get("want it"));
			}
			haveit.setVisibility(0);
			if(hav!=null){
			haveit.setText(" "+newProduct.tags.get("have it"));
			}
		
		
	}
	
	public Product retriveDetails() {

		ProductByIDSeeker proseeker = new ProductByIDSeeker();
		return proseeker.find(product.id);
	}
	
	private class CancelTaskOnCancelListener implements OnCancelListener {
		private AsyncTask<?, ?, ?> task;

		/*
		 * AsyncTask enables proper and easy use of the UI thread. This class
		 * allows to perform background operations and - publish results on the
		 * UI thread without having to manipulate threads or handlers.
		 */

		public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
			this.task = task;
		}

		public void onCancel(DialogInterface dialog) {
			if (task != null) {
				task.cancel(true);
			}
		}
	}
	
private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            InputStream is = httpRetriever.retrieveStream(url);
            if (is==null) {
				return null;
			}
            return BitmapFactory.decodeStream(new FlushedInputStream(is));
        }
        
        @Override
		protected void onPostExecute(final Bitmap result) {			
			runOnUiThread(new Runnable() {
		    	public void run() {
		    		if (progressDialog!=null) {
		    			progressDialog.dismiss();
		    			progressDialog = null;
		    		}
		    		if (result!=null) {
		    		image.setImageBitmap(result);
		    		}		    		
		    	}
		    });
		}
        
    }

private class ProductDetailTask extends AsyncTask<String, Void, Product> {
    
    @Override
    protected Product doInBackground(String... params) {
       // String url = params[0];
       // InputStream is = httpRetriever.retrieveStream(url);
        ProductByIDSeeker proseeker = new ProductByIDSeeker();
		return proseeker.find(product.id);
        
    }
    
    @Override
	protected void onPostExecute(final Product result) {			
		runOnUiThread(new Runnable() {
	    	public void run() {
	    		if (progressDialog!=null) {
	    			progressDialog.dismiss();
	    			progressDialog = null;
	    		}
	    		if (result!=null) {
	    		newProduct=result;
	    		
	    		populateFields();
	    		description.setVisibility(0);
	    		proscons.setVisibility(0);
	    		related.setVisibility(0);
	    		competitors.setVisibility(0);
	    		imageLovit.setVisibility(0);
	    		imageWantit.setVisibility(0);
	    		imageHaveit.setVisibility(0);
	    		String url = product.retrieveMediumImage();
	    		final ImageDownloaderTask task = new ImageDownloaderTask();
	        	task.execute(url);
	    		
	    		}		    		
	    	}
	    });
	}
    
	}
	
	private class PerformProductRelatedSearchTask extends
		AsyncTask<String, Void, ArrayList<Product>> {

@Override
	protected ArrayList<Product> doInBackground(String... params) { // perform

			String query = params[0];
			return relatedsearch.find(query, 5);
}

@Override
	protected void onPostExecute(final ArrayList<Product> result) { 
		runOnUiThread(new Runnable() {
				public void run() {
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
		Intent intent = new Intent(ProductOptionActivity.this,
				ProductRelatedListActivity.class);
		intent.putExtra("products", result);

		startActivity(intent);

				}
		});
	}

}

	private class PerformProductCompetitorsSearchTask extends
		AsyncTask<String, Void, ArrayList<Product>> {

		@Override
		protected ArrayList<Product> doInBackground(String... params) { 
				String query = params[0];
				return competitorssearch.find(query, 5);
		}

		@Override
		protected void onPostExecute(final ArrayList<Product> result) { // present

			runOnUiThread(new Runnable() {
				public void run() {
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					Intent intent = new Intent(ProductOptionActivity.this,
							ProductRelatedListActivity.class);
						intent.putExtra("products", result);

							startActivity(intent);

				}
			});
		}
		
}



}

