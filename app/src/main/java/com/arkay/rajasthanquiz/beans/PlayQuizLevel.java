package com.arkay.rajasthanquiz.beans;

import java.util.List;

public class PlayQuizLevel {
	
	private int levelNo;
	private int noOfQuestion;
//	private int userLevel;
	private List<PlayQuizQuestion> question;
	
	public PlayQuizLevel(int levelNo, int noOfQuestion, int userLevel) {
		super();
		this.levelNo = levelNo;
		this.noOfQuestion = noOfQuestion;
		//this.userLevel  = userLevel;
	}
	public PlayQuizLevel(int levelNo, int noOfQuestion) {
		super();
		this.levelNo = levelNo;
		this.noOfQuestion = noOfQuestion;
	}
	
	public int getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(int levelNo) {
		this.levelNo = levelNo;
	}
	public int getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(int noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}

	public List<PlayQuizQuestion> getQuestion() {
		return question;
	}

	public void setQuestion(List<PlayQuizQuestion> question) {
		this.question = question;
	}

//	public int getUserLevel() {
//		return userLevel;
//	}
//
//	public void setUserLevel(int userLevel) {
//		this.userLevel = userLevel;
//	}
}
