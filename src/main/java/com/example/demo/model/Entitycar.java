package com.example.demo.model;

public class Entitycar {
    
    private String plate_num;
    private String brand;
    private String model;
    private int seats;
    private int rent_price;
    private String status;
    private int id;

    
    
    
    
    public Entitycar() {
    	
    } 
    
    public Entitycar(String plate_num, String brand, String model, int seats, int rent_price, String status) {
    	this.setPlate_num(plate_num);
		this.setBrand(brand);
		this.setModel(model);
		this.setSeats(seats);
		this.setPrice(rent_price);
		this.setStatus(status);
		
		
	}
    
    public int getPrice() {
		return rent_price;
	}

	public void setPrice(int rent_price) {
		this.rent_price = rent_price;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    


	    public Entitycar(int id, String plateNum, String brand, String model, int seats, int rentPrice, String status) {
	        this.setId(id);
	        this.plate_num = plateNum;
	        this.brand = brand;
	        this.model = model;
	        this.seats = seats;
	        this.rent_price = rentPrice;
	        this.status = status;
	    }

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
    

}