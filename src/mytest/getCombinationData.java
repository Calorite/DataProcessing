package mytest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getCombinationData {
	
	List<String> combinations=new ArrayList<>();
	
	public  void combiantion(String chs[],String textid){  
        if(chs==null||chs.length==0){  
            return ;  
        }  
        List<String> list=new ArrayList();  
        combine(chs,0,2,list,textid);  
         
   }  
   public void combine(String []cs,int begin,int number,List<String> list,String textid){  
       getCombinationData insert=new getCombinationData();
	   if(number==0){  
           System.out.println(textid+"   "+list.toString());
           
           
        	   combinations.add(list.toString());
           
           
           return ;  
       }  
       if(begin==cs.length){  
           return;  
       }  
       list.add(cs[begin]);  
       combine(cs,begin+1,number-1,list,textid);  
       list.remove((String)cs[begin]);  
       combine(cs,begin+1,number,list,textid);  
   }  
	
	
	public boolean guyoumingci(String s) {
		boolean f=false;
		Pattern pattern=Pattern.compile("名詞-固有名詞(.)*");
		Matcher matcher=pattern.matcher(s);
		if (matcher.find()) {
			f=true;
		}		
		return f;
	}
	
	public String getTextID(String s) {
		String f=null;
		Pattern pattern=Pattern.compile("\\d{12}");
		Matcher matcher=pattern.matcher(s);
		if (matcher.find()) {
			f=matcher.group(0);
		}		
		return f;
	}
	
	public boolean japanese(String word) throws UnsupportedEncodingException {
		if(word.getBytes("shift-jis").length>=(2*word.length())){
			  return true;
		  }else 
			  return false;		
	}
	
	public WordCounter getWordCounter(String s) throws ClassNotFoundException, SQLException, IOException{
		String text ="";  
		WordCounter wc =new WordCounter();
		
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(s),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				text+=line;
			}
			read.close();			
			 Process process = Runtime.getRuntime().exec(new String[] {"I:\\Applications\\Tools\\MeCab\\bin\\mecab.exe", "-Ochasen"});
				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						process.getOutputStream())));
			//	OutputStreamWriter pw=null;
				
				String[] lines=text.split("\\。");
				// 入力文章を１行ずつ MeCab に送って形態素解析させる
				List<String> combinationlist=new ArrayList<>();
				for (String line1 : lines) {
					pw.println(line1);  // MeCab へ送信
					pw.flush();
					List<String> wordslist=new ArrayList<>();
					getCombinationData me=new getCombinationData();
					String line2;
					while ((line2 = br1.readLine()) != null) {  
						if (line2.equals("EOS")) {  // EOS は解析結果の終了を表す
							break;
						}
					Morpheme m=new Morpheme();	
	                String[] split =line2.split("\t",6);        
					m.a=split[0];
					m.yomi=split[1];
					m.kihonkei=split[2];
					m.hinshi=split[3];
					   					
					if(m.a.equals("、")||m.a.equals("。")||m.a.equals("null")||m.a.equals("<")||m.a.equals(">")||m.a.equals("[")||m.a.equals("）]")||m.a.equals("する")||m.a.equals("VIP")||m.a.equals("倶楽部")||m.a.equals("-")||m.a.equals(".")||m.a.equals("し")||m.a.equals("%")||m.a.equals("さ")||m.a.equals("(")||m.a.equals(")")||m.a.equals("%、")||m.a.equals("ログイン")){
					
					}else if (m.hinshi.equals("名詞-一般")||me.guyoumingci(m.hinshi)||m.hinshi.equals("名詞-サ変接続")||m.hinshi.equals("動詞-自立")||m.hinshi.equals("形容詞-自立")||m.hinshi.equals("名詞-形容動詞語幹")) {
						if (me.japanese(m.a)) {
							if (m.hinshi.equals("動詞-自立")) {
							wc.add(m);
						    wordslist.add(m.kihonkei);
						}else {
							wc.add(m);
						    wordslist.add(m.a);
						}
					  }												
					}
						
					
				  }
					String[] words= new String[wordslist.size()];
					int i=0;
					for(String w:wordslist){
						
						words[i]=w;
						i++;
					}
					combiantion(words,getTextID(s));
				}
				
				batchtest b=new batchtest();
				b.insert(getTextID(s), combinations);
				combinations.clear();			
				br1.close();			
				pw.close();
				process.destroy();
				
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return wc;
	}
	
	public List<String> getToDolist() throws SQLException {
		List<String> list=new ArrayList<>();
		DBHelper helper=new DBHelper();
		helper.connSQL();
		ResultSet rs=null;
		String sql="SELECT * FROM stockdb.learning_data;";//SELECT * FROM stockdb.learningdata where label<0;
		rs=helper.selectSQL(sql);
		
		while (rs.next()) {
		   getCombinationData t2=new getCombinationData();
		   String textid=t2.getTextID(rs.getString("newsid"));
		   list.add(textid);	
		}
		helper.deconnSQL();
		return list;
	}
	
	private static List<String> InsertedTextid() throws SQLException {
		List<String> list =new ArrayList<>();
		DBHelper helper=new DBHelper();
		String sql="SELECT distinct(textid) FROM stockdb.combination_data;";
		helper.connSQL();
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				String id=rs.getString("textid");
				list.add(id);
			}
		}
		helper.deconnSQL();
		return list;
	}
	
	
	public static void main(String []args) throws SQLException, ClassNotFoundException, IOException{
		String path="C:\\Users\\YI\\Desktop\\実験データ\\trainingNews";
		File file=new File(path);
		File[] tempList=file.listFiles();
		getCombinationData me=new getCombinationData();
	   List<String> list=me.getToDolist();
       List<String> Insertedlist=new ArrayList<>();
       Insertedlist=InsertedTextid();
			for(int i=0;i<tempList.length;i++){
			
				if (tempList[i].isFile()) {
					String textid=tempList[i].getPath().substring(39, 51);
					if (list.contains(textid)) {
						if (Insertedlist.contains(textid)) {
						
					}else{
						me.getWordCounter(tempList[i].toString());
					}
					}
										
				}
				
				
			}
			
	} 
}

