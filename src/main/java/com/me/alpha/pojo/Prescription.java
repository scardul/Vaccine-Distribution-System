package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Prescription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int prescriptionId;
	
	@ManyToOne
	@JoinColumn(name="vaccineId")
	Vaccine vaccine;
	
	int quantity;
	
	@ManyToOne
	@JoinColumn(name="userId")
	User doctor;
	
	@ManyToOne
	@JoinColumn(name="organizationId")
	Organization organization;
}