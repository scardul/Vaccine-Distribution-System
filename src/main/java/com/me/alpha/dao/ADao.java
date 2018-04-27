package com.me.alpha.dao;

import com.me.alpha.pojo.Administered;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;

public class ADao extends DAO{

	public ADao() {
		
	}
	
	
	public void administerVaccine(Organization o,int quantity,Vaccine v) {
		
		begin();
		Administered a = new Administered();
		a.setOrganization(o);
		a.setQuantity(quantity);
		a.setVaccine(v);
		getSession().save(a);
		commit();		
	}
	
}
