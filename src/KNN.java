import java.util.HashMap;

public class KNN {
	
	public static void mainIsh(Record[] everyone) {
		
		double sum = 0;
		
		/*
		 * The following following outer loop is used for statistics regarding predictions and their correctness.
		 * 
		 * Any if statement involving h == 0 and/or i == 0 has is used for printing the sample prediction.
		 */
		int itrs = 1000;
		for (int h = 0; h < itrs; h++) {
			
			// Set k
			int k = 3;
			
			// Normalize values and divide into train and test set
			KNNRecord[] normalizedEveryone = getNormalizedEveryone(everyone);
			KNNRecord[][] dividedSet = getTrainingSet(normalizedEveryone);
			KNNRecord[] normalizedTrain = dividedSet[0];
			KNNRecord[] normalizedTest = dividedSet[1];
			
			double trueCount = 0; // Used for statistics
			
			if (h == 0) {
				System.out.println("Sample prediction (attempt to predict the last value [0.0 means games student, 1.0 means software student]):");
			}
			
			
			 // This loop will loop through the entire test set and try to predict the records based on majority vote regarding their k nearest neighbors.
			for (int i = 0; i < normalizedTest.length; i++) {
				KNNRecord[] nearestNeighbors = getNearestNeighbors(normalizedTest[i], normalizedTrain, k);
				
				if (h == 0 && i == 0) {
					System.out.println("Record to test:    " + normalizedTest[i]);
					for (int g = 0; g < nearestNeighbors.length; g++) {
						System.out.println("Near neighbor (" + (g+1) + "): " + nearestNeighbors[g].toString());
					}
				}
				
				int gameCount = 0;
				for (int j = 0; j < k; j++) {
					if (nearestNeighbors[j].student == 0) {
						gameCount++;
					}
				}
				if (gameCount >= (int)((k/2)+1)) {
					if (normalizedTest[i].student == 0) {
						trueCount++;
					}
					if (h == 0 && i == 0) {
						System.out.println("Predicted: Games student.");
						if (normalizedTest[i].student == 0) {
							System.out.println("Prediction was true!");
						} else {
							System.out.println("Prediction was false...");
						}
					}
				} else {
					if (normalizedTest[i].student == 1) {
						trueCount++;
					}
					if (h == 0 && i == 0) {
						System.out.println("Predicted: Software student.");
						if (normalizedTest[i].student == 1) {
							System.out.println("Prediction was true!!");
						} else {
							System.out.println("Prediction was false...");
						}
					}
				}
				
			}
			sum += trueCount;
		}
		System.out.println();
		System.out.println("KNN statistics:");
		System.out.println("Total records correctly predicted: " + sum);
		System.out.println("Total records tested: " + (48*itrs));
		System.out.println("Average correct predictions percentage over " + itrs + " iterations: " + (sum/(double)(48*itrs)));
	}
	
	public static KNNRecord[] getNearestNeighbors(KNNRecord testRecord, KNNRecord[] train, int k) {
		
		/*
		 * This method calculates the nearest neighbors of an object
		 */
		
		
		// Compare one record to the train set and compute the closest k neighbours
		double[] distance = new double[train.length];
		
		for (int i = 0; i < train.length; i++) {
			distance[i] = getDistance(testRecord, train[i]);
		}
		
		int[] smallest =  findSmallest(distance, k);
		KNNRecord[] toRet = new KNNRecord[k];
		
		for (int i = 0; i < k; i++) {
			KNNRecord r = train[smallest[i]];
			toRet[i] = r;
		}
		
		
		
		return toRet;
	}
	
	public static int[] findSmallest(double[] distance, int k) {
		
		/*
		 * This method finds the k smallest distances.
		 */
		
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		
		for (int i = 0; i < distance.length; i++) {
			map.put(i, distance[i]);
		}
		
		int[] smallest = new int[k];
		// Get and remove smallest (k times)
		
		HashMap<Integer, Double> mapCopy = (HashMap<Integer, Double>) map.clone();
		for (int i = 0; i < k; i++) {
			smallest[i] = getAndRemoveSmallest(mapCopy);
		}
		
		return smallest;
	}
	
	public static int getAndRemoveSmallest(HashMap<Integer, Double> map) {
		
		/*
		 * This method finds the smallest distance and removes it from the list
		 * as to not get duplicates
		 */
		
		double small = Double.MAX_VALUE;
		int count = 0;
		int index = -1;
		
		for (Double d : map.values()) {
			if (d < small) {
				small = d.doubleValue();
				index = count;
			}
			count++;
		}
		map.remove(index);
	    
		return index;
	}
	
	
	public static double getDistance(KNNRecord test, KNNRecord train) {
		
		/*
		 * This methods calculates the euclidian distances between two records and their attributes
		 */
		
		double distance = Math.sqrt(
				Math.pow((test.lang - train.lang), 2)
				+
				Math.pow((test.hours - train.hours), 2)
				+
				Math.pow((test.games - train.games), 2)
				);
		return distance;
	}
	
