package com.example.demo.model;
import java.sql.Date;

public class Entityres {

		private int res_id;	
		private int c_id;
	    private int car_id;
	    private Date rent_sDate;
	    private Date rent_eDate;
	    private String status;
	  
	    public Entityres() {
	    	
	    } 
	    
	    public Entityres(int c_id, int car_id, Date rent_sDate, Date rent_eDate, String status) {
	        this.setC_id(c_id);
	        this.setCar_id(car_id);
	        this.setRent_sDate(rent_sDate);
	        this.setRent_eDate(rent_eDate);
	        this.setStatus(status);
	    }
	    
	    public Entityres(int res_id, int c_id, int car_id, Date rent_sDate, Date rent_eDate, String status) {
	    	this.setRes_id(res_id);
	    	this.setC_id(c_id);
	        this.setCar_id(car_id);
	        this.setRent_sDate(rent_sDate);
	        this.setRent_eDate(rent_eDate);
	        this.setStatus(status);
	    }
	    
	    
			public int getC_id() {
				return c_id;
			}

			public void setC_id(int c_id) {
				this.c_id = c_id;
			}

			public int getCar_id() {
				return car_id;
			}

			public void setCar_id(int car_id) {
				this.car_id = car_id;
			}

			public Date getRent_sDate() {
				return rent_sDate;
			}

			public void setRent_sDate(Date rent_sDate) {
				this.rent_sDate = rent_sDate;
			}

			public Date getRent_eDate() {
				return rent_eDate;
			}

			public void setRent_eDate(Date rent_eDate) {
				this.rent_eDate = rent_eDate;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public int getRes_id() {
				return res_id;
			}

			public void setRes_id(int res_id) {
				this.res_id = res_id;
			}


	}