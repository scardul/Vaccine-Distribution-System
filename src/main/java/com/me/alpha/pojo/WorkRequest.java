package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class WorkRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int workRequestId;
	
	public int getWorkRequestId() {
		return workRequestId;
	}

	public void setWorkRequestId(int workRequestId) {
		this.workRequestId = workRequestId;
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

	public Organization getSenderOrganization() {
		return senderOrganization;
	}

	public void setSenderOrganization(Organization senderOrganization) {
		this.senderOrganization = senderOrganization;
	}

	public Enterprise getSenderEnterprise() {
		return senderEnterprise;
	}

	public void setSenderEnterprise(Enterprise senderEnterprise) {
		this.senderEnterprise = senderEnterprise;
	}

	public Enterprise getReceiverEnterprise() {
		return receiverEnterprise;
	}

	public void setReceiverEnterprise(Enterprise receiverEnterprise) {
		this.receiverEnterprise = receiverEnterprise;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WorkRequest getWorkRequest() {
		return workRequest;
	}

	public void setWorkRequest(WorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	@ManyToOne
	@JoinColumn(name="vaccineId")
	Vaccine vaccine;
	
	int quantity;
	
	@ManyToOne
	@JoinColumn(name="organizationId")
	Organization senderOrganization;
	
	@ManyToOne
	@JoinColumn(name="senderEnterprise")
	Enterprise senderEnterprise;
	
	@ManyToOne
	@JoinColumn(name="receiverEnterprise")
	Enterprise receiverEnterprise;
	
	String status;
	
	@OneToOne
	@JoinColumn(name="RefId",nullable=true)
	WorkRequest workRequest;
}