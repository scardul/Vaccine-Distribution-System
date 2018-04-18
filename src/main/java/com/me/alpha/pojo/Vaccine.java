package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Vaccine {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int vaccineId;
	
	String vaccineName;
	
	@ManyToOne
	@JoinColumn(name="diseaseId",nullable=false)
	Disease prevents;
	
	@ManyToOne
	@JoinColumn(name="manufacturerId",nullable=false)
	Manufacturer manufacturer;

	public int getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(int vaccineId) {
		this.vaccineId = vaccineId;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public Disease getPrevents() {
		return prevents;
	}

	public void setPrevents(Disease prevents) {
		this.prevents = prevents;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	
}
