package com.ai.android.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.ai.android.model.Image;
import com.ai.android.model.Product;

public class ProductCompetitiorsSeeker extends GenericSeeker<Product> {
	private String url1 = "http://api.productwiki.com/connect/api.aspx?op=product&format=xml&idtype=productid&idvalue=";
	private String url2 = "&key=2358feba734043a9b2f6c52520f5feb5&fields=competitors";

	private static String xmlUrl;
	private final String TAG = getClass().getSimpleName();

	ArrayList<Product> prductList2;

	@Override
	public ArrayList<Product> find(String query, String type) { // perform
																// search
																// without
																// limitation.
		ArrayList<Product> personList = retrieveProductsList(query, type);
		return personList;
	}

	@Override
	public ArrayList<Product> find(String query, String type, int maxResults) { // limiting
																				// maximum
																				// numbers
																				// of
																				// results
																				// displaying
																				// to
																				// the
																				// user
		ArrayList<Product> personList = retrieveProductsList(query, type);
		return retrieveFirstResults(personList, maxResults);
	}

	@Override
	public ArrayList<Product> find(String query) {
		String type = null;
		ArrayList<Product> personList = retrieveProductsList(query, type);
		return personList;
	}

	@Override
	public ArrayList<Product> find(String query, int maxResults) {
		String type = null;
		ArrayList<Product> personList = retrieveProductsList(query, type);
		return retrieveFirstResults(personList, maxResults);
	}

	private ArrayList<Product> retrieveProductsList(String query, String type) {
		xmlUrl = constructSearchUrl(query, type);
		Log.d(TAG, "Url:" + xmlUrl);
		performSearch();

		return prductList2;
	}

	public String constructSearchUrl(String query, String type) { // constructing
																	// the
																	// Search
																	// request-
																	// GET HTTP
																	// request
																	// according
																	// the
																	// ProductWiki
																	// API
		StringBuffer sb = new StringBuffer();
		sb.append(url1);
		sb.append(URLEncoder.encode(query));
		sb.append(url2);

		return sb.toString();

	}

	private void performSearch() { // method that doing the actual search.
		try {

			List<Product> products = parseFromUrl(); // The result of parseUrl
														// is a list of
														// products.
			prductList2 = new ArrayList(); // creating an actual object of
											// productList2.otherwise a Null
											// exception will throw.
			for (Product product : products) {
				Log.d(TAG, "Product:" + product);
				prductList2.add(product);

			}

		} catch (Exception e) {
			Log.e(TAG, "Error while parsing", e);
		}

	}

	private List<Product> parseFromUrl() throws XmlPullParserException,
			IOException { // method that initiate a Pullparser and open the xml
							// document and get the list of product results.

		List<Product> productList = null;

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // obtaining
																			// a
																			// new
																			// instance
																			// of
																			// the
																			// XmlPullParserFactory
																			// class.
		factory.setNamespaceAware(false); // disable the namespace awareness of
											// the factory to improve the
											// parsing speed.
		XmlPullParser parser = factory.newPullParser(); // creating a new
														// XmlPullParser by
														// invoking the
														// newPullParser factory
														// method

		URL url = new URL(xmlUrl);
		URLConnection ucon = url.openConnection();
		InputStream is = ucon.getInputStream(); // input stream obtained by a
												// URL connection.

		parser.setInput(is, null); // providing input to the parser.
									// not providing an input encoding - null

		parser.nextTag();
		parser.nextTag();
		String noOfresults = parser.nextText();

		if (Integer.valueOf(noOfresults) > 0) { // checking whether there is one
												// or more results for the
												// search request
			parser.nextTag();
			parser.nextTag();
			parser.nextTag();

			parser.nextText();

			parser.nextTag();

			parser.nextText();
			parser.nextTag();

			parser.nextText();

			parser.nextTag();
			parser.nextText();
			parser.nextTag();

			parser.nextText();
			parser.nextTag();

			parser.nextText();

			parser.nextTag();
			parser.nextTag();
			parser.nextTag();
			parser.nextTag();
			parser.nextTag();

			parser.nextText();

			parser.nextTag();

			parser.nextText();
			parser.nextTag();

			parser.nextText();
			parser.nextTag();

			parser.nextText();
			parser.nextTag();

			parser.nextText();
			parser.nextTag();
			parser.nextTag();
			parser.nextTag();
			productList = parseProducts(parser); // if more than one results
													// proceed with the parsing
													// process

		} else {
			// do nothing

		}
		return productList;

	}

	/*
	 * going to use a “recursion-like” approach. The “products” element contains
	 * a number of “product” elements where a “product” element contains a
	 * number of “image” elements. So drill-down from the parent elements to the
	 * child ones using a dedicated method for the parsing of each element. From
	 * one method to an other, we pass the XmlPullParser instance as argument
	 * since there is a unique parser implementing the parsing. The result of
	 * each method is an instance of the model class and finally a list of
	 * products. In order to check the name of the current element, we use the
	 * getName method and in order to retrieve the enclosed text we use the
	 * nextText method.
	 */

