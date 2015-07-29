import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Apriori {

	
	/**
	 * This is the main method for the Apriori Frequent Pattern Mining method.
	 * 
	 * @param 	langauges		an array of ArrayLists where every index is an ArrayList of the programming languages of a student
	 * @param	minimumSupport	the minimum support specified as a decimal (e.g. 0.25)
	 * 
	 * @return					void
	 */
	public static void mainIsh(ArrayList[] languages, int minimumSupport) {
		
		Hashtable<ItemSet, Integer> frequentItemSets = generateFrequentItemSetsLevel1(languages, minimumSupport);
		
		
		for (int k = 1; frequentItemSets.size() > 0; k++) {

			System.out.println("Trying to find frequent itemsets of length " + (k + 1) + "…");
			
			frequentItemSets = generateFrequentItemSets(minimumSupport, languages, frequentItemSets);
			
			if (frequentItemSets.size() == 0) {
				System.out.println("...but failed! No more frequent itemsets \n--APRIORI END--");
				break;
			}
			
			System.out.println(" found " + frequentItemSets.size() + ":");
			System.out.println(frequentItemSets.toString());
		}

	}
	
	
	/**
	 * This method creates the initial frequent item sets
	 * 
	 * @param 	langauges		an array of ArrayLists where every index is an ArrayList of the programming languages of a student
	 * @param	minimumSupport	the minimum support specified as a decimal (e.g. 0.25)
	 * 
	 * @return	Hashtable		The initial frequent item sets
	 */
	private static Hashtable<ItemSet, Integer> generateFrequentItemSetsLevel1(ArrayList[] languages, 
			int minimumSupport) {
		
		Hashtable<ItemSet, Integer> map = new Hashtable<ItemSet, Integer>();
		
		for (ArrayList<String> al : languages) {
			for (String s : al) {
				ItemSet item = new ItemSet();
				item.add(s);
				if (!map.containsKey(item)) {
					map.put(item, countSupport(item, languages));
				}
			}
		}
		
		Hashtable<ItemSet, Integer> supportedMap = new Hashtable<ItemSet, Integer>();
		
		for (ItemSet item : map.keySet()) {
			if (map.get(item) >= minimumSupport) {
				Collections.sort(item.items);
				supportedMap.put(item, map.get(item));
			}
		}
		
		return supportedMap;
	}

	
	/**
	 * This method generates all the supported frequent item sets after the initial ones.
	 * 
	 * @param	minimumSupport	the minimum support specified as a decimal (e.g. 0.25)
	 * @param	langauges		an array of ArrayLists where every index is an ArrayList of the programming languages of a student
	 * @param	lowerLevelItemsets	frequent item sets form the previous iteration
	 *
	 * @return					the frequent item sets of length k+1
	 */
	private static Hashtable<ItemSet, Integer> generateFrequentItemSets(int minimumSupport, 
			ArrayList[] languages, Hashtable<ItemSet, Integer> lowerLevelItemSets) {
		
		Hashtable<ItemSet, Integer> potentiallyNewItemSets = extendItemSets(lowerLevelItemSets, languages);
		
		Hashtable<ItemSet, Integer> supportedMap = new Hashtable<ItemSet, Integer>();
		
		for (ItemSet item : potentiallyNewItemSets.keySet()) {
			if (potentiallyNewItemSets.get(item) >= minimumSupport) {
				Collections.sort(item.items);
				supportedMap.put(item, potentiallyNewItemSets.get(item));
			}
		}
		
		return supportedMap;
	}
	
	/**
	 * Extends a set of frequent item sets with the langauges that are still supported
	 * essentially creating every new permutation of the item sets with the available
	 * languages. It DOESN't take into account which item sets are supported.
	 * 
	 * @param	original	the original frequent item sets of length k
	 * @param	languages	all languages
	 * 
	 * @return				all new potentially frequent item sets
	 */
	public static Hashtable<ItemSet, Integer> extendItemSets(Hashtable<ItemSet, Integer> original, 
			ArrayList[] languages) {
		
		// Get a list of unique languages that are still supported
		ArrayList<String> uniques = new ArrayList<String>();
		
		for (ItemSet item : original.keySet()) {
			for (String s : item.items) {
				if (!uniques.contains(s)) {
					uniques.add(s);
				}
			}
		}
		
		
		// Extend every item set with every unique language
		Hashtable<ItemSet, Integer> newItemSets = new Hashtable<ItemSet, Integer>();
		
		for (ItemSet item : original.keySet()) {
			for (String s : uniques) {
				ItemSet i = new ItemSet();
				
				for (String o : item.items) {
					i.add(o); // add all "original" languages
				}
				
				if (i.items.contains(s)) {
					continue;
				} else {
					i.add(s);
				}
					
				Collections.sort(i.items);
				if (!newItemSets.contains(i)) { // Only add if its not already there!
					newItemSets.put(i, countSupport(i, languages));
				}
					
			}
		}
		
		return newItemSets;
	}
	
	/**
	 * This method counts the support of an item set
	 * 
 	 *@param	itemSet		An item set to be counted
 	 *@param	langauges	List all languages
 	 *@return	int			The support count of the item set
	 */

	private static int countSupport(ItemSet itemSet, ArrayList[] languages) {
		// Assumes that items in ItemSets and transactions are both unique
		
		int len = itemSet.getSize();
		int smallerCount = 0; // has to == len for all the items of itemset to be present
		int count = 0;
		
		for (ArrayList<String> al : languages) {
			for (String s : itemSet.items) {
				if (al.contains(s)) {
					smallerCount++;
				}
			}
			if (smallerCount == len) {
				count++;
			}
			smallerCount = 0;
		}
		
		return count;
	}

}
