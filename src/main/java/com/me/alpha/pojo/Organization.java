package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Organization {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int organizationId;
	
	public enum OrganizationType{
		DISTRIBUTER,PROVIDER,CLINIC
	}
	
	@Enumerated(EnumType.STRING)
	OrganizationType organizationType;
	
	public int getOrganizationId() {
		return organizationId;
	}

	public OrganizationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	String organizationName;
	
	@ManyToOne
	@JoinColumn(name="enterpriseId",nullable=false)
	Enterprise enterprise;
	
}
