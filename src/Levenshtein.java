public class Levenshtein {
	
	/*
	 * The following code for calculating the Levenshtein distance between
	 * two words (or in this case programming languages) was heavily inspired
	 * of the example listen in rosettacode.org:
	 * rosettacode.org/wiki/Levenshtein_distance#Java
	 */
	
	public static int distance(String a, String b) {
		
		a = a.toLowerCase();
		b = b.toLowerCase();
		
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

}
