package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Inventory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int inventoryId;
	
	@ManyToOne
	@JoinColumn(name="vaccineId",nullable=false)
	Vaccine vaccine;
	
	int quantity;
	
	@ManyToOne
	@JoinColumn(name="organizationId",nullable=false)
	Organization organization;
}
