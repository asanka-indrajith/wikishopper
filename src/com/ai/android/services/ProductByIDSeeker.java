package com.ai.android.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;
import com.ai.android.model.Image;
import com.ai.android.model.Product;

/*
 *This Class is used for a Product request 
 *But can get a fully detailed information of the product.
 * 
 */

public class ProductByIDSeeker {
	
	
	
	private String url1="http://api.productwiki.com/connect/api.aspx?op=product&format=xml&idtype=productid&idvalue=";
	private String url2="&key=2358feba734043a9b2f6c52520f5feb5&fields=proscons,description,key_features";

	private static  String xmlUrl ;
	private final String TAG = getClass().getSimpleName();
	
	Product product;
	
	public Product find(String query) {                                     // perform search without limitation.
		Product product = retrieveMoviesList(query);
		
		return product;
	}
		

	private Product retrieveMoviesList(String query) {					
		xmlUrl = constructSearchUrl(query);
		 performSearch();
		
		return product;
	}
	 public String constructSearchUrl(String query) {                      // constructing the Search request- GET HTTP request according the ProductWiki API
		 StringBuffer sb = new StringBuffer();
		 sb.append(url1);
		 sb.append(URLEncoder.encode(query));
		 sb.append(url2);
		 
		 return sb.toString();
		
	}
	 
	 private void performSearch(){                                      // method that doing the actual search.
	    	try {
	    		
	        	 product = parseFromUrl();                          // The result of parseUrl is a product object.
	        	        		             	        	
	        	
			} 
	        catch (Exception e) {
				Log.e(TAG, "Error while parsing", e);
			}	    	
	    	
	    }
	    
	 
	 private Product parseFromUrl() throws XmlPullParserException, IOException {          // method that initiate a Pullparser and open the xml document and get a product result.
	    	
	    	Product product1 = null;
	    	
	    	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();           // obtaining a new instance of the XmlPullParserFactory class. 
	        factory.setNamespaceAware(false);                                            // disable the namespace awareness of the factory to improve the parsing speed.
	        XmlPullParser parser = factory.newPullParser();                              // creating a new XmlPullParser by invoking the newPullParser factory method
	       
	        
	        URL url = new URL(xmlUrl);
	        URLConnection ucon = url.openConnection();                  
	        InputStream is = ucon.getInputStream();                                      // input stream obtained by a URL connection 
	        
	        parser.setInput(is, null);                                                   // providing input to the parser.
	                                                                                     // not providing an input encoding - null   
	        parser.nextTag();                                                         
	        parser.nextTag();  
	        parser.nextText(); 
	        parser.nextTag();        
	               	                       
	                
	        product1 = parseProducts(parser);
			
			return product1;
	    	
	    }
	 
	 /* 
	     * going to use a “recursion-like” approach. The “products” element contains a number of “product” elements where a “product” element contains a number of “image” elements and  number of “image” elements. 
	     * So drill-down from the parent elements to the child ones using a dedicated method for the parsing of each element. 
	     * From one method to an other, we pass the XmlPullParser instance as argument since there is a unique parser implementing the parsing. 
	     * The result of each method is an instance of the model class and finally a list of products. 
	     * In order to check the name of the current element, we use the getName method and in order to retrieve the enclosed text we use the nextText method. 
	     
	     * */
	    
	    private Product parseProducts(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	    	
	    	
	    	
	    	Product product2=null;
	        while (parser.nextTag() == XmlPullParser.START_TAG) {
	        	
	            product2 = parseProduct(parser);                            // The result of parseProduct is an instance of the Product class 
	            
	            
	        }
	        
	        return product2;
	        
	    }
	    
	    private Product parseProduct(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	    	Product product = new Product();
	    	
	        
			while (parser.nextTag() == XmlPullParser.START_TAG) {                     // parser.nextText() method is used in order to allow the parser to move and fetch the next tag
				
				if (parser.getName().equals("id")) {
					product.id = parser.nextText();
				} 
				else if (parser.getName().equals("url")) {
					product.url = parser.nextText();
				}
				else if (parser.getName().equals("title")) {
					product.title = parser.nextText();
				}
				else if (parser.getName().equals("description")) {
					product.description = parser.nextText();
				}
				else if (parser.getName().equals("proscore")) {
					product.proscore = parser.nextText();
				}
				else if (parser.getName().equals("category")) {
					product.category = parser.nextText();
				}
				else if (parser.getName().equals("images")) {                
					                                                                       //   parsing the parser to the parseImage method to get objects of Image class 
					product.imagesList=parseImage(parser);                                 //   The result of parseImage is an List of Images 
					
					                
					
					
		        }
				else if (parser.getName().equals("number_of_reviews")) {
					product.number_of_reviews = parser.nextText();
					
					
			
				}
				else if (parser.getName().equals("proscons")) {                                 
					parseProscons(parser, product);                                    // the parseProscons is a void method where pros and cons 
					
					
				}
				else if (parser.getName().equals("key_features")) {
					
					parseKeyfeatures(parser, product);
					
				}
				else {
					parser.nextText();
				}
				
			}
	    	
	        return product;
	        
	    }
	    
