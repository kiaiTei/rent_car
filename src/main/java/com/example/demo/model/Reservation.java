package com.example.demo.model;

import java.sql.Date;

public class Reservation {
	 private int reservationId;
	    private int customerId;
	    private int carId;
	    private Date rentStartDate;
	    private Date rentEndDate;
	    private String status;

	    // Constructor
	    public Reservation(int reservationId, int customerId, int carId,
	                       Date rentStartDate, Date rentEndDate, String status) {
	        this.reservationId = reservationId;
	        this.customerId = customerId;
	        this.carId = carId;
	        this.rentStartDate = rentStartDate;
	        this.rentEndDate = rentEndDate;
	        this.status = status;
	    }

	    // Getter & Setter
	    public int getReservationId() { return reservationId; }
	    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
	    public int getCustomerId() { return customerId; }
	    public void setCustomerId(int customerId) { this.customerId = customerId; }
	    public int getCarId() { return carId; }
	    public void setCarId(int carId) { this.carId = carId; }
	    public Date getRentStartDate() { return rentStartDate; }
	    public void setRentStartDate(Date rentStartDate) { this.rentStartDate = rentStartDate; }
	    public Date getRentEndDate() { return rentEndDate; }
	    public void setRentEndDate(Date rentEndDate) { this.rentEndDate = rentEndDate; }
	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }

}
