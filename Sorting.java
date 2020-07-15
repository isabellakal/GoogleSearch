import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	// 
    	
    	//FOR EVERY HEAPLIST REPLACE WITH SORTED URLS
    	//FOR EVERY L REPLACE WITH lengthUrls
		ArrayList<K> sortedUrls=buildHeap(results);
		int lengthUrls=sortedUrls.size()-1; //1-L+1
		for(int i=1;i<=lengthUrls-1;i++) {
			K temp = sortedUrls.get(1);
			sortedUrls.set(1, sortedUrls.get(lengthUrls+1-i));
			sortedUrls.set(lengthUrls+1-i,temp);
			downHeap(results,sortedUrls,lengthUrls-i);// sorting in decreasing way
		}
		sortedUrls.remove(0);//remove the first one to get a complete arraylist

		return sortedUrls;
	}
    
    private static <K, V extends Comparable> ArrayList<K> downHeap(HashMap<K, V> results,ArrayList<K> sortedUrls,int max){
		int i=1;
		while(2*i<=max) {
			int child=2*i;//find child
			if(child<max) {
				if(results.get(sortedUrls.get(child+1)).compareTo(results.get(sortedUrls.get(child))) <0) {
					child=child+1;//define larger child
				}
			}
			if(results.get(sortedUrls.get(child)).compareTo(results.get(sortedUrls.get(i))) <0) {
				K temp = sortedUrls.get(i);//swap child with i
				sortedUrls.set(i, sortedUrls.get(child));
				sortedUrls.set(child,temp);
				i=child;
			}else {
				break;
			}
		}return sortedUrls;
	}
    
	private static <K, V extends Comparable> ArrayList<K> buildHeap(HashMap<K, V> results){
		ArrayList<K> sortedUrls=new ArrayList<K>();
		sortedUrls.add(null);//the heap is starting from 1
		sortedUrls.addAll(results.keySet());//build heap
		for(int k=0;k<sortedUrls.size();k++) {
			upHeap(results,sortedUrls,k);
		}
		return sortedUrls;
	}
	private static <K, V extends Comparable> ArrayList<K> upHeap(HashMap<K, V> results,ArrayList<K> sortedUrls, int k){
		int i=k;
		while(i>1&&results.get(sortedUrls.get(i)).compareTo(results.get(sortedUrls.get(i/2)))<0) {
			K temp=sortedUrls.get(i);//swap element i and i/2;
			sortedUrls.set(i, sortedUrls.get(i/2));
			sortedUrls.set(i/2, temp);
			i=i/2;
		}
		return sortedUrls;
	}
    

}
