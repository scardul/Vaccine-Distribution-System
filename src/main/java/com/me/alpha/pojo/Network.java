package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Network {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int networkId;
	
	
	public int getNetworkId() {
		return networkId;
	}


	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}


	public String getNetworkName() {
		return networkName;
	}


	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}


	String networkName;
}
