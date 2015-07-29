import java.util.Hashtable;



public class KMeans {
	
	public static void mainIsh(Record[] all, int k) {
		
		
		// Create a copy of the records
		Record[] everyone = new Record[all.length];
		for (int i = 0; i < everyone.length; i++) {
			everyone[i] = all[i];
		}
		
		// Create k random clusters with the previously chosen records age as the mean 
		KMeansCluster[] clusters = new KMeansCluster[k];
		do {
			int[] randRecords = getKRecords(everyone, k); // Choose k random records
			for (int i = 0; i < k; i++) {
				clusters[i] = new KMeansCluster(everyone[randRecords[i]].getAge());
			}
		} while (!allDiffInitMean(clusters)); // To avoid clusters of the same ages
		
		
		System.out.println("Initial cluster means: ");
		for (KMeansCluster c : clusters) {
			System.out.print(c.getInitialMean() + " ");
		}
		
		// Allocate all records to the cluster to which they are closest (in distance)
		System.out.println();
		for (int i = 0; i < everyone.length; i++) {
			
			int age = everyone[i].getAge();
			
			int closestCluster = findBestInitialCluster(clusters, age);
			clusters[closestCluster].add(everyone[i]);
		}
		
		// Re-calculate the means of all clusters
		for (KMeansCluster c : clusters) {
			c.updateMean();
		}
		
		int movement;
		do {
			movement = 0;
			for (int i = 0; i < clusters.length; i++) {
				
				for (int l = 0; l < clusters[i].ClusterMembers.size(); l++) {
					Record record = clusters[i].ClusterMembers.get(l);
					int bestCluster = findBestCluster(clusters, record.getAge());
					if (bestCluster != i) {
						clusters[i].ClusterMembers.remove(record);
						clusters[bestCluster].add(record);
						movement++;
					}
				}
				
			}
			
		} while (movement > 0);
		
		for (KMeansCluster c : clusters) {
			System.out.println(c.toString());
		}
		
	}
	
	public static int findBestCluster(KMeansCluster[] clusters, int age) {
		
		/*
		 * This method finds the most appriopriate cluster for any record's age
		 * by conparing to the cluster mean
		 */
		
		double difference = Double.MAX_VALUE;
		int bestCluster = Integer.MAX_VALUE;
		
		for (int i = 0; i < clusters.length; i++) {
			if (Math.abs(clusters[i].getMean()-age) < difference) {
				difference = Math.abs(clusters[i].getMean()-age);
				bestCluster = i;
			}
		}
		return bestCluster;
	}
	
	
	public static int findBestInitialCluster(KMeansCluster[] clusters, int age) {
		
		/*
		 * This method finds the initial best cluster for a record
		 * by comparing the age to the cluster mean
		 */
		
		double difference = Double.MAX_VALUE;
		int bestCluster = Integer.MAX_VALUE;
		
		for (int i = 0; i < clusters.length; i++) {
			if (Math.abs(clusters[i].getInitialMean()-age) < difference) {
				difference = Math.abs(clusters[i].getInitialMean()-age);
				bestCluster = i;
			}
		}
		return bestCluster;
	}
	
	public static int[] getKRecords(Record[] everyone, int k) {
		
		/*
		 * This method returns k ages/records to serve as initial means
		 * for clusters
		 */
		
		int[] randoms = new int[k];
		int[] keepTrack = new int[everyone.length];
		
		for (int i = 0; i < randoms.length; i++) {
			int rand;
			do {
				rand = (int)(Math.random()*everyone.length);
			} while (keepTrack[rand] == 1);
			randoms[i] = rand;
			keepTrack[rand] = 1;
		}
		
		return randoms;
	}
	
	public static boolean allDiffInitMean(KMeansCluster[] clusters) {
		
		/*
		 * This method ensures that the k clusters all have different
		 * initial means. This is to ensure no lonely clusters
		 */
		
		int k = clusters.length;
		
		Hashtable<Double, Integer> meh = new Hashtable<Double, Integer>();
		
		for (int i = 0; i < clusters.length; i++) {
			meh.put(clusters[i].getInitialMean(), 1);
		}
		
		if (meh.size() == k) {
			return true;
		}
		return false;
		
	}
	
}
