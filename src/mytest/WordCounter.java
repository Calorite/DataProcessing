package mytest;


import java.util.*;

public class WordCounter {
       List<CountWord> countWordList;
       public WordCounter(){
    	   countWordList =new ArrayList<CountWord>();
       }
       
       public void add(Morpheme m){
    	   boolean found=false;
    	   for(CountWord cw:countWordList){
    		   if(cw.word.kihonkei.equals(m.kihonkei)&&cw.word.hinshi.equals(m.hinshi)){
    			   found=true;
    			   cw.number++;
    			   break;
    		   }
    	   }
    	   if(!found){
    		   CountWord cw=new CountWord();
    		   cw.word=m;
    		   cw.number=1;
    		   countWordList.add(cw);
    	   }
       }
       public int getNumber(Morpheme m){
    	   for(CountWord cw:countWordList){
    		   if(cw.word.equals(m)){
    			   return cw.number;
    		   }
    	   }
    	   return 0;
       }
       
       public List<Morpheme> getWordList(){
    	   List<Morpheme> wordList=new ArrayList<Morpheme>();
    	   for(CountWord cw:countWordList){
    		   wordList.add(cw.word);
    	   }
    	   return wordList;
       }
       
       public List<Morpheme> getSortedWordList(){
    	   Collections.sort(countWordList,new Comparator<CountWord>(){
    	   public int compare(CountWord cw1,CountWord cw2){
    		   return cw2.number-cw1.number;
    //		   return cw1.word.compareTo(cw2.word);
    	   }
       });
    	   return getWordList();
}
}