package com.me.alpha.dao;

import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Network;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Person;

public class PersonDAO extends DAO {

	PersonDAO(){
		
	}
	
	public Person addPerson(String fname, String lname, String phone, Enterprise e,Network n, Organization o) {		
		begin();
		Person p = new Person();
		p.setfName(fname);
		p.setlName(lname);
		p.setEnterprise(e);
		p.setOrganization(o);
		p.setNetwork(n);
		p.setPhone(phone);
		getSession().save(p);
		commit();
		return p;
	}
	
}
