package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;

import com.me.alpha.pojo.User;
import com.me.alpha.pojo.Vaccine;

@Controller
public class VaccineDAO extends DAO{

	public VaccineDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Vaccine> getVaccines(){
		
		Criteria crit = getSession().createCriteria(Vaccine.class);
		List<Vaccine> result = crit.list();
		return result;
	}
	
}
