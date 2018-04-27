package com.me.alpha.controller;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Controller;

import com.me.alpha.dao.ADao;
import com.me.alpha.dao.DaoFactory;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;
import com.me.alpha.pojo.Administered;

@Controller
public class AnalystController {

	public void getAdministered(Organization o) {
		
		List<Administered> fa = null;
		DaoFactory df = new DaoFactory();
		ADao ad = df.createADAO();
		List<Administered> la = ad.getRecords(o);
		for(int i=0;i<la.size();i++) {
			boolean x = true;
			Administered a = fa.get(0);
			Vaccine v = a.getVaccine();
			for(Administered xx : fa) {
				if(xx.getVaccine().equals(v)) {
					int t = a.getQuantity() + xx.getQuantity();
					xx.setQuantity(t);
				}
			}
		}
		
	}
	
	
}