	public static KNNRecord[] getNormalizedEveryone(Record[] original) {
		
		/*
		 * This methods normalizes the data, so each attribute "weighs" the same when calculating the distances
		 */
		
		KNNRecord[] all = new KNNRecord[original.length];
		
		// Variables for storing the maximum and minimum values
		double maxLang, minLang, maxHours, minHours, maxGames, minGames;

		double[] langMinMax = getMinMaxLang(original);
		minLang = langMinMax[0];
		maxLang = langMinMax[1];

		double[] hoursMinMax = getMinMaxHours(original);
		minHours = hoursMinMax[0];
		maxHours = hoursMinMax[1];

		double[] gamesMinMax = getMinMaxGames(original);
		minGames = gamesMinMax[0];
		maxGames = gamesMinMax[1];
		
		// Create new KNNRecord object and store the normalized values
		for (int i = 0; i < all.length; i++) {
			double normLang = calcNormData(original[i].getLanguagesKNN(), minLang, maxLang);
			double normHours = calcNormData(original[i].getHoursPlayedKNN(), minHours, maxHours);
			double normGames = calcNormData(original[i].getGamesKNN(), minGames, maxGames);
			double student;
			if (original[i].getGamesOrSDT().equalsIgnoreCase("games")) {
				student = 1;
			} else {
				student = 0;
			}
			
			KNNRecord r = new KNNRecord(normLang, normHours, normGames, student);
			
			all[i] = r;
		}
		
		
		// Get the normalized languages count
		double[] normalizedLang = new double[original.length];
		
		for (int i = 0; i < normalizedLang.length; i++) {
			normalizedLang[i] = calcNormData(original[i].getLanguagesKNN(), minLang, maxLang);
		}
		
		// Get the normalized hours count
		double[] normalizedHours = new double[original.length];
		
		for (int i = 0; i < normalizedHours.length; i++) {
			normalizedHours[i] = calcNormData(original[i].getHoursPlayedKNN(), minHours, maxHours);
		}
		
		// Get the normalized games count
		double[] normalizedGames = new double[original.length];
		
		for (int i = 0; i < normalizedGames.length; i++) {
			normalizedGames[i] = calcNormData(original[i].getGamesKNN(), minGames, maxGames);
		}
		
		return all;
	}
	
	public static double calcNormData(double standardVal, double minVal, double maxVal) {
		
		/*
		 * Converts a value to s normalized one by the formula: x_normalized = \frac{x_std - x_min}{x_max - x_min}
		 */
		double calc = ((standardVal - minVal)/(maxVal - minVal));
		return calc;
	}
	
	
	public static double[] getMinMaxGames(Record[] everyone) {
		
		/*
		 * Getter for max and min number of games in the records array
		 */
		
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		
		double[] ret = new double[2];
		
		for (Record r : everyone) {
			if (r.getGamesKNN() > max) {
				max = r.getGamesKNN();
			}
			if (r.getGamesKNN() < min) {
				min = r.getGamesKNN();
			}
		}
		
		ret[0] = min;
		ret[1] = max;
		
		return ret;
	}
	
	public static double[] getMinMaxHours(Record[] trainingSet) {
		
		/*
		 * Getter for max and min hours in the records array
		 */
		
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		
		double[] ret = new double[2];
		
		for (Record r : trainingSet) {
			if (r.getHoursPlayedKNN() > max) {
				max = r.getHoursPlayedKNN();
			}
			if (r.getHoursPlayedKNN() < min) {
				min = r.getHoursPlayedKNN();
			}
		}
		
		ret[0] = min;
		ret[1] = max;
		
		return ret;
	}
	
	
	public static double[] getMinMaxLang(Record[] trainingSet) {
		
		/*
		 * Getter for max and min number of languages in the records array
		 */
		
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		
		double[] ret = new double[2];
		
		for (Record r : trainingSet) {
			if (r.getLanguagesKNN() > max) {
				max = r.getLanguagesKNN();
			}
			if (r.getLanguagesKNN() < min) {
				min = r.getLanguagesKNN();
			}
		}
		
		ret[0] = min;
		ret[1] = max;
		
		return ret;
	}
	
	public static KNNRecord[][] getTrainingSet(KNNRecord[] everyone) {
		
		/*
		 * This method divides the full set of records in two: one for training and one for testing.
		 * 
		 *  The train set is selected randomly and is of size 30, and the test set is simply the remaining records.
		 */
		
		KNNRecord[] copy = new KNNRecord[everyone.length];
		for (int i = 0; i < everyone.length; i++) {
			copy[i] = everyone[i];
		}
		
		KNNRecord[][] all = new KNNRecord[2][30];
		
		KNNRecord[] trainingSet = new KNNRecord[30];
		
		int k = 0;
		while (k < 30) {
			int rand = (int)(Math.random()*copy.length);
			if (copy[rand] != null) {
				trainingSet[k] = copy[rand];
				copy[rand] = null;
				k++;
			}
		}
		
		KNNRecord[] rest = new KNNRecord[(everyone.length-trainingSet.length)];
		int l = 0;
		for (int i = 0; i < copy.length; i++) {
			if (copy[i] != null) {
				rest[l] = copy[i];
				l++;
			}
		}
		
		all[0] = trainingSet;
		all[1] = rest;
		
		return all;
	}
	
}
