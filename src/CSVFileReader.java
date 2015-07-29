
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The CSVFileReader class is used to load a csv file
 * 
 * @author andershh and hjab
 *
 */
public class CSVFileReader {
	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * This method is utilizes the string.split method for splitting each line
	 * of the data file. String tokenizer bug fix provided by Martin Marcher.
	 * 
	 * @param csvFile
	 *            File to load
	 * @param seperationChar
	 *            Character used to seperate entries
	 * @param nullValue
	 *            What to insert in case of missing values
	 * @return Data file content as a 2D string array
	 * @throws IOException
	 */
	public static String[][] readDataFile(String csvFile,
			String seperationChar, String nullValue,
			boolean checkForQuoteMarkRange) throws IOException {
		List<String[]> lines = new ArrayList<String[]>();
		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(
				csvFile)));

		// read the header
		// String line = bufRdr.readLine(); // This has been commented out to
		// allow searching through headers
		String line;
		while ((line = bufRdr.readLine()) != null) {
			String[] arr = line.split(seperationChar);

			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equals("")) {
					arr[i] = nullValue;
				}
			}

			if (checkForQuoteMarkRange) {
				arr = CheckForQuoteMarkRange(arr);
			}

			lines.add(arr);
		}

		String[][] ret = new String[lines.size()][];
		bufRdr.close();
		return lines.toArray(ret);
	}

	/**
	 * This method checks a line of date from the data to ensure that no piece
	 * of the data has been split up due to how the questionnaire data set
	 * handles decimal values and lists of data - eg. "181,5" and
	 * "Fifa 2014, Grand theft auto V". This is a problem since we use the comma
	 * to split read-in lines of data into attributes of data, and since because
	 * of this attribute data may erroneously be split up e.g. "181,5" is split
	 * up into two attributes - 181 and 5.
	 * 
	 * @param line
	 *            Line of data to check.
	 * @return Line of data corrected if needed.
	 */
	private static String[] CheckForQuoteMarkRange(String[] line) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> keeper = new ArrayList<String>();
		boolean addToKeeper = false;

		for (String part : line) {
			if (part.contains("\"")) {
				if (addToKeeper) {
					addToKeeper = false;
					keeper.add(part);
					String toAdd = keeper.toString();
					result.add(toAdd.substring(1, toAdd.length() - 1));// Done
																		// to
																		// remove
																		// brackets
																		// from
																		// the
																		// string.
					keeper.clear();
				} else {
					addToKeeper = true;
					keeper.add(part);
				}
			} else {
				if (addToKeeper) {
					keeper.add(part);
				} else {
					result.add(part);
				}
			}
		}
		String[] res = new String[result.size()];

		return result.toArray(res);
	}

	public static void main(String args[]) {

		try {
			String[][] data = readDataFile("DataMining2015Responses.csv", ",",
					"-", true);
			
			// Get all cleaned data and create a record with these stored
			ArrayList<String>[] lang = Preprocess.cleanAndGetLanguages(data);
			ArrayList<String>[] games = Preprocess.cleanAndGetGamesPlayed(data);
			String[] hours = Preprocess.cleanAndGetGameHoursPlayed(data);
			String[] student = Preprocess.getID3gamesOrSDT(data);
			int[] ages = Preprocess.cleanAndGetAge(data);

			Record[] everyone = new Record[data.length - 1];

			for (int i = 0; i < everyone.length; i++) {
				Record r = new Record(lang[i], games[i], hours[i], student[i], ages[i]);
				everyone[i] = r;
			}

			KNN.mainIsh(everyone);
			
			
			Apriori.mainIsh(lang, 15); // 15 set as minimum support as default. feel free to change
			
			
			KMeans.mainIsh(everyone, 3); // 3 set as number of clusters as default. feel free to change
			

		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}

	}

	public static void printArray(double[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static void printArray(String[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static void printArray(char[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static void printArray(ArrayList[] a) {
		for (ArrayList l : a) {
			System.out.println(l.toString());
		}
	}
}
