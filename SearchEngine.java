package assignment4;
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
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
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
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
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
		// TODO : Add code here
		
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
/*
 * 



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
	 * 	This method will fit in about 30-50 lines (or less)
	 
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		//traversing the internet graph
		//add the url as a node to the graph
		//get all the hyperlinks pointing out of the node and follow it
		//follow it: check if the to and from nodes AND their vertices are in graph 
		//move from URL to all adjacent vertices  (all links pointing out of url)
		//do depthfirst search on each of the w's

		//vertex is not in internet/graph, then add and set visited = true 

		ArrayList addedVertices = new ArrayList<>();

		if(!internet.getVertices().contains(url)) {
			internet.addVertex(url);
			internet.setVisited(url, true);
			addedVertices.add(url);
		}

		ArrayList<String> wordsContained = new ArrayList<>();
		wordsContained = parser.getContent(url);

		//System.out.println(wordIndex.get(wordsContained.get(0)).add(url));
		//System.out.println(wordsContained);
		//System.out.println("words in url: " + wordsContained);
		for(int i=0;i<wordsContained.size();i++) {
			//must pass in an array list of all urls it's mapped to as of yet 
			//if word is already in index and url is not
			//check cases too! if word is in the wordIndex already
			if(wordIndex.containsKey(wordsContained.get(i).toLowerCase()) && wordIndex.containsValue(url)) {
				continue;
			}else if(wordIndex.containsKey(wordsContained.get(i).toLowerCase()) && !wordIndex.containsValue(url)){
				wordIndex.get(wordsContained.get(i).toLowerCase()).add(url);
				continue;
			}else {
				wordIndex.put(wordsContained.get(i).toLowerCase(), addedVertices);
			}
		}

		ArrayList <String> outLinks =  new ArrayList();
		outLinks.addAll(parser.getLinks(url));



		//run through all the outlinks, add each of them + their outlinks + their edges to the graph
		for(int i=0;i<outLinks.size();i++) {

			//if one of the url's outLinks has been marked as visited 
			//AND there is no edge between the current url and the outlink, mark the outLink as not visited 
			if(internet.getVisited(outLinks.get(i)) && !internet.getEdgesInto(outLinks.get(i)).contains(url)){
				internet.setVisited(outLinks.get(i), false);
			}

			if(!internet.getVisited(outLinks.get(i))) {

				internet.addVertex(outLinks.get(i));
				addedVertices.add(outLinks.get(i));
				internet.setVisited(outLinks.get(i), true);
				internet.addEdge(url, outLinks.get(i));


				crawlAndIndex(outLinks.get(i));
			}
		}
		//URL and hyperlinks have been added to internet 
	}



	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		System.out.println("DO I ENTER PAGE RANK AT ALL");

		ArrayList <String> allVertices =  new ArrayList();
		allVertices.addAll(internet.getVertices());
		//System.out.println("all vertices " + allVertices);
		for(int i=0;i<allVertices.size();i++) {
			internet.setPageRank(allVertices.get(i), 1.0);
		}
		boolean bustOut = false;

		while (bustOut == false) {
			System.out.println("IN WHILE LOOOP");
			bustOut = true;
			ArrayList<Double> rankings = computeRanks(allVertices);
			int count=0;
			System.out.println("count: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + count);
			System.out.println(rankings);

			for(int i=0;i<allVertices.size();i++) {
				double oldRank = internet.getPageRank(allVertices.get(i));
				System.out.println("old rank : " + oldRank);
				internet.setPageRank(allVertices.get(i), rankings.get(i));
				double newRank = internet.getPageRank(allVertices.get(i));
				System.out.println("new rank : " + newRank);

				if(Math.abs(newRank - oldRank) > epsilon) {
					bustOut = false;
				}
			}
			count++;
		}
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		ArrayList<Double> rankings = new ArrayList<>();
		System.out.println(vertices + "!!!");
		//first step: finding all vertices pointing to current one 
		for(int i=0;i<vertices.size();i++) {
			System.out.println("i am hereee" + "!!!");
			double newRank = 0;
			ArrayList<String> outLinks = internet.getEdgesInto(vertices.get(i));
			System.out.println("current vertex  " + vertices.get(i) );
			//System.out.println("this is out links or edges going into the specific vertex index: "+ outLinks);
			for(int j=0;j<outLinks.size(); j++) {
				//System.out.println("ENTERING");
				System.out.println("this is current outLinks index " + outLinks.get(j));
				System.out.println("out degree" + internet.getOutDegree(outLinks.get(j)));
				System.out.println("page Rank" + internet.getPageRank(outLinks.get(j)));

				double subRank = internet.getPageRank(outLinks.get(j))/(internet.getOutDegree(outLinks.get(j)));
				System.out.println("subRanks: " + subRank);
				newRank = newRank + subRank;
				System.out.println("newRank: " + newRank);

			}

			//System.out.println("final newRank: " + newRank);
			rankings.add(i, (1-0.5)+0.5*newRank);

		}
		//System.out.println("wxited" + "!!!");

		//System.out.println(rankings);
		return rankings;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 
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
*/
