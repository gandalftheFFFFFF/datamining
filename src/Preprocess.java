import java.util.ArrayList;


public class Preprocess {
	
	public static String[] getID3gamesOrSDT(String[][] data) {
		
		/*
		 * This method is used to convert the cleaned column of the study orientation of students to be used for ID3.
		 * This involves simplifying the study orientation. For the sake of ID3, I've chosen to convert "other" student
		 * to software students.
		 */
		
		String[] converted = cleanAndGetGamesOrSDT(data);
		
		String gamesOnlyStudents = "Games-A|Games-T";
		String softwareOnlyStudents = "SDT-SE|SDT-DT|SWU";
		
		for (int i = 0; i < converted.length; i++) {
			if (converted[i].matches(gamesOnlyStudents)) {
				converted[i] = "games";
			} else if (converted[i].matches(softwareOnlyStudents)) {
				converted[i] = "software";
			} else {
				converted[i] = "software";
			}
		}
		return converted;
	}
	
	public static String[] getID3reasons(String[][] data) {
		
		ArrayList<String>[] reasons = cleanAndGetReasonsForTakingCourse(data);
		
		String[] converted = new String[reasons.length];
		
		for (int i = 0; i < reasons.length; i++) {
			converted[i] = "other reason";
			for (String s: reasons[i]) {
				if (s.equals("this was a mandatory course for me")) {
					converted[i] = "mandatory";
				}
			}
		}
		return converted;
	}
	
	public static String[] getID3hoursPlayed(String[][] data) {
		String[] hours = cleanAndGetGameHoursPlayed(data);
		
		String[] converted = new String[hours.length];
		
		for (int i = 0; i < converted.length; i++) {
			if (hours[i].equals("Never") || hours[i].equals("< 5 hours a week")) {
				converted[i] = "< 5 hours a week";
			} else {
				converted[i] = ">= 5 hours a week";
			}
		}
		return converted;
	}
	
	public static String[] getID3GamesPlayed(String[][] data) {
		
		ArrayList[] gamesPlayed = Preprocess.cleanAndGetGamesPlayed(data);
		
		String[] convertedGames = new String[gamesPlayed.length];
		
		for (int i = 0; i < gamesPlayed.length; i++) {
			int size = gamesPlayed[i].size();
			if (size <= 3) {
				convertedGames[i] = "<= 2";
			} else if (size <= 6) {
				convertedGames[i] = "3-6";
			} else {
				convertedGames[i] = ">= 7";
			}
		}
		return convertedGames;
		
	}
	
	public static String[] getID3Languages(String[][] data) {
		
		ArrayList[] languages = Preprocess.cleanAndGetLanguages(data);
		String[] convertedLanguages = new String[languages.length];
		
		for (int i = 0; i < languages.length; i++) {
			int size = languages[i].size();
			if (size <= 2) {
				convertedLanguages[i] = "<= 2";
			} else if (size <= 4) {
				convertedLanguages[i] = "3-4";
			} else {
				convertedLanguages[i] = ">= 5";
			}
		}
		return convertedLanguages;
	}
	
	public static String[] cleanAndGetGamesOrSDT(String[][] data) {
		
		int column = findColumnByString(data, "What degree are you studying?");
		
		String legalStrings = "SDT-SE|SDT-DT|SWU|Games-T|Games-A";
		
		String[] gamesOrSDT = new String[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			String s = data[i][column];
			if (s.matches(legalStrings)) {
				gamesOrSDT[i-1] = s;
			} else {
				gamesOrSDT[i-1] = "Other";
			}
		}	
		
		return gamesOrSDT;
		
	}
	
	public static ArrayList[] cleanAndGetReasonsForTakingCourse(String[][] data) {

		int column = findColumnByString(data, "Why are you taking this course?");
		
		String legalStrings = "[I am interested in the subject|It may help me to find a job|The other optional courses were least appealing|This was a mandatory course for me]+";
		
		ArrayList[] reasons = new ArrayList[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			String games[] = data[i][column].split(",");
			ArrayList<String> tmpList = new ArrayList<String>();
			for (String s : games) {
				s = s.replaceAll("\"", "");
				s = s.trim();
				if (s.matches(legalStrings)) {
					tmpList.add(s.toLowerCase());
				} else {
					tmpList.add("Other");
				}
			}
			reasons[i-1] = tmpList;
		}
		
		return reasons;
	}
	
