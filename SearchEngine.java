import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	

	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	
	 */
	public void crawlAndIndex(String url) throws Exception {
		// 
ArrayList<String> allVertices=new ArrayList<String>();// list for storing liked urls
		
		if(internet.addVertex(url)) {
			internet.setVisited(url,true);//any added node, need to be set as visited
		}
			allVertices.addAll(parser.getLinks(url));//add all linked urls to this url
			for(String node: allVertices) {//check each url in the linkedVertex
				if(!internet.getVisited(node)) {
					crawlAndIndex(node);// call recursive steps
				}
				internet.addEdge(url, node);
			}
				
			for(int k=0;k<parser.getContent(url).size();k++) {//update wordIndex
				ArrayList<String> links=new ArrayList<String>();
				if(!allVertices.contains(url)) {
				allVertices.add(url);
				}
				for(int j=0;j<allVertices.size();j++) {
				if(parser.getContent(allVertices.get(j)).contains(parser.getContent(url).get(k))) {
					links.add(allVertices.get(j));
					}
				}
				wordIndex.put(parser.getContent(url).get(k),links);
			}
				
		}



	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 */
	public void assignPageRanks(double epsilon) {
		//
		//replacing pre with old
		ArrayList<Double> old=new ArrayList<Double>();
		//replacing now with current
		ArrayList<Double> current=new ArrayList<Double>();
		//replacing vertices with allVertices
		ArrayList<String> allVertices;
		//replacing count with counter
		int counter=0;
		allVertices=this.internet.getVertices();//all the vertices in the graph
		for(int i=0;i<allVertices.size();i++) {
		internet.setPageRank(allVertices.get(i), 1);//initialize all the ranks with 1
		old.add(internet.getPageRank(allVertices.get(i))); //initial orders
		}
		//1st literation;
		current.addAll(computeRanks(allVertices));
		//literations;
		while(counter!=allVertices.size()) {
			counter=0;
			for(int j=0;j<allVertices.size();j++) {
				double diff=0;
				diff=Math.abs(current.get(j)-old.get(j));
				if(diff<epsilon) {
					counter++;
					}
				}
			for(int k=0;k<allVertices.size();k++) {
				internet.setPageRank(allVertices.get(k), current.get(k));
				}//reset vertices;
			if(counter==allVertices.size()) {
				break;
			}
			ArrayList<Double> ranksNew=computeRanks(allVertices);
				for(int f=0;f<allVertices.size();f++) {//reset 
				old.set(f, current.get(f));//reset pre
				current.set(f, ranksNew.get(f));//reset now
				}
		}
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// 
		
		//replacing temp with oldRank
		ArrayList<String> oldRank;
		//replacing ranks with rankings
				ArrayList<Double> rankings=new ArrayList<Double>();
		//replacing rank with new Rank
		double newRank=0;
		
		int i=0;
		while(i<vertices.size())
		{
			double tempRank=0;
			oldRank=internet.getEdgesInto(vertices.get(i));//all vertices into this;
			for(int j=0;j<oldRank.size();j++) {
				double pageRank=internet.getPageRank(oldRank.get(j));
				double outDegree=internet.getOutDegree(oldRank.get(j));
				tempRank+=(pageRank/outDegree);//sum of all intoEdges' rank
			}
			newRank=0.5+(0.5*tempRank);//calculate with formula 
			rankings.add(i,newRank);
			i++;
		}		
		
		return rankings;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		query = query.toLowerCase();

		ArrayList<String> results = new ArrayList<>();
		ArrayList <String> allVertices =  new ArrayList();
		allVertices.addAll(internet.getVertices());
		ArrayList<Integer> rankSorted =  new ArrayList();

		//want to interate through all words at every url in the internet and see if one matches the query

		for(int b=0;b<allVertices.size();b++) {
			for(int i=0; i< parser.getContent(allVertices.get(b)).size();i++) {

				if(query.equalsIgnoreCase(parser.getContent(allVertices.get(b)).get(i))) {
					//there is a match! urls contain the query
					System.out.println(wordIndex.get(query));
					System.out.println("wordIndex: " + wordIndex.get(query));
					ArrayList <String> listUrls = wordIndex.get(query);

					HashMap<Integer , Integer> organize = new HashMap<>();

					if(!wordIndex.isEmpty()) {
						for(int j =0;j< listUrls.size();j++) {
							int rank = (int)internet.getPageRank(listUrls.get(j));
							organize.put(j, rank);
						}
						rankSorted=Sorting.fastSort(organize);//sorted rank

						for(int k=0;k<rankSorted.size();k++) {
							int index;
							index = rankSorted.get(k);
							results.add(listUrls.get(index));
						}
					}
				}

			}
		}


		return results;
	}
}

