import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Random;

public class MarkovChain 
{
	//INSTANCE VARIABLES
	/**
	 *	This holds all of the word relationships
	 */
	private WordGraph wg;
	
	/**
	 *	This remembers the lastWord that this Markov Chain generated
	 */
	private String lastWord;

	//CONSTRUCTOR
    public MarkovChain() 
    {
    	//initialize instance variables
    	wg = new WordGraph();
    }
    
    //METHODS
    /**
     *	Add word relationships from the specified file
     */
     
    public WordGraph train(String filename)
    {
    	//TODO: add data from filename to the WordGraph
    	try{
    		Scanner s = new Scanner(new File(filename));
    		String fileContents = "";
    		while(s.hasNextLine()){
    			wg.processString(s.nextLine().trim());
    		}
		//	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("text2.txt", true)), true);
		//	pw.println(wg.toString());
		//	pw.close();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
 		return wg;
    	
    }
    
    /**
     *	Get a list of words that follow lastWord.
	 *	words with more *weight* will appear more times in the list.
	 *  if lastWord is null, then return the words that are neighbors of [START]
     */
    public List<String> getNextWords()
    {
    	if(lastWord == null){
    		lastWord = "[START]";
    	}
    	//TODO: return List<String> of words that are neighbors of lastWord, weighted appropriatly
    	WeightedGraph<String> graph = wg.getGraph();
    	List<String> neighs = graph.getNeighbors(lastWord);
    	////System.out.println(neighs.toString());
    	List<String> weighted = new ArrayList<>();
    	
    	for(String key : neighs){
    		int weight = graph.getWeight(lastWord, key);
    		for(int x = 0; x < weight; x++){
    			weighted.add(key);
    		}
    	}
    	
 
    	return weighted;
    }
    
    /**
     *	Get a word that follows lastWord
     *	Use a weighted random number to pick the next word from the list of all of lastWord's neighbors in wordGraph
     *	If lastWord is null or [END], this should generate a word that *starts* a sentence
     *	Remember the word that is about to be returned in the appropriate instance variable
     */
    public String getNextWord()
    {
    	//TODO: return random word with an edge from lastWord
    	List<String> options = getNextWords();
    	////System.out.println(options);
    	int index = new Random().nextInt(options.size());

    	return options.get(index);
    }
    
    /**
     *	Generate a sentence using the data in the wordGraph. 
     */
    public String generateSentence()
    {
    	//TODO: generate a sentence from [START] to [END]
    	String next = getNextWord();
    	String sentence = "";
    	while(!next.equals("[END]")){
    		sentence += next + " ";
    		lastWord = next;
    		next = getNextWord();
    	}
    	lastWord = null;
    	return sentence;
    }
    
    
}