	public static String[] cleanAndGetGameHoursPlayed(String[][] data) {
		
		int column = findColumnByString(data, "How often do you play video games?");
		
		String[] hoursPlayed = new String[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			hoursPlayed[i-1] = data[i][column].trim();
		}	
		
		return hoursPlayed;
	}
	
	public static ArrayList[] cleanAndGetGamesPlayed(String[][] data) {
		
		int column = findColumnByString(data, "Which of these games have you played?");
		
		ArrayList[] gamesPlayed = new ArrayList[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			String games[] = data[i][column].split(",");
			ArrayList<String> tmpList = new ArrayList<String>();
			for (String s : games) {
				s = s.replaceAll("\"", "");
				tmpList.add(s.trim().toLowerCase());
			}
			gamesPlayed[i-1] = tmpList;
		}
		
		return gamesPlayed;
	}
	
	public static ArrayList[] cleanAndGetLanguages(String[][] data) {
		
		/*
		 * This method attempts to clean the information on peoples programming language qualifications.
		 * For every response in the survey the list of programming languages is split operator ";" or "," (since some people used semicolon and others the comma.
		 * Then every language string is trimmed for excess whitespace and.
		 * Next excess double quotation marks are removed. These seem to appear when people use the comma separator instead of the semicolon.
		 * Then I check every string if it matches any language from a list of programming languages; if it matches I add it to an ArrayList that stores that particular
		 * persons programming language skills.
		 * If the string does not match any of the languages with the .matches() method, I then try to guess what language the string might approximate to.
		 * To do this, I first filter for any single letter (or empty) string and discard these. I then use the Levenshtein Distance method to try and compensate for
		 * any spelling mistake and add the most likely program to the list. If no match is found within reasonable distance, I discard the string.
		 */

		final String legalStrings = "ASSEMBLER|actionscript|C|C\\#|C\\+|C\\+\\+|COBOL|CSS|F#|Fortran|Go|BASH|HTML|Haskell|Java|Javascript|Lua|Math Lab|Objective C|PHP|Pascal|Pearl|Python|Ruby|SAS|SML|SQL|Scala|Swift|VB\\.NET|VBA|clojure";
		final String[] legalStringsArray = {"ASSEMBLER","actionscript","C","C#","C+","C++","COBOL","CSS","F#","Fortran","Go","BASH","HTML","Haskell","Java","Javascript","Lua","Math Lab","Objective C","PHP","Pascal","Pearl","Python","Ruby","SAS","SML","SQL","Scala","Swift","VB.NET","VBA","clojure"};
		
		int column = findColumnByString(data, "Which programming languages do you know?");
		
		ArrayList[] languages = new ArrayList[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			String parts[] = data[i][column].split(";|,");
			ArrayList<String> tmpList = new ArrayList<String>();
			for (String s : parts) {
				s = s.trim();
				s = s.toLowerCase();
				s = s.replaceAll("\"", "");
				if (s.toLowerCase().matches(legalStrings.trim().toLowerCase())) {
					if (tmpList.contains(s.toLowerCase().trim())) {
						continue;
					}
					tmpList.add(s.toLowerCase());
				} else {
					if (s.length() <= 1 || s.equals(" ")) {
						s = "____NO MATCH!____";
					}
					// Use Levenshtein to chose nearest value:
					int minimum = Integer.MAX_VALUE;
					int distance = 0;
					String programToAdd = "____NO MATCH!____";
					for (String programName : legalStringsArray) {
						distance = Levenshtein.distance(s,programName);
						if (distance < minimum && distance < 3) {
							programToAdd = programName;
							minimum = distance;
						}
					}
					if (tmpList.contains(programToAdd) || programToAdd.equals("____NO MATCH!____")) {
						// program is already there (somehow!). skip it!
					} else { 
						tmpList.add(programToAdd.toLowerCase());
					}
				}
			}
			languages[i-1] = tmpList;
		}
		
		return languages;
	}
	
