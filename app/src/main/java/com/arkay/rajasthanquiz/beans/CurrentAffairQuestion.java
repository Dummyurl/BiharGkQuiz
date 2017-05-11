package com.arkay.rajasthanquiz.beans;

import java.util.ArrayList;

public class CurrentAffairQuestion {

	private String question="";
	private ArrayList<String> options = new ArrayList<String>();
	private String trueAns="A";
	private int queid=0;
	private String optiona,optionb,optionc,optiond,rightans;


	private boolean isQuestionTrue;
	private int ansIndex = -1;

	public CurrentAffairQuestion() {
	}

	public CurrentAffairQuestion(int questionID, String question, String trueAns ) {
		this.question = question;
		this.trueAns = trueAns;
		this.queid = questionID;
	}

	public int getQueid() {
		return queid;
	}

	public void setQueid(int queid) {
		this.queid = queid;
	}

	public String getOptiona() {
		return optiona;
	}

	public void setOptiona(String optiona) {
		this.optiona = optiona;
	}

	public String getOptionb() {
		return optionb;
	}

	public void setOptionb(String optionb) {
		this.optionb = optionb;
	}

	public String getOptionc() {
		return optionc;
	}

	public void setOptionc(String optionc) {
		this.optionc = optionc;
	}

	public String getOptiond() {
		return optiond;
	}

	public void setOptiond(String optiond) {
		this.optiond = optiond;
	}

	public String getRightans() {
		return rightans;
	}

	public void setRightans(String rightans) {
		this.rightans = rightans;
	}

	public void setQuestionTrue(boolean questionTrue) {
		isQuestionTrue = questionTrue;
	}

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean addOption(String option){
		return this.options.add(option);
	}


	public ArrayList<String> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}


	public int getQuestionID() {
		return queid;
	}

	public void setQuestionID(int questionID) {
		this.queid = questionID;
	}

	public String getTrueAns() {
		return trueAns;
	}

	public void setTrueAns(String trueAns) {
		this.trueAns = trueAns;
	}

	public boolean isQuestionTrue() {
		return isQuestionTrue;
	}

	public void setIsQuestionTrue(boolean isQuestionTrue) {
		this.isQuestionTrue = isQuestionTrue;
	}

	public int getAnsIndex() {
		return ansIndex;
	}

	public void setAnsIndex(int ansIndex) {
		this.ansIndex = ansIndex;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Question: "+ question +" OptionS: "+options;
	}


	
	
}
