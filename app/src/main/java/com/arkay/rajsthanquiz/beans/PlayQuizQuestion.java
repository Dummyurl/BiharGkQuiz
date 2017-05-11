package com.arkay.rajsthanquiz.beans;


import com.arkay.rajsthanquiz.util.Constants;

public class PlayQuizQuestion extends CurrentAffairQuestion{
	private int categoryIDInt;
	private String categoryIDStr;

//	private String question;
//	private ArrayList<String> options = new ArrayList<String>();
//	private String trueAns;
//	private int questionID=0;
//
//	private boolean isQuestionTrue;
//	private int ansIndex = 0;

	private boolean isBookmark;
	private int question_type = 1;

	//private int userLevel = Constants.TOO_EASY_1;



	public PlayQuizQuestion() {
		super();
	}

	public PlayQuizQuestion(int questionID, String question, String trueAns, boolean isBookmark, int questionType) {
		super(questionID,question,trueAns);

//		this.question = question;
//		this.trueAns = trueAns;
//		this.questionID = questionID;
		this.isBookmark = isBookmark;
		//this.userLevel = userLevel;
		this.question_type = questionType;
	}
	
	public PlayQuizQuestion(String string, boolean equals, boolean equals2,
							String string2) {
	}

//	public String getQuestion() {
//		return question;
//	}
//	public void setQuestion(String question) {
//		this.question = question;
//	}
	
//	public boolean addOption(String option){
//		return this.options.add(option);
//	}
//
//
//	public ArrayList<String> getOptions() {
//		return options;
//	}

//	public void setOptions(ArrayList<String> options) {
//		this.options = options;
//	}
//
//
//	public int getQuestionID() {
//		return questionID;
//	}

//	public void setQuestionID(int questionID) {
//		this.questionID = questionID;
//	}
//
//	public String getTrueAns() {
//		return trueAns;
//	}

//	public void setTrueAns(String trueAns) {
//		this.trueAns = trueAns;
//	}

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

//	public int getUserLevel() {
//		return userLevel;
//	}
//
//	public void setUserLevel(int userLevel) {
//		this.userLevel = userLevel;
//	}

	public int getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(int question_type) {
		this.question_type = question_type;
	}

//	public boolean isQuestionTrue() {
//		return isQuestionTrue;
//	}
//
//	public void setIsQuestionTrue(boolean isQuestionTrue) {
//		this.isQuestionTrue = isQuestionTrue;
//	}
//
//	public int getAnsIndex() {
//		return ansIndex;
//	}
//
//	public void setAnsIndex(int ansIndex) {
//		this.ansIndex = ansIndex;
//	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Question: "+ super.getQuestion() +" OptionS: "+super.getOptions();
	}
	
	
	
		
}