	    private ArrayList<Image> parseImage(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	    	
	       
	        ArrayList<Image> imageList2=new ArrayList<Image>();                               // list of Image objects
	        
			while (parser.nextTag() == XmlPullParser.START_TAG) {
									
				 
				 			
				parser.next();                                                               // skipping unnecessary elements  
				parser.nextText();
				parser.next();
				
				 
				 if (parser.getName().equals("rawimage")) {                                  // checking whether image type is raw
					 Image image = new Image();	
					 image.type = "rawimage";                                                 // defining image type as raw
						image.url = parser.nextText();
						imageList2.add(image);                                                 // adding image to the image list    
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
				 Log.d(TAG, "Start tag " + parser.getName());
			}
			
	        return imageList2;
	        
	    }
	     
	    private void parseProscons(XmlPullParser parser,Product product) throws XmlPullParserException, IOException {
	    	
	    	
	      
	        parser.nextTag();
	        ArrayList<String> pros = new ArrayList<String>();               // declaring a list to put all pro statements of a product.
	        ArrayList<String> cons = new ArrayList<String>();                // declaring a list to put all cons statements of a product.
	        
			while (parser.nextTag() == XmlPullParser.START_TAG) {             // parsing start TAG to "statement".
				 
			       
				while (parser.nextTag() == XmlPullParser.START_TAG) {
					
					 
					 
					if (parser.getName().equals("text")) {
						String text =parser.nextText();                            // Adding each pro statement to the pros list
						pros.add(text);
						
					} 
					else if (parser.getName().equals("id")) {
						parser.nextText();
						
					}
					else if (parser.getName().equals("score")) {
						parser.nextText();
					}	
					else {
						parser.nextText();
					}
					
					
				}
			
			}
			product.pros=pros;                                                // adding pros list to the passed Product object
			
			 parser.nextTag();
			 
	while (parser.nextTag() == XmlPullParser.START_TAG) {
		
				while (parser.nextTag() == XmlPullParser.START_TAG) {
					
					                
					if (parser.getName().equals("text")) {
						String text =parser.nextText();                        // Adding each cons statement to the pros list
						cons.add(text);
						
					} 
					else if (parser.getName().equals("id")) {
						parser.nextText();
					}
					else if (parser.getName().equals("score")) {
						parser.nextText();
					}
					else {
						parser.nextText();
					}
				}
			
			}
		
		product.cons=cons;                                                     // adding cons list to the passed Product object
		parser.nextTag();
			
	    }
	    
	    private void parseKeyfeatures(XmlPullParser parser,Product product) throws XmlPullParserException, IOException {
	   	 HashMap<String,String> key =new HashMap<String, String>();
	   	
	   	 
	   	 while (parser.nextTag() == XmlPullParser.START_TAG) {
	   		
	   		 String feature="";
	   		 String value=""; 
	   		while (parser.nextTag() == XmlPullParser.START_TAG) {
	   			
	   			
	   			 
	   			if (parser.getName().equals("title")) {
	   				feature =parser.nextText();
	   				
	   				
	   				
	   			} 
	   			else if (parser.getName().equals("values")) {
	   				
	   				
	   				while (parser.nextTag() == XmlPullParser.START_TAG) {
	   					
	   					if(parser.getName().equals("value")){
	   					value=value.concat(parser.nextText());
	   					value=value.concat("  ");
	   					
	   				
	   					
	   					}
	   					else{
	   						parser.nextText();
	   					}
	   					
	   				}
	   				
	   				
	   			}
	   			
	   			else {
	   				parser.nextText();
	   			}
	   			key.put(feature, value);
	   			
	   			
	   			
	   		}
	   	
	   	}
	   	
	   	product.keyFeatures=key;
	   	
	   	 
	   	
	   }   



}
