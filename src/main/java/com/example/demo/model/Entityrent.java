package com.example.demo.model;

    
    
public class Entityrent {
    
    private int id;
    private String name;
    private int price;
    private String password;

    
    
    
    
    public Entityrent() {
    	
    } 
    

    public Entityrent(int id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}




	// getter / setter
    public int getId() {
        return id;
    }
    public String getpw() {
        return password;
    }
    
    
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setVegetableName(String vegetableName) {
        this.name = vegetableName;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}