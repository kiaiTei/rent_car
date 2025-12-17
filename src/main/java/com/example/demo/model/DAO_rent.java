package com.example.demo.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DAO_rent {

    private final JdbcTemplate db;

    public DAO_rent(JdbcTemplate db) {
        this.db = db;
    }

    /* ===================== LOGIN ===================== */

    public String login(int id) {
        String sql = "SELECT e_password FROM employee_info WHERE employee_id = ?";
        List<Map<String, Object>> res = db.queryForList(sql, id);
        if (res.isEmpty()) return null;
        return (String) res.get(0).get("e_password");
    }

    /* ===================== CAR REGISTER ===================== */

    public void car_touroku(Entitycar ec) {
        String sql =
            "INSERT INTO car_info (plate_num, brand, model, seats, price, status) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        db.update(sql,
            ec.getPlate_num(),
            ec.getBrand(),
            ec.getModel(),
            ec.getSeats(),
            ec.getPrice(),
            ec.getStatus()
        );
    }

    /* ===================== CAR LIST ===================== */

    public ArrayList<Entitycar> zenken_kensaku() {
        ArrayList<Entitycar> list = new ArrayList<>();
        String sql = "SELECT * FROM car_info";
        List<Map<String, Object>> res = db.queryForList(sql);

        for (Map<String, Object> m : res) {
            Entitycar ec = new Entitycar(
                (int) m.get("id"),
                (String) m.get("plate_num"),
                (String) m.get("brand"),
                (String) m.get("model"),
                (int) m.get("seats"),
                (int) m.get("price"),
                (String) m.get("status")
            );
            list.add(ec);
        }
        return list;
    }

    /* ===================== CAR DETAIL ===================== */

    public Entitycar getCarById(int id) {
        String sql = "SELECT * FROM car_info WHERE id = ?";
        Map<String, Object> m = db.queryForMap(sql, id);

        return new Entitycar(
            (int) m.get("id"),
            (String) m.get("plate_num"),
            (String) m.get("brand"),
            (String) m.get("model"),
            (int) m.get("seats"),
            (int) m.get("price"),
            (String) m.get("status")
        );
    }

    /* ===================== CAR UPDATE ===================== */

    public void updateCar(int id, String brand, String model, int seats, int price, String status) {
        String sql =
            "UPDATE car_info SET brand=?, model=?, seats=?, price=?, status=? WHERE id=?";
        db.update(sql, brand, model, seats, price, status, id);
    }

    /* ===================== CAR DELETE ===================== */

    public int car_deleteById(int id) {
        String sql = "DELETE FROM car_info WHERE id = ?";
        return db.update(sql, id);
    }

    /* ===================== RESERVE ===================== */

    public void res_touroku(Entityres er) {
        String sql =
            "INSERT INTO rent_records (customer_id, car_id, rent_start, rent_end, status) " +
            "VALUES (?, ?, ?, ?, ?)";
        db.update(sql,
            er.getC_id(),
            er.getCar_id(),
            er.getRent_sDate(),
            er.getRent_eDate(),
            er.getStatus()
        );
    }

    public ArrayList<Entityres> res_zenken() {
        ArrayList<Entityres> list = new ArrayList<>();
        String sql = "SELECT * FROM rent_records";
        List<Map<String, Object>> res = db.queryForList(sql);

        for (Map<String, Object> m : res) {
            list.add(new Entityres(
                (int) m.get("rent_id"),
                (int) m.get("customer_id"),
                (int) m.get("car_id"),
                (Date) m.get("rent_start"),
                (Date) m.get("rent_end"),
                (String) m.get("status")
            ));
        }
        return list;
    }

    /* ===================== JOIN ===================== */

    public Entityrent_car getRentWithCarById(int rentId) {
        String sql =
            "SELECT r.rent_id, r.customer_id, r.car_id, r.rent_start, r.rent_end, r.status AS rent_status, " +
            "c.plate_num, c.brand, c.model, c.seats, c.price, c.status AS car_status " +
            "FROM rent_records r " +
            "JOIN car_info c ON r.car_id = c.id " +
            "WHERE r.rent_id = ?";

        return db.queryForObject(sql, new Object[]{rentId}, (rs, rowNum) -> {
            Entityrent_car e = new Entityrent_car();
            e.setRent_id(rs.getInt("rent_id"));
            e.setCustomer_id(rs.getInt("customer_id"));
            e.setCar_id(rs.getInt("car_id"));
            e.setRent_start(rs.getDate("rent_start"));
            e.setRent_end(rs.getDate("rent_end"));
            e.setRent_status(rs.getString("rent_status"));
            e.setPlate_num(rs.getString("plate_num"));
            e.setBrand(rs.getString("brand"));
            e.setModel(rs.getString("model"));
            e.setSeats(rs.getInt("seats"));
            e.setPrice(rs.getInt("price"));
            e.setCar_status(rs.getString("car_status"));
            return e;
        });
    }
}
