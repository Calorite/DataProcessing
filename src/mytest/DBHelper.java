package mytest;


import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.util.List;  



public class DBHelper {

     
       public Connection conn=null;
       public PreparedStatement statement=null;
       
       void connSQL(){
            String url="jdbc:mysql://127.0.0.1/classdata?characterEncoding=utf8&useSSL=true";  
            String username="root";
            String password="474950494";
            String name="com.mysql.jdbc.Driver";

            try{
            	Class.forName(name);
            	conn=DriverManager.getConnection(url, username, password);
            }catch(ClassNotFoundException ex){
            	ex.printStackTrace();
            }
            catch(SQLException sql){
            	sql.printStackTrace();
            }
       }
       
       void deconnSQL(){
    	   try{
    		   if(conn!=null)
    			   conn.close();
    	   }catch(Exception e){
    		   e.printStackTrace();
    	   }
       }
   
       ResultSet selectSQL(String sql){
    	   ResultSet rs=null;
    	   try{
    		   statement=conn.prepareStatement(sql);
    		   rs=statement.executeQuery(sql);
    	   }catch(SQLException e){
    		   e.printStackTrace();
    	   }
    	   return rs;
       }
       
       
       
       
       boolean insertSQL(String sql){
    	   try{
    		   statement=conn.prepareStatement(sql);
    		   statement.executeUpdate();
    		   return true;
    	   }catch(SQLException e){
    		   System.out.println("connect DB fail!");
    		   e.printStackTrace();
    	   }catch(Exception e){
    		   System.out.println("insert data fail!");
    		   e.printStackTrace();
    	   }
    	   return false;
    	      	   
       }
       
       //execute delete language
       boolean deleteSQL(String sql){
    	   try{
    		   statement=conn.prepareStatement(sql);
    		   statement.executeUpdate();
    		   return true;
    	   }catch(SQLException e){
          System.out.println("connect DB fail!");
          e.printStackTrace();
       }catch(Exception e){
    	   System.out.println("delete data fail!");
    	   e.printStackTrace();
    	   }
    	   return false;
       }
       
       boolean updateSQL(String sql){
    	   try{
    		   statement=conn.prepareStatement(sql);
    		   statement.executeUpdate();
    		   return true;
    	   }catch(SQLException e){
    		   System.out.println("connect DB fail!");
    		   e.printStackTrace();
    	   }
    	   catch(Exception e){
    		   System.out.println("update data fail!");
    		   e.printStackTrace();
    	   }
    	   return false;
       }
       
       public void doStore(String id,List<String> list) throws SQLException {            
           int count = 0;
           String insert_sql = "INSERT INTO stockdb.test_newsdata (id,combination,textid) VALUES (?,?,?)";
           //Connection connection=new getConnection();
           try{
    		    statement=conn.prepareStatement(insert_sql);
           //PreparedStatement psts = conn.prepareStatement(insert_sql); 
           if (list!=null) {
			for (String combination:list) {                  
				statement.setString(1, "null");  
				statement.setString(2, id);  
				statement.setString(3, combination);  
				statement.addBatch();           
               count++;              
           }  
			statement.executeBatch();  
           conn.commit();   
           System.out.println("All down : " + count);  
           conn.close();  
           }
    	   }catch(SQLException e){
    		   System.out.println("connect DB fail!");
    		   e.printStackTrace();
    	   }
          
		}
           
          
       
       
       
       void layoutStyle2(ResultSet rs){
   
           try{
        	   while(rs.next()){
        		  // System.out.println(rs.getInt("ID")+"/t/t"+rs.getInt("stockCode")+"/t/t"+rs.getString("date")+"/t/t"+rs.getString("open")+"/t/t"+rs.getString("high")+"/t/t"+rs.getString("low")+"/t/t"+rs.getString("close"));
        	   }
           }catch(SQLException e){
        	   System.out.println("printout data fail");
        	   e.printStackTrace();
           }catch(Exception e){
        	   System.out.println("display fail");
        	   e.printStackTrace();
           }
       }
       
        int GetMaxRows(String sql){
    	  int max = 0;
    	   try{
    		   statement=conn.prepareStatement(sql);
    		   max=statement.getMaxRows();
    	   }catch(SQLException e){
    		   e.printStackTrace();
    	   }
    	   return max;
       }
        
}

