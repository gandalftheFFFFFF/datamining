import java.util.ArrayList;


public class Record {
	
	ArrayList<String> languages;
	ArrayList<String> gamesPlayed;
	String hoursPlayed;
	String student;
	int age;
	
	
	public Record(ArrayList<String> languages, ArrayList<String> gamesPlayed, String hoursPlayed, String student, int age) {
		this.languages = languages;
		this.gamesPlayed = gamesPlayed;
		this.hoursPlayed = hoursPlayed;
		this.student = student;
		this.age = age;
	}
	
	public double getLanguagesKNN() {
		if (languages.size() == 1 && languages.get(0).equalsIgnoreCase("____NO MATCH!____")) {
			return 0;
		} else {
			return languages.size();
		}
	}
	
	public int getAge() {
		return age;
	}
	
	public double getGamesKNN() {
		if (gamesPlayed.contains("i have not played any of these games")) {
			return 0;
		} else {
			return gamesPlayed.size();
		}
	}
	
	public double getHoursPlayedKNN() {
		if (hoursPlayed.equalsIgnoreCase("Never")) {
			return 0;
		} else if (hoursPlayed.equalsIgnoreCase("< 5 hours a week")) {
			return 2.5;
		} else if (hoursPlayed.equalsIgnoreCase("< 10 hours a week")) {
			return 7.5;
		} else if (hoursPlayed.equalsIgnoreCase("< 20 hours a week")) {
			return 15;
		} else if (hoursPlayed.equalsIgnoreCase("> 20 hours a week")) {
			return 20;
		}
		return 10;
	}
	
	public String getGamesOrSDT() {
		return student;
	}
	
	public String toString() {
		String s = "Record: ";
		s = s + languages.toString()
				+ ", "
				+ gamesPlayed.toString()
				+ ", "
				+ hoursPlayed;
		return s;
	}

}
