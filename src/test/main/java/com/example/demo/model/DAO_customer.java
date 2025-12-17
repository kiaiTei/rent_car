package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

	    return new Entitycostmer(id, name, phone, email, address);
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

            Entitycostmer c = new Entitycostmer(id, name, phone, email, address);
            list.add(c);
        }
        return list;
    }

    // 追加
    public void insert(Entitycostmer customer) {

        String sql = """
            INSERT INTO customer_info
            (name, phone, email, address)
            VALUES (?, ?, ?, ?)
            """;

        db.update(sql,
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress());
    }


    // 削除
    public void delete(int customerId) {
        String sql = "DELETE FROM customer_info WHERE customer_id = ?";
        db.update(sql, customerId);
    }
 // 更新
    public void update(Entitycostmer customer) {

        String sql = """
            UPDATE customer_info
            SET name = ?,
                phone = ?,
                email = ?,
                address = ?
            WHERE customer_id = ?
            """;

        db.update(sql,
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCustomerId());
    }



}
