package com.arkay.rajasthanquiz.beans;

public class Category {
	private int cateogoryID;
	private String category;
	private int color;
	private int iconID;
	private int totalQuestion;
	private int levelScore;

	public Category(int cateogoryID, String category) {
		this.cateogoryID = cateogoryID;
		this.category = category;
	}

	public Category(int cateogoryID, String category, int color, int iconID, int totalQuestion) {
		this.cateogoryID = cateogoryID;
		this.category = category;
		this.color = color;
		this.iconID = iconID;
		this.totalQuestion = totalQuestion;
	}

	public Category(int cateogoryID, String category, int iconID) {
		this.cateogoryID = cateogoryID;
		this.category = category;
		this.iconID = iconID;
	}

	public int getCateogoryID() {
		return cateogoryID;
	}

	public void setCateogoryID(int cateogoryID) {
		this.cateogoryID = cateogoryID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}

	public int getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}
}
