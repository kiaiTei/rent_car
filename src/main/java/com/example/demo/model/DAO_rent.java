package com.example.demo.model;
import java.sql.Date;
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
		
//		String sql = "insert into employeeinfo ( e_name , e_password ) values( ? , ? )" ; 
//		db.update( sql , ev.getName( ) , ev.getpw( ) ) ;
//		
		String sql =
				"INSERT INTO car_info ( plate_num, brand, model, seats, rent_price, status ) VALUES ( ?, ?, ?, ?, ?, ? )";

		
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
			
			String sql = "insert into car_info ( plate_num, brand, model, seats, price, status ) values( ? , ? , ? , ? , ? , ?)" ; 
			db.update( sql , ec.getPlate_num( ) , ec.getBrand( ) , ec.getModel( ) , ec.getSeats( ) , ec.getPrice( ) , ec.getStatus( ) ) ;
			
			
		}
	  
	  
	  
	  
	  public ArrayList < Entitycar > zenken_kensaku( ) {
			
			ArrayList < Entitycar > al = new ArrayList < Entitycar > ( ) ; 
			
			
			
			String sql = "select * from car_info" ; 
	        
	        List < Map < String , Object > > res = db.queryForList( sql ) ;
	        

	        
	        for (Map<String, Object> m : res) {
	            int i = (int) m.get("id");              
	            String p_n = (String) m.get("plate_num");
	            String b = (String) m.get("brand");
	            String mod = (String) m.get("model");
	            int s = (int) m.get("seats");
	            int r_p = (int) m.get("price");
	            String status = (String) m.get("status");

	            Entitycar ec_id = new Entitycar(i, p_n, b, mod, s, r_p, status);
	            al.add(ec_id);
	        }


	        return al ;
		}
	  
	  public Entitycar getCarById(int id) {
	      String sql = "SELECT * FROM car_info WHERE car_id = ?";
	      System.out.println(id);
	      Map<String, Object> m = db.queryForMap(sql, id);

	      return new Entitycar(
	          (int) m.get("car_id"),
	          (String) m.get("plate_num"),
	          (String) m.get("brand"),
	          (String) m.get("model"),
	          (int) m.get("seats"),
//	          (int) m.get("price"),
	          (int) m.get("rent_price"),

	          (String) m.get("status")
	      );
	  }

	  public void updateCar(int id, String brand, String model, int seats, int price, String status) {
//	      String sql = "UPDATE car_info SET brand=?, model=?, seats=?, price=?, status=? WHERE car_id=?";
		  String sql =
				  "UPDATE car_info SET brand=?, model=?, seats=?, rent_price=?, status=? WHERE car_id=?";

	      db.update(sql, brand, model, seats, price, status, id);
	  }
	  
	  
	  public int car_deleteById(int id) {
	        String sql = "DELETE FROM car_info WHERE car_id = ?";
	        return db.update(sql, id);
	    }
	  /***********************************************************************************

		
/***********************************************************************************/
	  
 
/**************************** RESERVE ******************************************/
	  public void res_touroku( Entityres er ) {
			
			String sql = "insert into rent_records ( customer_id, car_id,rent_start,rent_end, status ) values( ? , ? , ? , ? , ? )" ; 
			db.update( sql , er.getC_id( ) , er.getCar_id( ) , er.getRent_sDate( ) , er.getRent_eDate( ) , er.getStatus( ) ) ;	
		}
	  
	  
	  
	  public ArrayList < Entityres > res_zenken( ) {
			
			ArrayList < Entityres > al = new ArrayList < Entityres > ( ) ; 
			
			
			
			String sql = "select * from rent_records" ; 
	        
	        List < Map < String , Object > > res = db.queryForList( sql ) ;
	        

	        
	        for( Map < String , Object > m : res ) { 
	        	int res_id = ( int ) m.get( "rent_id" ) ;
	        	int c_i = ( int ) m.get( "customer_id" ) ;
	        	int car_i = ( int ) m.get( "car_id" ) ;
	        	Date s_d = (Date) m.get("rent_start");   // 获取 sql.Date
	        	Date e_d = (Date) m.get("rent_end"); 
	        	String status = (String) m.get( "status" );
	        	Entityres res_all = new Entityres(res_id, c_i ,car_i,s_d,e_d,status) ;
	        	al.add( res_all );
	        }

	        return al ;
		}
	  
	  public Entityres getOrderById(int id) {
	      String sql = "SELECT * FROM rent_records WHERE rent_id = ?";
	      System.out.println(id);
	      Map<String, Object> m = db.queryForMap(sql, id);

	      return new Entityres(
	    	  (int) m.get("rent_id"),  
	          (int) m.get("customer_id"),
	          (int) m.get("car_id"),
	          (Date)m.get( "rent_start" ),
	    	  (Date)m.get( "rent_end" ),
	    	  (String) m.get("status")
	      );
	  }
	  
	  
	  public void updateRes(int r_id, int c_id, int car_id, Date r_s, Date r_e, String status) {
	      String sql = "UPDATE rent_records SET customer_id=?, car_id=?, rent_start=?, rent_end=?, status=? WHERE rent_id=?";
	      db.update(sql, c_id, car_id, r_s,r_e, status, r_id);
	  }
	  
	  
	// 新增检查
	  public boolean isCarAvailable(int carId, Date startTime, Date endTime) {
	      String sql = "SELECT COUNT(*) FROM rent_records " +
	                   "WHERE car_id=? AND status='ongoing' " +
	                   "AND NOT (rent_end <= ? OR rent_start >= ?)";
	      Integer count = db.queryForObject(sql, Integer.class, carId, startTime, endTime);
	      return count == 0;
	  }

	  // 更新检查
	  public boolean isCarAvailableForUpdate(int resId, int carId, Date start, Date end) {
	      String sql = "SELECT COUNT(*) FROM rent_records " +
	                   "WHERE car_id=? AND status='ongoing' AND rent_id != ? " +
	                   "AND NOT (rent_end <= ? OR rent_start >= ?)";
	      Integer count = db.queryForObject(sql, Integer.class, carId, resId, start, end);
	      return count == 0;
	  }
	  
	  
	  public Entityrent_car getRentWithCarById(int rentId) {
		    String sql = "SELECT r.rent_id, r.customer_id, r.car_id, r.rent_start, r.rent_end, r.status AS rent_status, "
//		               + "c.plate_num, c.brand, c.model, c.seats, c.price, c.status AS car_status "
                       + "c.plate_num, c.brand, c.model, c.seats, c.rent_price, c.status AS car_status "
		               + "FROM rent_records r "
		               + "JOIN car_info c ON r.car_id = c.car_id "
		               + "WHERE r.rent_id = ?";
		    
		    return db.queryForObject(sql, new Object[]{rentId}, (rs, rowNum) -> {
	            Entityrent_car rwc = new Entityrent_car();
	            rwc.setRent_id(rs.getInt("rent_id"));
	            rwc.setCustomer_id(rs.getInt("customer_id"));
	            rwc.setCar_id(rs.getInt("car_id"));
	            rwc.setRent_start(rs.getDate("rent_start"));
	            rwc.setRent_end(rs.getDate("rent_end"));
	            rwc.setRent_status(rs.getString("rent_status"));

	            rwc.setPlate_num(rs.getString("plate_num"));
	            rwc.setBrand(rs.getString("brand"));
	            rwc.setModel(rs.getString("model"));
	            rwc.setSeats(rs.getInt("seats"));
//	            rwc.setPrice(rs.getInt("price"));
	            rwc.setPrice(rs.getInt("rent_price"));
	            rwc.setCar_status(rs.getString("car_status"));

	            return rwc;
	        });
		}
	  
	  public int deleteById(int id) {
	        String sql = "DELETE FROM rent_records WHERE rent_id = ?";
	        return db.update(sql, id);
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
