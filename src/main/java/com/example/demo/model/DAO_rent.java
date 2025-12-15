package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


// リポジトリはDAOの事。
@Repository
public class DAO_rent {
	
	
	// データベースへの接続を全部自動で行うクラス。
	private JdbcTemplate db;
	
	public DAO_rent( JdbcTemplate db ) {
		this.db = db ;
	}
	
	
	
	public void touroku( Entityrent ev ) {
		
		String sql = "insert into employeeinfo ( e_name , e_password ) values( ? , ? )" ; 
		db.update( sql , ev.getName( ) , ev.getpw( ) ) ;
		
		
	}
	/******************************* LOGIN ************************************/
	  public String login(int id) {
		  String sql = "SELECT e_password FROM employee_info WHERE employee_id = " + id;

		    List<Map<String, Object>> res = db.queryForList(sql);

		    if (res.isEmpty()) {
		        // ID不存在，返回 null 或其他标识
		        return null;
		    }

		    Map<String, Object> m = res.get(0);
		    
		    return (String) m.get("e_password");
	    }
	
/***********************************************************************************/
	  
	  
/******************** SHOW CAR ************************************************/
	  
	  
/***********************************************************************************/
	  
	  
/******************** INPUT CAR ************************************************/
	 /* public void car_touroku( String plate_num, String brand, String model, int seats, int rent_price, String status ) {
			
			String sql = "insert into car_info ( plate_num,brand,model,seats,rent_price,status) values( ? , ?,?,?,?,? )" ; 
			db.update( sql , plate_num ,brand,seats,rent_price,status ) ;
			
			
		}
	  */
	  
	  public void car_touroku( Entitycar ec ) {
			
			String sql = "insert into car_info ( plate_num, brand, model, seats, rent_price, status ) values( ? , ? , ? , ? , ? , ?)" ; 
			db.update( sql , ec.getPlate_num( ) , ec.getBrand( ) , ec.getModel( ) , ec.getSeats( ) , ec.getPrice( ) , ec.getStatus( ) ) ;
			
			
		}
	  
	  
	  
	  
	  public ArrayList < Entitycar > zenken_kensaku( ) {
			
			ArrayList < Entitycar > al = new ArrayList < Entitycar > ( ) ; 
			
			
			
			String sql = "select * from car_info" ; 
	        
	        List < Map < String , Object > > res = db.queryForList( sql ) ;
	        

	        
	        for( Map < String , Object > m : res ) {        	
	        	int i = ( int ) m.get( "id" ) ;
	        	String n = ( String ) m.get( "name" ) ;
	        	String p = (String) m.get( "password" );
	        	Entityrent ev = new Entityrent( i , n , p ) ;
	        	al.add( ev );
	        }

	        return al ;
		}
		
/***********************************************************************************/
	  public Entityrent selectOne(int id) {
		    
		    String sql = "SELECT * FROM emplyee_info WHERE id = " + id;
		    

		    List<Map<String, Object>> res = db.queryForList(sql);
		    
		    if (res.isEmpty()) {
		        return null;
		    }
		    
		    Map<String, Object> m = res.get(0);
		    int i = (int) m.get("id");
		    String n = (String) m.get("name");
		    String p = (String) m.get("password");
		    Entityrent ev = new Entityrent(i, n, p);
		    return ev;
		}

	  
	  
	  
	
	public void deleteVegetable(String id) {
	    String sql = "DELETE FROM vegetable WHERE id = ?";
	    System.out.println("id:"+id);
	    int rowsAffected = db.update(sql, id); 
	    System.out.println("Rows deleted: " + rowsAffected);
	}
	
	
	public void updateVegetable(int id, String name, int price) {
		String sql = "UPDATE vegetable SET vegetable_name = ?, price = ? WHERE id = ?";
	    int rowsAffected = db.update(sql, id, name, price);
	    System.out.println("Rows updated: " + rowsAffected);
	}

}
