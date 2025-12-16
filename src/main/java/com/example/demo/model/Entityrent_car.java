package com.example.demo.model;

import java.sql.Date;

public class Entityrent_car {
	private int rent_id;
    private int customer_id;
    private int car_id;
    private Date rent_start;
    private Date rent_end;
    private String rent_status;

    // 车辆信息
    private String plate_num;
    private String brand;
    private String model;
    private int seats;
    private int price;
    private String car_status;
	public int getRent_id() {
		return rent_id;
	}
	public void setRent_id(int rent_id) {
		this.rent_id = rent_id;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getCar_id() {
		return car_id;
	}
	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}
	public Date getRent_start() {
		return rent_start;
	}
	public void setRent_start(Date rent_start) {
		this.rent_start = rent_start;
	}
	public Date getRent_end() {
		return rent_end;
	}
	public void setRent_end(Date rent_end) {
		this.rent_end = rent_end;
	}
	public String getRent_status() {
		return rent_status;
	}
	public void setRent_status(String rent_status) {
		this.rent_status = rent_status;
	}
	public String getPlate_num() {
		return plate_num;
	}
	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCar_status() {
		return car_status;
	}
	public void setCar_status(String car_status) {
		this.car_status = car_status;
	}

}
