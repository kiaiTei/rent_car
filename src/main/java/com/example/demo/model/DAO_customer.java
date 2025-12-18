package com.example.demo.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
@Repository
public class DAO_customer {

    @Autowired
    private JdbcTemplate db;
    

    public JdbcTemplate getDb() {
		return db;
	}

	public void setDb(JdbcTemplate db) {
		this.db = db;
	}
	
	
	public String cus_login(int id) {
		  String sql = "SELECT password FROM customer_info WHERE customer_id = " + id;

		    List<Map<String, Object>> res = db.queryForList(sql);

		    if (res.isEmpty()) {
		        // ID不存在，返回 null 或其他标识
		        return null;
		    }

		    Map<String, Object> m = res.get(0);
		    
		    return (String) m.get("password");
	    }
	
	public  Entitycostmer findById(int customerId) {

	    String sql = "SELECT * FROM customer_info WHERE customer_id = ?";

	    Map<String, Object> m = db.queryForMap(sql, customerId);

	    int id = (int) m.get("customer_id");
	    String name = (String) m.get("name");
	    String phone = (String) m.get("phone");
	    String email = (String) m.get("email");
	    String address = (String) m.get("address");
	    String password = (String) m.get("password");

	    return new Entitycostmer(id, name, phone, email, address,password);
	}
	
	
	public  Entitycostmer findByCustomerId(int customerId) {

	    String sql = "SELECT * FROM customer_info WHERE customer_id = ?";

	    Map<String, Object> m = db.queryForMap(sql, customerId);

	    int id = (int) m.get("customer_id");
	    String name = (String) m.get("name");
	    String phone = (String) m.get("phone");
	    String email = (String) m.get("email");
	    String address = (String) m.get("address");
	    String password = (String) m.get("password");

	    return new Entitycostmer(id, name, phone, email, address,password);
	}

	


	// 全件検索（zenken_kensaku 相当）
    public ArrayList<Entitycostmer> zenken_kensaku() {
        ArrayList<Entitycostmer> list = new ArrayList<>();

        String sql = "SELECT * FROM customer_info";
        List<Map<String, Object>> result = db.queryForList(sql);

        for (Map<String, Object> m : result) {
            int id = (int) m.get("customer_id");
            String name = (String) m.get("name");
            String phone = (String) m.get("phone"); 
            String email = (String) m.get("email");
            String address = (String) m.get("address");
            String password = (String) m.get("password");

            Entitycostmer c = new Entitycostmer(id, name, phone, email, address,password);
            list.add(c);
        }
        return list;
    }

    // 追加
//    public void insert(Entitycostmer customer) {
//
//        String sql = """
//            INSERT INTO customer_info
//            (name, phone, email, address, password)
//            VALUES (?, ?, ?, ?,?)
//            """;
//
//        db.update(sql,
//                customer.getName(),
//                customer.getPhone(),
//                customer.getEmail(),
//                customer.getAddress(),
//                customer.getPassword());
//    }
    
    public void insert(Entitycostmer customer) {
        String sql = "INSERT INTO customer_info(name, phone, email, address, password) VALUES(?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getPassword());
            return ps;
        }, keyHolder);

        customer.setCustomerId(keyHolder.getKey().intValue());
    }


    // 削除
    public void delete(int customerId) {
        String sql = "DELETE FROM customer_info WHERE customer_id = ?";
        db.update(sql, customerId);
    }
    
    
    
    public Entitycostmer getCusById(int id) {
	      String sql = "SELECT * FROM customer_info WHERE customer_id = ?";
	      System.out.println(id);
	      Map<String, Object> m = db.queryForMap(sql, id);

	      return new Entitycostmer(
	          (int) m.get("customer_id"),
	          (String) m.get("name"),
	          (String) m.get("phone"),
	          (String) m.get("email"),
	          (String) m.get("address"),
	          (String) m.get("password")
	      );
	  }

    
    
 // 更新
//    public void update(Entitycostmer customer) {
//
//        String sql = """
//            UPDATE customer_info
//            SET name = ?,
//                phone = ?,
//                email = ?,
//                address = ?,
//                password = ?
//            WHERE customer_id = ?
//            """;
//
//        db.update(sql,
//                customer.getName(),
//                customer.getPhone(),
//                customer.getEmail(),
//                customer.getAddress(),
//                customer.getPassword(),
//                customer.getCustomerId());
//    }
    
    public void update(int id, String name, String phone, String email, String address, String password) {
	      String sql = "UPDATE customer_info SET name = ?,phone = ?,email = ?,address = ?,password = ? WHERE customer_id = ?";
	      db.update(sql, name, phone, email, address, password, id);
	  }
    
    public int cus_deleteById(int id) {
        String sql = "DELETE FROM customer_info WHERE customer_id = ?";
        return db.update(sql, id);
    }
    
    
    public List<Entityres> findReservationsByCustomerId(int c_id) {
        String sql = "SELECT * FROM rent_records WHERE customer_id=? ORDER BY rent_start DESC";
        return db.query(sql, new Object[]{c_id}, (rs, rowNum) ->
                new Entityres(
                        rs.getInt("rent_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("car_id"),
                        rs.getDate("rent_start"),
                        rs.getDate("rent_end"),
                        rs.getString("status")
                )
        );
    }

    // 新增预约
    public int insertReservation(Entityres res) {
        String sql = "INSERT INTO rent_records(customer_id, car_id, rent_start, rent_end, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        return db.update(sql,
                res.getC_id(),
                res.getCar_id(),
                res.getRent_sDate(),
                res.getRent_eDate(),
                res.getStatus()
        );
    }

    // 检查车辆时间段是否可用
    public boolean isCarAvailable(int car_id, Date start, Date end) {
        String sql = "SELECT COUNT(*) FROM rent_records WHERE car_id=? AND NOT (rent_end < ? OR rent_start > ?)";
        Integer count = db.queryForObject(sql, Integer.class, car_id, start, end);
        return count != null && count == 0;
    }
    
    public void updateResStatus(int resId, String status) {
        String sql = "UPDATE rent_records SET status = ? WHERE rent_id = ?";
        db.update(sql, status, resId);
    }

}
