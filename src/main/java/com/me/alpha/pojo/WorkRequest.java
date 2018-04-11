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
	
	@ManyToOne
	@JoinColumn(name="vaccineId")
	Vaccine vaccine;
	
	int quantity;
	
	@ManyToOne
	@JoinColumn(name="organizationId")
	Organization senderOrganization;
	
	@ManyToOne
	@JoinColumn(name="enterpriseId",insertable=false,updatable=false)
	Enterprise senderEnterprise;
	
	@ManyToOne
	@JoinColumn(name="enterpriseId",insertable=false,updatable=false)
	Enterprise receiverEnterprise;
	
	String status;
	
	@OneToOne
	@JoinColumn(name="workRequestId")
	WorkRequest workRequest;
}
