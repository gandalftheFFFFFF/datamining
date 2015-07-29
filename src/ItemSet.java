import java.util.ArrayList;
import java.util.Collections;


public class ItemSet {
	/*
	 * This class is used for the Apriori class and contains primarily an ArrayList of strings (the languages known)
	 */
	public ArrayList<String> items = new ArrayList<String>();
	
	public ItemSet() {
		// empty
	}
	
	public void add(String s) {
		items.add(s); 
	}
	
	public int getSize() {
		if (items.isEmpty()) {
			return 0;
		} else {
			return items.size();
		}
	}
	
	public int hashCode() {
        int code = 0;
        Collections.sort(items);
        for (String s : items) {
        	code += s.hashCode();
        }
        return code;
    }
	
	public boolean equals(Object o) {
		if (!(o instanceof ItemSet)) {
			return false;
		}
		ItemSet other = (ItemSet) o;
		Collections.sort(items);
		Collections.sort(other.items);
		if (other.items.size() != this.items.size()) {
			return false;
		}
		for (int i = 0; i < items.size(); i++) {
			if (!items.get(i).equals(other.items.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return items.toString();
	}
	
}
