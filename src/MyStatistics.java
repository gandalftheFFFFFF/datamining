import java.util.HashMap;
import java.util.Map;


public class MyStatistics {
	
	/*
	 * This class is used for simple statistic calculations for data pre-processing
	 */
	
	public static char getMode(char[] a) {
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		
		for (int i = 0; i < a.length; i++) {
			if (a[i] == '0') {
				// Illegal value - don't add to map
			} else {
				if (map.containsKey(a[i])) {
					map.put(a[i], map.get(a[i]) + 1);
				} else {
					map.put(a[i], 1);
				}
			}
		}
		
		char mostFrequentChar = 'Æ';
		int mostFrequentCount = 0;
		
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			Character key = entry.getKey();
		    Integer value = entry.getValue();
		    if (value > mostFrequentCount) {
		    	mostFrequentCount = value;
		    	mostFrequentChar = key;
		    }
		}
		return mostFrequentChar; 
	}
	
	public static int getAverage(int[] a) {
		
		/*
		 * This static method takes an array of integers and calculates the average 
		 * (ignoring values below 0 [illegal values are set to -1 when pre-processing the array])
		 */
		
		double sum = 0;
		double count = 0;
		
		for (int i = 0; i < a.length; i++) {
			if (a[i] < 0) {
				
			} else {
				sum += a[i];
				count++;
			}
		}
		
		return (int)(sum / (double)count);
	}
	
	public static double getAverage(double[] a) {
		
		/*
		 * This static method takes an array of doubles and calculates the average 
		 * (ignoring values below 0 [illegal values are set to -1 when pre-processing the array])
		 */
		
		double sum = 0;
		double count = 0;
		
		for (int i = 0; i < a.length; i++) {
			if (a[i] < 0) {
				
			} else {
				sum += a[i];
				count++;
			}
		}
		
		return (sum / count);
	}

	public static double getVariance(int[] a) {
		
		double average = getAverage(a);
		
		double sumOfSquaredDifferences = 0;
		
		for (int i = 0; i < a.length; i++) {
			sumOfSquaredDifferences = sumOfSquaredDifferences + Math.pow((a[i] - average),2);
		}
		
		return sumOfSquaredDifferences / (double)a.length;
	}
	
	public static double getVariance(double[] a) {
		
		double average = getAverage(a);
		
		double sumOfSquaredDifferences = 0;
		
		for (int i = 0; i < a.length; i++) {
			sumOfSquaredDifferences = sumOfSquaredDifferences + Math.pow((a[i] - average),2);
		}
		
		return sumOfSquaredDifferences / (double)a.length;
	}
	
	public static double getStandardDeviation(int[] a) {
		
		return (Math.sqrt(getVariance(a)));
	}
	
	public static double getStandardDeviation(double[] a) {
		
		return (Math.sqrt(getVariance(a)));
	}
}
