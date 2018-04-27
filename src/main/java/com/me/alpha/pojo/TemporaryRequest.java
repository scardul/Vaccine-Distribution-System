package com.me.alpha.pojo;

import java.util.ArrayList;
import java.util.List;

public class TemporaryRequest {
	
	public TemporaryRequest() {
		li = new ArrayList<>();
		quantity = 0;
	}
	
	Vaccine vaccine;
	int quantity;
	
	List<Integer> li;
	
	
	public void add(int e) {
		li.add(e);
	}
	
	
	
	public List<Integer> getLi() {
		return li;
	}



	public void setLi(List<Integer> li) {
		this.li = li;
	}



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
