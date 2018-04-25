package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InternalRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int internalRequestId;
	
	@ManyToOne
	@JoinColumn(name="vaccineId",nullable=false)
	Vaccine vaccine;
	
	int quantity;
	
	@ManyToOne
	@JoinColumn(name="userId",nullable=false)
	User user;
	
	String status;
	
	@ManyToOne
	@JoinColumn(name="organizationId",nullable=false)
	Organization organization;

	public int getInternalRequestId() {
		return internalRequestId;
	}

	public void setInternalRequestId(int internalRequestId) {
		this.internalRequestId = internalRequestId;
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
		this.quantity = quantity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
