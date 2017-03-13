package mytest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class batchtest {
	
	public void insert(String id,List<String> list) throws SQLException{
		int count = 0;
     String insert_sql = "INSERT INTO stockdb.combination_data (combination,textid) VALUES (?,?)";
     String url="jdbc:mysql://127.0.0.1/classdata?characterEncoding=utf8&useSSL=true";
     String username="root";
     String password="474950494";
     Connection connection=java.sql.DriverManager.getConnection(url,username,password);
     connection.setAutoCommit(false);
     try{
    	 PreparedStatement statement=connection.prepareStatement(insert_sql);
     //PreparedStatement psts = conn.prepareStatement(insert_sql); 
     if (list!=null) {
		for (String combination:list) {                  
			//statement.setString(1, null);  
			statement.setString(1, combination);  
			statement.setString(2, id);  
			statement.addBatch();           
         count++;              
     }  
		statement.executeBatch();  
     connection.commit();   
     System.out.println("All down : " + count);  
     connection.close();  
     }
	   }catch(SQLException e){
		   System.out.println("connect DB fail!");
		   e.printStackTrace();
	   }
	}
	
	 
}
