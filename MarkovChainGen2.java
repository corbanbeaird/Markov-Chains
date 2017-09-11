import java.io.*;
import java.util.*;

public class MarkovChainGen2 extends MarkovChain
{
	//INSTANCE VARIABLES
	/**
	 *	This holds relationships between pairs-of-words -> next-word
	 */
	private WordGraph wgPairs;
	
	private String lastWord;
	
	//CONSTRUCTOR
    public MarkovChainGen2() 
    {
    	wgPairs = new WordGraph();
    	//Initialize Instance Variables
    }
    
    //METHODS
    
    @Override
    public WordGraph train(String filename)
    {
    	wgPairs = super.train(filename); //tell the MarkovChain super class to train() like it normally does
    	try{
    		Scanner s = new Scanner(new File(filename));
    		while(s.hasNextLine()){
    			wgPairs.processStringGen2(s.nextLine().trim());
    		}
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("text.txt", false)), true);
			pw.println(wgPairs.toString());
			pw.close();
			//wgPairs.reset();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	//TODO: do some magic to store 2nd order markov chain data
    	return wgPairs;
    }
    
    
    //TODO: override any other other methods needed to generate 2nd Degree Markov Chains
    
  @Override
  public List<String> getNextWords()
    {
		if(lastWord == null){
    		lastWord = "[START]";
    	}
    	WeightedGraph<String> graph = wgPairs.getGraph();
		List<String> words = graph.getNeighbors(lastWord);
		//System.out.println(lastWord);
	
		
		List<String> weighted = new ArrayList<>();
			//List<String> secondOrder = new ArrayList<>();
		
		
		for(String word : words){
			if(word.equals("[END]")){
				weighted = new ArrayList<String>();
				weighted.add(word);
				return weighted;
			}
			String str = lastWord + " " + word;
			List<String> neighs = graph.getNeighbors(str);
			//System.out.println(neighs.size());
			for(String key : neighs){
    			int weight = graph.getWeight(str, key);
    			for(int x = 0; x < weight; x++){
    				weighted.add(word + " " + key);
    			}
			}
		
		}
		//System.out.println(weighted.size());
		//System.out.println(weighted.get(0));
    	return weighted;
    }
    
    /**
     *	Get a word that follows lastWord
     *	Use a weighted random number to pick the next word from the list of all of lastWord's neighbors in wordGraph
     *	If lastWord is null or [END], this should generate a word that *starts* a sentence
     *	Remember the word that is about to be returned in the appropriate instance variable
     */
    @Override
    public String getNextWord()
    {
    	//TODO: return random word with an edge from lastWord
    	List<String> options = getNextWords();
    	//System.out.println(options);
    	int index = new Random().nextInt(options.size());
    	return options.get(index);
    }
    
    /**
     *	Generate a sentence using the data in the wordGraph. 
     */
    @Override 
    public String generateSentence()
    {
    	//TODO: generate a sentence from [START] to [END]
    	String next = getNextWord();
    	//System.out.println("Next Word " + next);
    	String sentence = "";
    	while(!next.equals("[END]")){
    		String[] str = next.split(" ");
    		if((str[1]).equals("[END]")){
    			sentence += str[0];
    			lastWord = null;
    			return sentence;
    		}
    		sentence += next + " ";
    		lastWord = str[1];
    		next = getNextWord();
    	}
    	lastWord = null;
    	return sentence;
    }
}