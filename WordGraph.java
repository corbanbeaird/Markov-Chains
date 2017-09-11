import java.io.*;
import java.util.*;

class WordGraph 
{
	//INSTANCE VARIABLES
	/**
	 *	Keep track of assoociation betweens consecutive words.
	 *	An edge between words (A -> B)means that B came after A
	 *	The weight of hte edge tells you how many times B came after A
	 */
	private WeightedGraph<String> graph;
	
	/**
	 *	Keep track of the lastWord that was added to the graph.
	 *	When adding a new word to the graph, add an edge between
	 *	the lastWord and the new word.
	 */
	private String lastWord;
	private String previousWord;
	private String lastPreviousWord; 
		
	private String storedTail;
	
	//CONSTRUCTOR
	public WordGraph()
	{
		//initialize instance varaibles
	
		//TODO: create a weighted graph object
		graph = new WeightedAdjacencyListGraph<String>();
		//lastWord starts as null.
		graph.add("[START]");
		graph.add("[END]");
	}
	
	//METHODS
	/**
	 *	"Sanatize" newWord by trimming extra spaces from the endges (use the trim() method)
	 *	Add the specified word to the graph.
	 *	Add an edge between lastWord and the newWord
	 *	Increment the weight between these nodes by 1
	 *	Set lastWord to point to newWord
	 */
	public void addWord(String newWord)
	{
		String cleanWord = newWord.trim();
		if(cleanWord.equals("")){}
		else{
			if(lastWord == null){
				lastWord = "[START]";
				//System.out.println(lastWord);
			}
			//System.out.println(cleanWord);
			boolean ret = graph.add(cleanWord);
			//System.out.print(ret);
			ret = graph.addEdge(lastWord, cleanWord);
			//System.out.println( " " + ret);
			int weight = graph.getWeight(lastWord, cleanWord);
			graph.setWeight(lastWord, cleanWord, weight + 1);
			lastWord = cleanWord;
			if(isEndWord(cleanWord)){
				//System.out.println(cleanWord);
				graph.addEdge(cleanWord, "[END]");
				graph.setWeight(cleanWord, "[END]", graph.getWeight(cleanWord, "[END]") + 1);
				lastWord = null;
			}
		}
		//TODO: add a word to the graph instance variable
	}

	
	
	
	public void addPair(String newWord){
		newWord = newWord.trim();
		if(newWord.equals("")){}
		else{
			if(lastWord == null){
				lastWord = "[START]";
				previousWord = newWord;
			}
	
			String pair = lastWord + " " + previousWord;
			graph.add(pair);
			if(!isEndWord(previousWord)){
			if(previousWord != newWord){
				graph.add(newWord);
				graph.addEdge(pair, newWord);
				graph.setWeight(pair, newWord, graph.getWeight(pair, newWord) + 1);
				if(isEndWord(newWord)){
					String pairEnd = previousWord + " " + newWord;
					graph.add(pairEnd);
					graph.addEdge(pairEnd, "[END]");
					graph.setWeight(pairEnd, "[END]", graph.getWeight(pairEnd, "[END]") + 1);
					lastWord = null;
				}
				lastWord = previousWord;
				previousWord = newWord;
			}
			}
			else{
				graph.addEdge(pair, "[END]");
				graph.setWeight(pair, "[END]", graph.getWeight(pair, "[END]") + 1);
				lastWord = null;
			}
		}		
	}

	public void addTriplet(String newWord){
		
		newWord = newWord.trim();
		
		if(newWord.equals("")){}
		
		else{
			//Possible Bug: Includes words after the sentence ends
			if(lastWord == null){
				lastWord = "[START]";
				previousWord = newWord;
				lastPreviousWord = null;
			}
			
			else if(lastPreviousWord == null){	lastPreviousWord = newWord;	}
			
			else{
				
				String triplet = lastWord + " " + previousWord + " " + lastPreviousWord;
				graph.add(triplet);
				
				if(!isEndWord(lastPreviousWord)){
					
					if(lastPreviousWord != newWord){
						
						graph.add(newWord);
						graph.addEdge(triplet, newWord);
						graph.setWeight(triplet, newWord, graph.getWeight(triplet, newWord) + 1);
						
						if(isEndWord(newWord)){
							String tripletEnd = previousWord + " " + lastPreviousWord + " " + newWord;
							
							graph.add(tripletEnd);
							graph.addEdge(tripletEnd, "[END]");
							graph.setWeight(tripletEnd, "[END]", graph.getWeight(tripletEnd, "[END]") + 1);
							
							lastWord = null;
						}
						
						lastWord = previousWord;
						previousWord = newWord;
					}
				}
				
				else{
					graph.addEdge(triplet, "[END]");
					graph.setWeight(triplet, "[END]", graph.getWeight(triplet, "[END]") + 1);
					lastWord = null;
				}
			}
		}			
	}
			

	





	/**
	 *	Process a string by splitting it on spaces (use the split() method)
	 *	and calling addWord() on each word.
	 */	
	public void processString(String str)
	{
		String[] words = str.split(" ");
		for(String word : words){
			addWord(word);
		}
		//TODO: add each word from str to the graph instance variable
	}
	
	
	public void processStringGen2(String str){
		String[] words = str.split(" ");
		for(int x = 0; x < words.length; x++){
			//The fat cat was brown
			/* [START] The -> fat, The fat -> cat, fat cat -> was, cat was -> brown, was brown -> end*/
				addPair(words[x]);
			}	
	}
	
	public void processStringTriplets(String str){
		String[] words = str.split(" ");
		for(int x  = 0; x < words.length; x++){
			addTriplet(words[x]);
		}
	}
	
	
	
	
	/**
	 *	Process a file by reading each line from a file (using nextLine() method)
	 *	and call the processString() method on it.
	 */
	public void processFile(String filename)
	{
		try
		{
			//open the specified file
			File file = new File(filename);
			Scanner in = new Scanner(file);
			
			//loop through each line of the file and process it
			while(in.hasNextLine())
				processString(in.nextLine());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 *	Getter method for the weighted graph instance variable
	 */
	public WeightedGraph<String> getGraph()
	{
		return graph;
	}
	
	
	public boolean isEndWord(String str){
		String word = str.substring(str.length()-1, str.length());
		return word.contains(".") || word.contains("?") || word.contains("?") || 
				word.contains("!")	|| word.contains("’") || word.contains("\"");
	}
	
	public String toString(){
		return graph.toString();
	
	}
	public void reset(){
		lastWord = null;
		previousWord = null;
	}
	
	
}
