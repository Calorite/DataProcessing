package mytest;

public class combination {
 
	public static void main(String []args){
		DBHelper helper=new DBHelper();
		helper.connSQL();
		String sql="insert into stockdb.combination select combination from stockdb.combination_chi_02 order by stockdb.combination_chi_02.chi desc limit 30000;";
		if(helper.insertSQL(sql)){
			System.out.println("inserted!");
		}else{
			System.out.println("failed!");
		}
	}
}
