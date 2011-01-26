package com.ai.android.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import android.util.Log;

import com.ai.android.model.Image;

import com.ai.android.model.Product;

/*
 *Class used for a Search request 
 * */

public class ProductByNameSeeker extends GenericSeeker<Product> {
		

	private String url1="http://api.productwiki.com/connect/api.aspx?op=search&q=";
	private String url2=":&format=xml&key=2358feba734043a9b2f6c52520f5feb5";
	private static  String xmlUrl ;
	private final String TAG = getClass().getSimpleName();
	
	ArrayList<Product> prductList2;
	
	public ArrayList<Product> find(String query) {							// perform search without limitation.
																			
		ArrayList<Product> moviesList = retrieveProductsList(query);
		
		return moviesList;
	}
	
	public ArrayList<Product> find(String query, int maxResults) {			// limiting maximum numbers of results displaying to the user
		                                                                    //return an ArrayList of objects of the Product class.
		ArrayList<Product> moviesList = retrieveProductsList(query);
		
		return retrieveFirstResults(moviesList, maxResults);
	}
	public  ArrayList<Product> find(String query,String type){				
		ArrayList<Product> moviesList = retrieveProductsList(query);
		
		return moviesList;	
		
	}
	
	public  ArrayList<Product> find(String query,String type, int maxResults){
		ArrayList<Product> moviesList = retrieveProductsList(query);
	
		return retrieveFirstResults(moviesList, maxResults);
	}

	private ArrayList<Product> retrieveProductsList(String query) {
		xmlUrl = constructSearchUrl(query);
		 performSearch();
		
		return prductList2;
	}
	 public String constructSearchUrl(String query) {      // constructing the Search request- GET HTTP request according the ProductWiki API
		 StringBuffer sb = new StringBuffer();
		 sb.append(url1);
		 sb.append(URLEncoder.encode(query));
		 sb.append(url2);
		 
		 return sb.toString();
		
	}
	 
	 private void performSearch(){									// method that doing the actual search.
	    	try {
	    		
	        	List<Product> products = parseFromUrl();			// The result of parseUrl is a list of products. 
	        	 prductList2 = new ArrayList();						// creating an actual object of productList2.otherwise a Null exception will throw.
	        	for (Product product : products) {
	        		Log.d(TAG, "Product:"+product);
	        		prductList2.add(product);
	        		
				}
	        		             	        	
	        	
			} 
	        catch (Exception e) {
				Log.e(TAG, "Error while parsing", e);
			}	    	
	    	
	    }
	    
	 
	 private List<Product> parseFromUrl() throws XmlPullParserException, IOException {		// method that initiate a Pullparser and open the xml document and get the list of product results.
	    	
	    	List<Product> productList = null;
	    	
	    	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();		// obtaining a new instance of the XmlPullParserFactory class. 
	        factory.setNamespaceAware(false);										// disable the namespace awareness of the factory to improve the parsing speed.
	        XmlPullParser parser = factory.newPullParser();                        // creating a new XmlPullParser by invoking the newPullParser factory method
	       
	        
	        URL url = new URL(xmlUrl);											
	        URLConnection ucon = url.openConnection();
	        InputStream is = ucon.getInputStream();								// input stream obtained by a URL connection 
	        
	        parser.setInput(is, null);											// providing input to the parser
	        																	// not providing an input encoding - null
	        parser.nextTag();        
	        parser.nextTag();  
	        String noOfresults =  parser.nextText(); 
	        
	        if(Integer.valueOf(noOfresults)>0){                                   //checking whether there is one or more results for the search request
	        parser.nextTag();        	                                
	        productList = parseProducts(parser);                                  //if more than one results proceed with the parsing process
	        
	        }
	        else{
	        	// do nothing
	        	
	        }
			return productList;
	    	
	    }
	 
	    /* 
	     * going to use a “recursion-like” approach. The “products” element contains a number of “product” elements where a “product” element contains a number of “image” elements. 
	     * So drill-down from the parent elements to the child ones using a dedicated method for the parsing of each element. 
	     * From one method to an other, we pass the XmlPullParser instance as argument since there is a unique parser implementing the parsing. 
	     * The result of each method is an instance of the model class and finally a list of products. 
	     * In order to check the name of the current element, we use the getName method and in order to retrieve the enclosed text we use the nextText method. 
	     
	     * */
	 
	 
	    private List<Product> parseProducts(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	    	List<Product> productsList = new LinkedList<Product>();		// list of the results products.
	    	
	    	

	        while (parser.nextTag() == XmlPullParser.START_TAG) {
	        	
	            Product product = parseProduct(parser);				//  The result of parseProduct is an instance of the Product class 
	            	
	            productsList.add(product);
	        }
	        
	        return productsList;
	        
	    }
	    
	    private Product parseProduct(XmlPullParser parser) throws XmlPullParserException, IOException {		
	    	
	    	Product product = new Product();
	    	
	        
			while (parser.nextTag() == XmlPullParser.START_TAG) {        // parser.nextText() method is used in order to allow the parser to move and fetch the next tag
																		
				if (parser.getName().equals("id")) {
					product.id = parser.nextText();
				} 
				else if (parser.getName().equals("url")) {
					product.url = parser.nextText();
				}
				else if (parser.getName().equals("title")) {
					product.title = parser.nextText();
				}
				else if (parser.getName().equals("proscore")) {
					product.proscore = parser.nextText();
				}
				else if (parser.getName().equals("category")) {
					product.category = parser.nextText();
				}
				else if (parser.getName().equals("images")) {			
				                                                        //   parsing the parser to the parseImage method to get objects of Image class 
					product.imagesList=parseImage(parser); 				//   The result of parseImage is an List of Images 
																		
					
					                
					
					
						            }
				else if (parser.getName().equals("number_of_reviews")) {
					product.number_of_reviews = parser.nextText();
					
				}
				else {
					parser.nextText();
				}
				
			}
	    	
	        return product;
	        
	    }
	    
	    private ArrayList<Image> parseImage(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	    	
	       
	        ArrayList<Image> imageList2=new ArrayList<Image>();				// list of Image objects
	        
			while (parser.nextTag() == XmlPullParser.START_TAG) {
									
				
				 			
				parser.next();													// skipping unnecessary elements  
				parser.nextText();
				parser.next();
				
								// checking whether image type is raw
				 if (parser.getName().equals("rawimage")) {
					 Image image = new Image();	
					 image.type = "rawimage";								// defining image type as raw
						image.url = parser.nextText();
						imageList2.add(image);								// adding image to the image list
						parser.next();
						
			} if (parser.getName().equals("largeimage")) {
				
				Image image = new Image();	
				image.type = "largeimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();
				
				
			}  if (parser.getName().equals("mediumimage")) {
				Image image = new Image();	
				image.type = "mediumimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();
				
			} if (parser.getName().equals("smallimage")) {
				
				Image image = new Image();	image.type = "smallimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();
				
				
			}
				
			}
			
	        return imageList2;
	        
	    }
	     
	

}
