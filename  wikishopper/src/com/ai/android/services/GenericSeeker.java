package com.ai.android.services;

import java.util.ArrayList;

public abstract class GenericSeeker<E> {

	protected HttpRetriever httpRetriever = new HttpRetriever();

	public abstract ArrayList<E> find(String query);

	public abstract ArrayList<E> find(String query, String type);

	public abstract ArrayList<E> find(String query, int maxResults);

	public abstract ArrayList<E> find(String query, String type, int maxResults);

	/*
	 * the method that limits the number of results shows to user. this is
	 * implement to reduce the time taken to display results. Also lot of time
	 * last results have the maximum probability of not according to the search
	 * query.
	 */

	public ArrayList<E> retrieveFirstResults(ArrayList<E> list, int maxResults) {
		ArrayList<E> newList = new ArrayList<E>();
		int count = Math.min(list.size(), maxResults);
		for (int i = 0; i < count; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}

}
