package com.ai.android.database;

/*
 * This class used to module category preferences in the Category Details activity.
 * */
public class CategoryAction {

	private String label;

	private String data;

	private int type;

	public static final int ACTION_PREF1 = 1;
	public static final int ACTION_PREF2 = 2;
	public static final int ACTION_PREF3 = 3;

	public CategoryAction(String label, String data, int type) {
		super();
		this.label = label;
		this.data = data;
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
