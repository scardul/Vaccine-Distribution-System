package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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


	public List<Administered> getRecords(Organization o) {
		// TODO Auto-generated method stub
		Criteria c = getSession().createCriteria(Administered.class);
		c.add(Restrictions.eq("organization",o));
		List<Administered> la = c.list();
		return la;
	}
	
}
