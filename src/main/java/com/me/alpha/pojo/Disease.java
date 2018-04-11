package com.me.alpha.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Disease {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int diseaseId;
	
	String diseaseName;
}
