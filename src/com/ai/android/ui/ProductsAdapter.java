package com.ai.android.ui;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai.android.io.FlushedInputStream;

import com.ai.android.model.Product;
import com.ai.android.services.HttpRetriever;
import com.ai.android.util.Utils;
import com.ai.android.productsearch.R;


public class ProductsAdapter extends ArrayAdapter<Product> {
	
	private HttpRetriever httpRetriever = new HttpRetriever();
	
	private ArrayList<Product> productDataItems;
	
	private Activity context;
	
	/*
	 * A ListAdapter that manages a ListView backed by an array of Product objects.
	 * Also Initialized with the products list retrieved from the List activity.
	 * */
	
	public ProductsAdapter(Activity context, int textViewResourceId, ArrayList<Product> productDataItems) {
        super(context, textViewResourceId, productDataItems);
        this.context = context;
        this.productDataItems = productDataItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);           // Using LayoutInflater service to retrieve an XML layout during runtime 
			view = vi.inflate(R.layout.product_data_row, null);
		}
		
		Product product = productDataItems.get(position);
		
		if (product != null) {
			
			// title
			TextView titleTextView = (TextView) view.findViewById(R.id.name_text_view);                             // Take reference of each of the views widgets 
			titleTextView.setText(product.title);                                                                   // providing  the relevant text,
			
			// category
			TextView categoryTextView = (TextView) view.findViewById(R.id.rating_text_view);
			categoryTextView.setText("Category: " + product.category);
			
			// proscore 
			TextView proscoreTextView = (TextView) view.findViewById(R.id.released_text_view);
			proscoreTextView.setText("Proscore: " + product.proscore);
			
			// number of reviews
			TextView reviewsTextView = (TextView) view.findViewById(R.id.certification_text_view);
			reviewsTextView.setText("Reviews: " + product.number_of_reviews);
			
			// product id
			TextView pidTextView = (TextView) view.findViewById(R.id.language_text_view);
			pidTextView.setText("Product ID: " + product.id);
			
			// thumb image
			ImageView imageView = (ImageView) view.findViewById(R.id.movie_thumb_icon);                          // providing ImageView a Bitmap that contains the thumbnail image. 
	         String url = product.retrieveThumbnail();                                                            // getting the url of Thumbnail -url of the image object which type is smallImage.                                                                  
		
	         
	         /*
	          * A very basic caching mechanism is used at this point in order to avoid re-downloading the same image again and again
	          * If the image is not found in the cache, by a background task is launched or by directly to retrieve the image.
	          * For slower Internet connection directly retrieving is better. 
	          * */
	         
		if (url!=null) {
				Bitmap bitmap = fetchBitmapFromCache(url);
			if (bitmap==null) {				
					
					
					InputStream is = httpRetriever.retrieveStream(url);
					if (is==null) {
						new BitmapDownloaderTask(imageView).execute(url);
						}
					 bitmap= BitmapFactory.decodeStream(new FlushedInputStream(is));                      //  using the decodeStream method of the BitmapFactory class to create a new Bitmap object                       
					                                                                                      // //first wrap the InputStream  with a FlushedInputStream class.otherwise an exception will through over slower connection.   
					 addBitmapToCache(url, bitmap);
					 imageView.setImageBitmap(bitmap);
				}
			else {
					imageView.setImageBitmap(bitmap);
				}
			}
			else {
				imageView.setImageBitmap(null);
			}
			
		}
		
		return view;
		
	}
	
	private LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();             // the map containing the URL-Bitmap association.
	
	private void addBitmapToCache(String url, Bitmap bitmap) {                  // method use to add bitmap into the cache.
        if (bitmap != null) {
            synchronized (bitmapCache) {
                bitmapCache.put(url, bitmap);
            }
        }
    }
	
	private Bitmap fetchBitmapFromCache(String url) {
		
		synchronized (bitmapCache) {
            final Bitmap bitmap = bitmapCache.get(url);
            if (bitmap != null) {
                // Bitmap found in cache
                // Move element to first position, so that it is removed last
                bitmapCache.remove(url);
                bitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        return null;
        
	}
	/*
	 *  background task that  is launched in order to retrieve the image and store it in the cache for next calls.
	 * inside each task, the ImageView instance is referenced via a WeakReference-done for performance reasons.
	 * 
	 * */
	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> { 
		
        private String url;
		private final WeakReference<ImageView> imageViewReference;                           //in order to allow the VM's garbage collector to collect any ImageViews that might belong to a killed activity

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            
            InputStream is = httpRetriever.retrieveStream(url);
            try{
		if (is==null) {
				return null;
			}
		final Bitmap bitmap =BitmapFactory.decodeStream(new FlushedInputStream(is));                             //   using the decodeStream method of the BitmapFactory class to create a new Bitmap object.                      
		return bitmap;
        }
		finally{Utils.closeStreamQuietly(is); }                             
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {        	
            if (isCancelled()) {
                bitmap = null;
            }
            
            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                imageView.setImageBitmap(bitmap);                             //displaying bitmap Image in ImageView.
            }
        }
    }
	
}