	public static char[] cleanAndGetRows(String[][] data) {
		
		String legalChars = "[ABCDEFGHIJLKLMNOPQRSTUVWXYZ]|[abcdefghijklmnopqrstuvwxyz]";
		
		int column = findColumnByString(data, "Which row are you sitting/did you sit in during the introduction lecture?");
		
		char[] rows = new char[data.length-1];
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalChars)) {
				rows[i-1] = (data[i][column]).toUpperCase().charAt(0);
				System.out.println(rows[i-1]);
			} else {
				rows[i-1] = '0';
			}
		}
		
		// Replace illegal values most frequent (mode) row
		char mode = MyStatistics.getMode(rows);
		
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == '0') {
				rows[i] = mode;
			}
		}
		
		return rows;
	}
	
	public static double[] cleanAndGetHeight(String[][] data) {
		
		String legalChars = "[0123456789]+";
		
		int column = findColumnByString(data, "Height");
		
		double[] heights = new double[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalChars)) {
				heights[i-1] = Double.parseDouble(data[i][column]);
				if (heights[i-1] < 100) { // If height  is below 100 centimetres, I consider it as missing/invalid and it will be sent to cleaning
					heights[i-1] = -1;
				}
			} else {
				// Instance is not Male or Female and must be replaced or removed
				heights[i-1] = -1;
			}
		}
		
		// Replace illegal values with average height
		double average = MyStatistics.getAverage(heights);
		
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] < 0) {
				heights[i] = average;
			}
		}
		
		return heights;
	}
	
	public static String[] cleanAndGetGender(String[][] data) {
		
		String legalStrings = "Male|Female";
		
		int column = findColumnByString(data, "Gender");
		
		String[] gender = new String[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalStrings)) {
				gender[i-1] = data[i][column];
			} else {
				// Instance is not Male or Female and must be replaced or removed. This should not be the case!
				gender[i-1] = "-1";
				throw new RuntimeException ("SOMETHING IS TERRIBLY WRONG!");
			}
		}
		
		
		
		return gender;
	}
	
	public static double[] cleanAndGetShoeSize(String[][] data) {
		
		/*
		 * Cleans and builds an array of shoe sizes and returns as a single dimensional array of integers
		 * The EUR shoe sizes are converted to centimeters using a formula provided from wikipedia: http://en.wikipedia.org/wiki/Shoe_size
		 * Formula used: Shoe Size (Paris Points) = 3/2 * foot length
		 */
	
		
		String legalChars = "[0123456789]+";
		int column = findColumnByString(data, "Shoe Size");
		
		double[] sizes = new double[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalChars)) {
				sizes[i-1] = ((Double.parseDouble(data[i][column])) / (3.0/2.0));
			} else {
				// String is illegal and cannot be parsed. Must be cleaned!
				sizes[i-1] = -1;
			}
		}
		
		// Calculate average and replace 0 values with average
		double average = MyStatistics.getAverage(sizes);
		
		for (int i = 0; i < sizes.length; i++) {
			if (sizes[i] <= 0) {
				sizes[i] = average;
			}
		}
		return sizes;
	}
	
	public static int[] cleanAndGetSeat(String[][] data) {
		
		String legalChars = "[0123456789]+";
		int column = findColumnByString(data, "Which seat are you sitting/did you sit on during the introduction lecture?");
		
		int[] seats = new int[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalChars)) {
				seats[i-1] = Integer.parseInt(data[i][column]);
			} else {
				// String is illegal and cannot be parsed. Must be cleaned!
				seats[i-1] = -1;
			}
		}
		
		// Replace the illegal values with the mean rounded down to an integer:
		int average = MyStatistics.getAverage(seats);
		
		for (int i = 0; i < seats.length; i++) {
			if (seats[i] < 0) {
				seats[i] = average;
			}
		}
		
		
		return seats;
	}
	
	public static int[] cleanAndGetAge(String[][] data) {
		
		String legalChars = "[0123456789]+";
		int column = findColumnByString(data, "Age");
		
		int[] ages = new int[data.length-1];
		
		for (int i = 1; i < data.length; i++) {
			if (data[i][column].matches(legalChars)) {
				ages[i-1] = Integer.parseInt(data[i][column]);
			} else {
				// String is illegal and cannot be parsed. Must be cleaned! Replacing value with -1
				ages[i-1] = -1;
			}
		}
		
		// Calculate average and replace -1 and 0 values with average
		int average = MyStatistics.getAverage(ages);
		
		for (int i = 0; i < ages.length; i++) {
			if (ages[i] <= 0) {
				ages[i] = average;
			}
		}
		
		return ages;
	}
	
	public static int findColumnByString(String[][] data, String wordToSearch) {
		
		/*
		 * Finds the index of a column in the data array that matches the string  and returns that column number as an integer
		 */
		
		int columnToReturn = 0;
		
		for (String s: data[0]) {
			if (s.toLowerCase().equals(wordToSearch.toLowerCase())) {
				break;
			}
			columnToReturn++;
		}
		
		return columnToReturn;
	}
}