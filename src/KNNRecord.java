
public class KNNRecord {
	
	/*
	 * This class is used for the KNN (instead of normal Record objects) calculations and stores the normalized values
	 */
	
	double lang;
	double hours;
	double games;
	double student;
	
	public KNNRecord(double lang, double hours, double games, double student) {
		this.lang = lang;
		this.hours = hours;
		this.games = games;
		this.student = student;
	}
	
	public String toString() {
		return lang + ", "
				+ hours + ", "
				+ games + ", "
				+ student;
	}

}
