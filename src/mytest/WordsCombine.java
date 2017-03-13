package mytest;

import java.util.ArrayList;
import java.util.List;

public class WordsCombine {
 	

	
   public List<String> combine(String words[]){  
      List<String> combinations=new ArrayList<>();
      int n=0;
      for (int i=n;i<words.length;i++) {
		String combination=null;
			for (int j=n+1;j<words.length;j++) {
				combination=words[i]+"&"+words[j];
				combinations.add(combination);
			}
          n++;
		}  
    	       
	  return combinations;
   }  
   
   public List<String> relatecombine(List<String> words1,List<String> words2){
	   List<String> combinations=new ArrayList<>();
	   for (String word1 : words1) {
		for (String word2 : words2) {
			String combination=null;
			combination=word1+"&"+word2;
			combinations.add(combination);
		}
	}
	   return combinations;
   }
   
}
