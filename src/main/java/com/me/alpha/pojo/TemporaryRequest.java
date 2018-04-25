package com.me.alpha.pojo;

public class TemporaryRequest {
	
	public TemporaryRequest() {
		quantity = 0;
	}
	
	Vaccine vaccine;
	int quantity;
	
	public Vaccine getVaccine() {
		return vaccine;
	}
	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity += quantity;
	}
	
	

}
