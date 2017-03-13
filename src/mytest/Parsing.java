package mytest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {

	
	private static String getPhrase(String str) {
		String information=null;
		Pattern pattern=Pattern.compile("[0-9]{1,2} (-)?[0-9]{1,2}D");
		Matcher matcher=pattern.matcher(str);
		if (matcher.find()) {
			information=matcher.group(0);
		}
		return information;
	}
	
	private static boolean lineFilter(String line) {
		boolean f=true;
		List<String> list=new ArrayList<>();
		list.add("ログイン");list.add("VIP");list.add("倶楽部");list.add("%（");list.add(")@");list.add(")→");
		list.add("(△");list.add(":＋");list.add("---　");list.add(">　");list.add(">）");list.add(")［");list.add("---■");//list.add();
		list.add("<");list.add(">");list.add(".");list.add("⇒");list.add("∞");list.add("≪");list.add("≫");
		
		for (String i : list) {
			if (line.contains(i)) {
				f=false;
			}
		}
		return f;
	}
	
	
	private static int NoiseFilter(String line) throws UnsupportedEncodingException {
		int f=-1;
		List<String> list=new ArrayList<>();
		list.add("-");
		list.add("<");list.add("[");list.add("%");list.add("(");list.add(")");
		list.add(">");list.add("%、");list.add("-");list.add(".");list.add("null");list.add("+");
		list.add("&");list.add("E");list.add("*");list.add("!」");list.add("!』」");list.add(",");list.add(".");list.add("(");
		list.add("）]");list.add(")");list.add(":");list.add(")　　");list.add(">　※");list.add(">、");list.add(">【");list.add("]");
		list.add("/");list.add(".（");list.add("%）");list.add("!");list.add("％→-");list.add("％<");//list.add("");//list.add("");
		//list.add("");
		if(line.getBytes("shift-jis").length>=(2*line.length())){
			f=0;
		     	for (String i : list) {
			      if (line.equals(i)) {
				     f=1;
			     }	
		      }
		  }			
		return f;
	}
	
	
	private static String getArchetyoe(String line) {
		String str=null;
		List<String> list=new ArrayList<>();
		list.add("連用形");
		list.add("基本形");
		for (String string : list) {
			Pattern pattern=Pattern.compile(string+",[^,]{1,}");
		    Matcher matcher=pattern.matcher(line);
		    if (matcher.find()) {
			   String s=matcher.group(0);
			   String[] st=s.split(",");
			   if (st!=null) {
				str=st[1];
			}
			   
		    }
		}		
		return str;
	}
	
	private static String getWord(String line) {
		String str=null;
		Pattern pattern=Pattern.compile("[^\\s]{1,}");
		Matcher matcher=pattern.matcher(line);
		if (matcher.find()) {
			str=matcher.group(0);
		}
		
		return str;
	}
	
	
	private static boolean POSFilter(String line) {
		boolean f=false;
		List<String> list=new ArrayList<>();
		list.add("名詞,固有名詞");
		list.add("動詞,自立");
		list.add("名詞,一般");
		list.add("名詞,形容動詞語幹");
		list.add("名詞,サ変接続");
		list.add("形容詞,自立");
		for (String i : list) {
			if (line.contains(i)) {
				f=true;
			}
		}
		return f;
	}
	
	
	
	
	
	public List<String> Parsing_Combination(String path){
		WordCounter wc=new WordCounter();
		String text =""; 
		List<String> combinationlist=new ArrayList<>();
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(path),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				text+=line;
			}
			read.close();			
			 Process process = Runtime.getRuntime().exec(new String[] {"I:\\Applications\\Tools\\CaboCha\\bin\\cabocha.exe", "-f1"});
				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						process.getOutputStream())));
			//	OutputStreamWriter pw=null;
				
				String[] lines=text.split("\\。");
				// 入力文章を１行ずつ MeCab に送って形態素解析させる
				
				for (String line1 : lines) {
					pw.println(line1);  // MeCab へ送信
					pw.flush();
					String phrase;
					List<String> combinations=new ArrayList<>();
					List<Received_Relate> relates=new ArrayList<>();
					HashMap<Integer,String> phrases=new HashMap();
					List<String> wordslist=new ArrayList<>();
					String line2;
					String information="";
					int sum=-1;
					
						while ((line2 = br1.readLine()) != null) {
						int id = -1;
					
						if (getPhrase(line2)!=null) {
							Received_Relate rr=new Received_Relate();
							
							phrase=getPhrase(line2);
							if (phrase!=null) {
								String []str=phrase.split(" ");																								
							    if (str!=null) {
							    	sum++;
							    	id=Integer.valueOf(str[0]);
								    rr.id=Integer.valueOf(str[0]);
								    rr.relate=Integer.valueOf(str[1].substring(0, str[1].length()-1));								
								    relates.add(rr);
								    System.out.println(rr.id+"    "+rr.relate);
								    if (id>0) {
								    	if (information.equals("")) {
											
										}else {
											phrases.put(id-1, information);
										}										
									}
								    information ="";
							    }	
							}													
						}else if (line2!=null) {
							if (POSFilter(line2)&&lineFilter(line2)) {
								information=information+line2+"\n";	
							}else if (line2.equals("EOS")) {
							phrases.put(sum, information);
							break;
						   }															
						}				
					}					
						for (int i : phrases.keySet()) {
							String word=null;
							String lp=phrases.get(i);
							List<String> wordslisteachphrase=new ArrayList<>();
							String[] l=lp.split("\n");
							for (String string : l) {
								if (string.contains("動詞,自立")||string.contains("動詞,自立")) {
								 word=getArchetyoe(string);
								
							}else {
								word=getWord(string);
							}	
								if (word!=null) {
									if (NoiseFilter(word)==0) {
									wordslisteachphrase.add(word);
						            System.out.println(word);
								   }
								}
								 
						        
							}
							
							//combination	
							if (wordslisteachphrase.size()>1) {
								WordsCombine combine=new WordsCombine();
						        String[] words1=new String[wordslisteachphrase.size()];
						        int k=0;
						        for(String w:wordslisteachphrase){
							        words1[k]=w;
							        k++;
						         }
						       List<String> combi=new ArrayList<>();
						       combi=combine.combine(words1);
						       combinationlist.addAll(combi);	
							}
												   
					    }	
						
						for (Received_Relate rr : relates) {
							if (phrases.get(rr.id)!=null&&(phrases.get(rr.relate)!=null)&&rr.relate!=-1) {								
								String word1=null;
								String word2=null;
								List<String> words1=new ArrayList<>();
								List<String> words2=new ArrayList<>();
								WordsCombine combine=new WordsCombine();
								List<String> combinationforrelate=new ArrayList<>();
								String lp1=phrases.get(rr.id);
								String[] l1=lp1.split("\n");
								for (String string : l1) {
									if (string.contains("動詞,自立")||string.contains("動詞,自立")) {
									word1=getArchetyoe(string);
								}else {
									word1=getWord(string);
								}
									if (word1!=null) {
										if (NoiseFilter(word1)==0) {
											words1.add(word1);
										}
									}
									
							}
								String lp2=phrases.get(rr.relate);
								String[] l2=lp2.split("\n");
								for (String string : l2) {
									if (string.contains("動詞,自立")||string.contains("動詞,自立")) {
									word2=getArchetyoe(string);
								}else {
									word2=getWord(string);
								}
									if (word2!=null) {
										if (NoiseFilter(word2)==0) {
											words2.add(word2);
										}
									}
									
							}
								combinationforrelate=combine.relatecombine(words1, words2);
								combinationlist.addAll(combinationforrelate);
						}
						 // combination
					}	
						
				}
				    br1.close();			
				    pw.close();
				    process.destroy();
		} catch (IOException ex) {
			ex.printStackTrace();
	}
	    return combinationlist;
  }
	
	public static void main(String []args) throws SQLException {
		Parsing parsing=new Parsing();
		//parsing.Parsing_Combination("J:\\ProgramFiles\\EclipseWorkplace\\WebCrawler\\filterednews\\201505010809.txt");
		String path="C:\\Users\\YI\\Desktop\\news企業別\\9984";
		File file=new File(path);
		File[] tempList=file.listFiles();
		getCombinationData me=new getCombinationData();
		batchtest insert=new batchtest();
		//List<String> list=me.getToDolist();
		//if (list!=null) {
			for(int i=0;i<tempList.length;i++){
			if(tempList[i].isFile()){
				String id=tempList[i].getPath().substring(33, 45);	//51,63	
				//if (list.contains(id)) {
					 List<String> combinationsFortext=new ArrayList<>();
					 combinationsFortext=parsing.Parsing_Combination(tempList[i].toString());
				     insert.insert(id, combinationsFortext);
				//}	         
			}
	     // }
		}
	}
	
}
