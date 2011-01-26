package com.ai.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Product implements Serializable{
		
	private static final long serialVersionUID = 1L;
	public String url;
	public String id;
	public String title;
	public String proscore;
	public String description;
	public String category;
	public String number_of_reviews;
	
	public ArrayList<Image> imagesList;
	public ArrayList<String> pros;
	public ArrayList<String> cons;
	public HashMap<String,String> keyFeatures;
	
	
	/*
	 *   returns the of the image object which type is small image.                        
	 * */
	public String retrieveThumbnail() {
		if (imagesList!=null && !imagesList.isEmpty()) {
			for (Image productImage : imagesList) {
				if (productImage.type.equalsIgnoreCase("smallimage")) {
					return productImage.url;
				}
			}
		}
		return null;
	}
	/*
	 *   returns the of the image object which type is largeimage.                        
	 * */
	public String retrieveLargeImage() {
		if (imagesList!=null && !imagesList.isEmpty()) {
			for (Image productImage : imagesList) {
				if (productImage.type.equalsIgnoreCase("largeimage")) {
					return productImage.url;
				}
			}
		}
		return null;
	}
	
	/*The ArrayAdapter-ProductAdapter uses the toString method of each of the encapsulated objects.
	 * So override the method and provide a more meaningful representation.
	 * Useful when debugging.
	 * */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [name=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

}
