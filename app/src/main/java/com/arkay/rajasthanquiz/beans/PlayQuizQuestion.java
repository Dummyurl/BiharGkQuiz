package com.arkay.rajasthanquiz.beans;


public class PlayQuizQuestion extends CurrentAffairQuestion{
	private int categoryIDInt;
	private String categoryIDStr;

	private boolean isBookmark;
	private int question_type = 1;



	public PlayQuizQuestion() {
		super();
	}

	public PlayQuizQuestion(int questionID, String question, String trueAns, boolean isBookmark, int questionType) {
		super(questionID,question,trueAns);

		this.isBookmark = isBookmark;
		//this.userLevel = userLevel;
		this.question_type = questionType;
	}
	

	public boolean isBookmark() {
		return isBookmark;
	}

	public void setIsBookmark(boolean isBookmark) {
		this.isBookmark = isBookmark;
	}

	public int getCategoryIDInt() {
		return categoryIDInt;
	}

	public void setCategoryIDInt(int categoryIDInt) {
		this.categoryIDInt = categoryIDInt;
	}

	public String getCategoryIDStr() {
		return categoryIDStr;
	}

	public void setCategoryIDStr(String categoryIDStr) {
		this.categoryIDStr = categoryIDStr;
	}



	public int getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(int question_type) {
		this.question_type = question_type;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Question: "+ super.getQuestion() +" OptionS: "+super.getOptions();
	}
	
	
	
		
}
