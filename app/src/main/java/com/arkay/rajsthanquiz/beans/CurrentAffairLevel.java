package com.arkay.rajsthanquiz.beans;

import java.util.ArrayList;

public class CurrentAffairLevel {

	public int currentAffairLevelID;
	public String currentAfairStr;
	public boolean is_ca_level_play = false;
	private int currentAffairLevelScore = 0;

	private ArrayList<CurrentAffairQuestion> question = new ArrayList<CurrentAffairQuestion>();

	public CurrentAffairLevel() {
		currentAffairLevelID =1;
		currentAfairStr="Current Affiar";
		question = new ArrayList<CurrentAffairQuestion>();
		is_ca_level_play = false;
	}

	public CurrentAffairLevel(int currentAffairLevelID, String currentAfairStr, boolean is_ca_level_play, int currentAffairLevelScore) {
		this.currentAffairLevelID = currentAffairLevelID;
		this.currentAfairStr = currentAfairStr;
		this.is_ca_level_play  = is_ca_level_play;
		this.currentAffairLevelScore = currentAffairLevelScore;
	}


	public void addQuestion(CurrentAffairQuestion question) {
		this.question.add(question);
	}

	public int getCurrentAffairLevelID() {
		return currentAffairLevelID;
	}

	public void setCurrentAffairLevelID(int currentAffairLevelID) {
		this.currentAffairLevelID = currentAffairLevelID;
	}

	public String getCurrentAfairStr() {
		return currentAfairStr;
	}

	public void setCurrentAfairStr(String currentAfairStr) {
		this.currentAfairStr = currentAfairStr;
	}

	public void setQuestion(ArrayList<CurrentAffairQuestion> question) {
		this.question = question;
	}

	public ArrayList<CurrentAffairQuestion> getQuestion() {
		return question;
	}

	public boolean is_ca_level_play() {
		return is_ca_level_play;
	}

	public void setIs_ca_level_play(boolean is_ca_level_play) {
		this.is_ca_level_play = is_ca_level_play;
	}

	public int getCurrentAffairLevelScore() {
		return currentAffairLevelScore;
	}

	public void setCurrentAffairLevelScore(int currentAffairLevelScore) {
		this.currentAffairLevelScore = currentAffairLevelScore;
	}
}
