package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int userId;
	
	String username;
	String password;
	
	@ManyToOne
	@JoinColumn(name="pId",nullable=false)
	Person person;
	
	String role;
	
	@ManyToOne
	@JoinColumn(name="networkId")
	Network network;
	
	@ManyToOne
	@JoinColumn(name="enterpriseId")
	Enterprise enterprise;
	
	@ManyToOne
	@JoinColumn(name="organizationId")
	Organization organization;
	
}
