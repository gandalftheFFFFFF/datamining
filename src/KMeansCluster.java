import java.util.ArrayList;
import java.util.Iterator;

public class KMeansCluster {

	public ArrayList<Record> ClusterMembers;
	private double mean;

	public KMeansCluster(double mean) {
		this.mean = mean;
		this.ClusterMembers = new ArrayList<Record>();
	}
	
	public void add(Record record) {
		ClusterMembers.add(record);
	}
	
	public void setMean(double mean) {
		this.mean = mean;
	}
	
	public double getInitialMean() {
		return mean;
	}
	
	public double getMean() {
		if (ClusterMembers.size() == 0) {
			return 0;
		}
		double sum = 0;
		for (Record r : ClusterMembers) {
			sum += r.getAge();
		}
		return (sum/(double)ClusterMembers.size());
	}
	
	public void updateMean() {
		if (ClusterMembers.size() == 0) {
			mean = 0;
		} else {
			double sum = 0;
			for (Record r : ClusterMembers) {
				sum += r.getAge();
			}
			mean = (sum/(double)ClusterMembers.size());
		}
	}
	
	

	@Override
	public String toString() {
		String toPrintString = "-----------------------------------CLUSTER START (mean: " + getMean() + ") ------------------------------------------"
				+ System.getProperty("line.separator");

		for (Record r : this.ClusterMembers) {
			toPrintString += r.age + " ";
					//System.getProperty("line.separator");
		}
		toPrintString += "\n-----------------------------------CLUSTER END-----------------------------------------------------------------------------"
				+ System.getProperty("line.separator");

		return toPrintString;
	}
	

}
