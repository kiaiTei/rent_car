package com.example.demo.model;

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

	


}
