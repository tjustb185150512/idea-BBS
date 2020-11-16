package test;


import java.sql.Connection;

import dao.impl.BaseDao;


public class dbtest extends BaseDao{
	
	public void Test(){
		try {
			Connection conn = null;
			conn = getConn();
			System.out.println(conn);
		} catch (Exception e) {
		}
	}	
}