	private List<Product> parseProducts(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		List<Product> productsList = new LinkedList<Product>(); // list of the
																// results
																// products.

		while (parser.nextTag() == XmlPullParser.START_TAG) {

			while (parser.nextTag() == XmlPullParser.START_TAG) {
				Log.d(TAG, parser.getName());
				if (parser.getName().equals("product")) {

					Product product = parseProduct(parser); // The result of
															// parseProduct is
															// an instance of
															// the Product class

					productsList.add(product);
				} else {
					parser.nextText();
				}
			}
		}
		return productsList;

	}

	private Product parseProduct(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		Product product = new Product();

		while (parser.nextTag() == XmlPullParser.START_TAG) { // parser.nextText()
																// method is
																// used in order
																// to allow the
																// parser to
																// move and
																// fetch the
																// next tag

			if (parser.getName().equals("id")) {
				product.id = parser.nextText();
			} else if (parser.getName().equals("url")) {
				product.url = parser.nextText();
			} else if (parser.getName().equals("title")) {
				product.title = parser.nextText();
			} else if (parser.getName().equals("proscore")) {
				product.proscore = parser.nextText();
			} else if (parser.getName().equals("category")) {
				product.category = parser.nextText();
			} else if (parser.getName().equals("description")) {
				product.description = parser.nextText();
			} else if (parser.getName().equals("images")) {
				// parsing the parser to the parseImage method to get objects of
				// Image class
				product.imagesList = parseImage(parser); // The result of
															// parseImage is an
															// List of Images

			} else if (parser.getName().equals("number_of_reviews")) {
				product.number_of_reviews = parser.nextText();

			} else if (parser.getName().equals("proscons")) {

				parseProscons(parser, product);

			} else if (parser.getName().equals("key_features")) {

				parseKeyfeatures(parser, product);

			} else {
				parser.nextText();
			}

		}

		return product;

	}

	private ArrayList<Image> parseImage(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		ArrayList<Image> imageList2 = new ArrayList<Image>(); // list of Image
																// objects.

		while (parser.nextTag() == XmlPullParser.START_TAG) {

			parser.next(); // skipping unnecessary elements.
			parser.nextText();
			parser.next();

			// checking whether image type is raw.
			if (parser.getName().equals("rawimage")) {
				Image image = new Image();
				image.type = "rawimage"; // defining image type as raw.
				image.url = parser.nextText();
				imageList2.add(image); // adding image to the image list.
				parser.next();

			}
			if (parser.getName().equals("largeimage")) {

				Image image = new Image();
				image.type = "largeimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();

			}
			if (parser.getName().equals("mediumimage")) {
				Image image = new Image();
				image.type = "mediumimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();

			}
			if (parser.getName().equals("smallimage")) {

				Image image = new Image();
				image.type = "smallimage";
				image.url = parser.nextText();
				imageList2.add(image);
				parser.next();

			}

		}

		return imageList2;

	}

	private void parseProscons(XmlPullParser parser, Product product)
			throws XmlPullParserException, IOException {

		parser.nextTag();
		ArrayList<String> pros = new ArrayList<String>();
		ArrayList<String> cons = new ArrayList<String>();

		while (parser.nextTag() == XmlPullParser.START_TAG) {

			while (parser.nextTag() == XmlPullParser.START_TAG) {

				if (parser.getName().equals("text")) {
					String text = parser.nextText();
					pros.add(text);

				} else if (parser.getName().equals("id")) {
					parser.nextText();

				} else if (parser.getName().equals("score")) {
					parser.nextText();
				} else {
					parser.nextText();
				}

			}

		}
		product.pros = pros;

		parser.nextTag();

		while (parser.nextTag() == XmlPullParser.START_TAG) {

			while (parser.nextTag() == XmlPullParser.START_TAG) {

				if (parser.getName().equals("text")) {
					String text = parser.nextText();
					cons.add(text);

				} else if (parser.getName().equals("id")) {
					parser.nextText();
				} else if (parser.getName().equals("score")) {
					parser.nextText();
				} else {
					parser.nextText();
				}
			}

		}

		product.cons = cons;
		parser.nextTag();

	}

	private void parseKeyfeatures(XmlPullParser parser, Product product)
			throws XmlPullParserException, IOException {
		HashMap<String, String> key = new HashMap<String, String>();

		while (parser.nextTag() == XmlPullParser.START_TAG) {

			String feature = "";
			String value = "";
			while (parser.nextTag() == XmlPullParser.START_TAG) {

				if (parser.getName().equals("title")) {
					feature = parser.nextText();

				} else if (parser.getName().equals("values")) {

					while (parser.nextTag() == XmlPullParser.START_TAG) {

						if (parser.getName().equals("value")) {
							value = value.concat(parser.nextText());
							value = value.concat("  ");

						} else {
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

		product.keyFeatures = key;

	}

}
