package com.arkay.rajasthanquiz.beans;

import android.content.SharedPreferences;


import com.arkay.rajasthanquiz.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameData {
	
	 // serialization format version
    private static final String SERIAL_VERSION = "1.1";
    
	private int totalScore;
	private int levelCompleted;
	private int countHowManyTimePlay;
	private int countHowManyQuestionCompleted;
	private int countHowManyRightAnswareQuestion;
	
	private int currentAffairTotalScore;
	private int currentAffairLevelCompleted=1;

	private ArrayList<LetLeranScore> letLeranScores = new ArrayList<LetLeranScore>();

	//private String allCateglroyScore;
	
    
	public GameData(SharedPreferences settings){
		levelCompleted = settings.getInt(MainActivity.LEVEL_COMPLETED, 1);
		totalScore =  settings.getInt(MainActivity.TOTAL_SCORE,0);
		countHowManyTimePlay = settings.getInt(MainActivity.HOW_MANY_TIMES_PLAY_QUIZ,0);
		countHowManyQuestionCompleted = settings.getInt(MainActivity.COUNT_QUESTION_COMPLETED,0);
		countHowManyRightAnswareQuestion = settings.getInt(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS,0);
		
		//currentafair
		currentAffairTotalScore = settings.getInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE,0);
		currentAffairLevelCompleted = settings.getInt(MainActivity.CURRENT_AFAIR_LEVEL_COMPLETED,0);

		letLeranScores = getLetLeranFromStr(settings.getString(MainActivity.LETS_LEARN_SCORE,""));

	}

	 /** Constructs a SaveGame object from serialized data. */
    public GameData(byte[] data) {
        if (data == null) return; // default progress
        loadFromString(new String(data));
    }
    
    
	public GameData() {
		// TODO Auto-generated constructor stub
	}

    
	private void loadFromString(String json) {
		 if (json == null || json.trim().equals("")) return;
		 
		 try {
	            JSONObject obj = new JSONObject(json);
	            String format = obj.getString("version");
	            if (!format.equals(SERIAL_VERSION)) {
	                throw new RuntimeException("Unexpected loot format " + format);
	            }
	            JSONObject gameData = obj.getJSONObject("score");
	            this.setTotalScore(gameData.getInt(MainActivity.TOTAL_SCORE));
	            this.setLevelCompleted(gameData.getInt(MainActivity.LEVEL_COMPLETED));
	            this.setCountHowManyTimePlay(gameData.getInt(MainActivity.HOW_MANY_TIMES_PLAY_QUIZ));
	            this.setCountHowManyQuestionCompleted(gameData.getInt(MainActivity.COUNT_QUESTION_COMPLETED));
	            this.setCountHowManyRightAnswareQuestion(gameData.getInt(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS));
	            
	            this.setCurrentAffairTotalScore(gameData.getInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE));
	            this.setCurrentAffairLevelCompleted(gameData.getInt(MainActivity.CURRENT_AFAIR_LEVEL_COMPLETED));

			 	this.setLetLeranScores(getLetLeranFromStr(gameData.getString(MainActivity.LETS_LEARN_SCORE)));

	        }
	        catch (JSONException ex) {
	        	this.setCurrentAffairTotalScore(0);
	        	this.setCurrentAffairLevelCompleted(0);
	            ex.printStackTrace();
	        }
	        catch (NumberFormatException ex) {
	            ex.printStackTrace();
	        }
		
	}

	 /** Serializes this SaveGame to an array of bytes. */
    public byte[] toBytes() {
        return toString().getBytes();
    }
    
	@Override
	public String toString() {
		  try {
	            JSONObject gameData = new JSONObject();

	            gameData.put(MainActivity.TOTAL_SCORE, getTotalScore());
	            gameData.put(MainActivity.LEVEL_COMPLETED, getLevelCompleted());
	            gameData.put(MainActivity.HOW_MANY_TIMES_PLAY_QUIZ, getCountHowManyTimePlay());
	            gameData.put(MainActivity.COUNT_QUESTION_COMPLETED, getCountHowManyQuestionCompleted());
	            gameData.put(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS, getCountHowManyRightAnswareQuestion());
	            gameData.put(MainActivity.CURRENT_AFAIR_TOTAL_SCORE, getCurrentAffairTotalScore());
	            gameData.put(MainActivity.CURRENT_AFAIR_LEVEL_COMPLETED, getCurrentAffairLevelCompleted());

			  	gameData.put(MainActivity.LETS_LEARN_SCORE, getStringFromListToStr());
	            JSONObject obj = new JSONObject();
	            obj.put("version", SERIAL_VERSION);
	            obj.put("score", gameData);
	            return obj.toString();
	        }
	        catch (JSONException ex) {
	            ex.printStackTrace();
	            throw new RuntimeException("Error converting save data to JSON.", ex);
	        }
	}

    
	public void saveDataLocal(SharedPreferences settings){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(MainActivity.LEVEL_COMPLETED,getLevelCompleted());
		editor.putInt(MainActivity.TOTAL_SCORE, getTotalScore());
		editor.putInt(MainActivity.HOW_MANY_TIMES_PLAY_QUIZ, getCountHowManyTimePlay());
		editor.putInt(MainActivity.COUNT_QUESTION_COMPLETED, getCountHowManyQuestionCompleted());
		editor.putInt(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS, getCountHowManyRightAnswareQuestion());
		
		editor.putInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE, getCurrentAffairTotalScore());
		editor.putInt(MainActivity.CURRENT_AFAIR_LEVEL_COMPLETED, getCurrentAffairLevelCompleted());

		editor.putString(MainActivity.LETS_LEARN_SCORE, getStringFromListToStr());
		editor.commit();
	}
	
	/**
     * Computes the union of this SaveGame with the given SaveGame. The union will have any
     * levels present in either operand. If the same level is present in both operands,
     * then the number of stars will be the greatest of the two.
     *
     * @param other The other operand with which to compute the union.
     * @return The result of the union.
     */
    public GameData unionWith(GameData other) {
    	GameData result = clone();
    	int existingPoint = result.getCountHowManyQuestionCompleted();
        int newPoint = other.getCountHowManyQuestionCompleted();
        if (newPoint > existingPoint) {
        	result.setCountHowManyQuestionCompleted(newPoint);
        }
        existingPoint = result.getCountHowManyRightAnswareQuestion();
        newPoint = other.getCountHowManyRightAnswareQuestion();
        if (newPoint > existingPoint) {
        	result.setCountHowManyRightAnswareQuestion(newPoint);
        }

        existingPoint = result.getCountHowManyTimePlay();
        newPoint = other.getCountHowManyTimePlay();
        if (newPoint > existingPoint) {
        	result.setCountHowManyTimePlay(newPoint);
        }

        existingPoint = result.getLevelCompleted();
        newPoint = other.getLevelCompleted();
        if (newPoint > existingPoint) {
        	result.setLevelCompleted(newPoint);
        }

        existingPoint = result.getTotalScore();
        newPoint = other.getTotalScore();
        if (newPoint > existingPoint) {
        	result.setTotalScore(newPoint);
        }
        
        existingPoint = result.getCurrentAffairTotalScore();
        newPoint = other.getCurrentAffairTotalScore();
        if (newPoint > existingPoint) {
        	result.setCurrentAffairTotalScore(newPoint);
        }
        
        existingPoint = result.getCurrentAffairLevelCompleted();
        newPoint = other.getCurrentAffairLevelCompleted();
        if (newPoint > existingPoint) {
        	result.setCurrentAffairLevelCompleted(newPoint);
        }

		result.setLetLeranScores(letLeranScores);


		return result;
    }
    
    /** Returns a clone of this SaveGame object. */
    public GameData clone() {
    	GameData result = new GameData();
    	result.setCountHowManyQuestionCompleted(countHowManyQuestionCompleted);
    	result.setCountHowManyRightAnswareQuestion(countHowManyRightAnswareQuestion);
    	result.setCountHowManyTimePlay(countHowManyTimePlay);
    	result.setLevelCompleted(levelCompleted);
    	result.setTotalScore(totalScore);
    	
    	result.setCurrentAffairTotalScore(currentAffairTotalScore);
    	result.setCurrentAffairLevelCompleted(currentAffairLevelCompleted);

		result.setLetLeranScores(letLeranScores);

    	return result;
    }
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getLevelCompleted() {
		return levelCompleted;
	}
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
	}
	public int getCountHowManyTimePlay() {
		return countHowManyTimePlay;
	}
	public void setCountHowManyTimePlay(int countHowManyTimePlay) {
		this.countHowManyTimePlay = countHowManyTimePlay;
	}
	public int getCountHowManyQuestionCompleted() {
		return countHowManyQuestionCompleted;
	}
	public void setCountHowManyQuestionCompleted(int countHowManyQuestionCompleted) {
		this.countHowManyQuestionCompleted = countHowManyQuestionCompleted;
	}
	public int getCountHowManyRightAnswareQuestion() {
		return countHowManyRightAnswareQuestion;
	}
	public void setCountHowManyRightAnswareQuestion(
			int countHowManyRightAnswareQuestion) {
		this.countHowManyRightAnswareQuestion = countHowManyRightAnswareQuestion;
	}

	public int getCurrentAffairTotalScore() {
		return currentAffairTotalScore;
	}

	public void setCurrentAffairTotalScore(int currentAffairTotalScore) {
		this.currentAffairTotalScore = currentAffairTotalScore;
	}

	public int getCurrentAffairLevelCompleted() {
		return currentAffairLevelCompleted;
	}

	public void setCurrentAffairLevelCompleted(int currentAffairLevelCompleted) {
		this.currentAffairLevelCompleted = currentAffairLevelCompleted;
	}

	public ArrayList<LetLeranScore> getLetLeranScores() {
		return letLeranScores;
	}

	public LetLeranScore getLetLearnScoreByLevelID(int lelvelID){
		LetLeranScore letLearnScore = new LetLeranScore(lelvelID);
		for(LetLeranScore tempScore : letLeranScores){
			if(tempScore.getLelveID()==lelvelID){
				letLearnScore = tempScore;
			}
		}
		return letLearnScore;
	}
	public void addLetLearnScore(LetLeranScore letLearnScore){
		boolean isScoreExit = false;
		int exitIndex = 0;
		for(int i=0;i<letLeranScores.size();i++){
			if(letLeranScores.get(i).getLelveID()==letLearnScore.getLelveID()){
				isScoreExit = true;
				exitIndex = i;
			}
		}
		if(isScoreExit) {
			//exitIndex
			this.letLeranScores.set(exitIndex, letLearnScore);
		}else{
			this.letLeranScores.add(letLearnScore);
		}
	}
	public void removeLetLearnScore(){
		this.letLeranScores.clear();
	}
	public void setLetLeranScores(ArrayList<LetLeranScore> letLeranScores) {
		this.letLeranScores = letLeranScores;
	}



	public ArrayList<LetLeranScore> getLetLeranFromStr(String json) {
		ArrayList<LetLeranScore> letLeranScores = new ArrayList<LetLeranScore>();
		if (json == null || json.trim().equals(""))
			return letLeranScores ;

		try {
			//Log.i("JSON: ",json);
			JSONObject obj = new JSONObject(json);
			JSONObject mainData = obj.getJSONObject(MainActivity.LET_LEARN_SCORE);
			JSONArray allLetsLearnScore = mainData.getJSONArray(MainActivity.ALL_LET_LEARN_SCORE);
			for(int i=0;i<allLetsLearnScore.length();i++){
				JSONObject rec = allLetsLearnScore.getJSONObject(i);
				LetLeranScore temp = new LetLeranScore(rec.getInt(MainActivity.LEVEL_ID),rec.getInt(MainActivity.LEVEL_SCORE),rec.getBoolean(MainActivity.IS_LEVEL_PLAY));
				letLeranScores.add(temp);
			}
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return  letLeranScores;
	}
	public String getStringFromListToStr() {
		int jsonArrayIndex = 0;

		try {
			JSONObject allLetLearnData = new JSONObject();
			JSONObject obj = new JSONObject();
			JSONArray letsLearnScoreArray = new JSONArray();

			for(LetLeranScore tempLetsLearn : letLeranScores) {
				letsLearnScoreArray.put(jsonArrayIndex, tempLetsLearn.getToStringJSON());
				jsonArrayIndex++;
			}
			allLetLearnData.put(MainActivity.ALL_LET_LEARN_SCORE,  letsLearnScoreArray);

			obj.put(MainActivity.LET_LEARN_SCORE, allLetLearnData);
			return obj.toString();
		} catch (JSONException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error converting save data to JSON.", ex);
		}
	}
}
