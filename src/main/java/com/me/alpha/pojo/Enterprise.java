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
public class Enterprise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int enterpriseId;
	
	@Enumerated(EnumType.STRING)
	EnterpriseType enterpriseType;

	public EnterpriseType getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(EnterpriseType enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	String enterpriseName;
	
	@ManyToOne
	@JoinColumn(name="networkId",nullable=false)
	Network network;

	public enum EnterpriseType {
		HEALTHCARE, PROVIDER, DISTRIBUTER
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
