package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.me.alpha.pojo.Alerts;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;

public class AlertsDAO extends DAO {

	public void addAlert(Vaccine v, int q, Organization o) {
		
		begin();
		String vaccine = v.getVaccineName();
		Alerts a = new Alerts();
		a.setOrganization(o);
		a.setStatus("NOTSEEN");
		String message = "Received "+q+" units of "+vaccine+".";
		a.setMessage(message);
		getSession().save(a);
		commit();
		close();
	}
	
	public List<Alerts> getAlerts(Organization o){
		
		Criteria c = getSession().createCriteria(Alerts.class);
		c.add(Restrictions.eq("organization", o));
		c.add(Restrictions.eq("status","NOTSEEN"));
		List<Alerts> la = c.list();
		
		return la;
		
	}
	
	public Organization getOrganization(int o) {
		Criteria c = getSession().createCriteria(Organization.class);
		c.add(Restrictions.eq("organizationId",o));
		Organization org = (Organization) c.uniqueResult();
		return org;
	}
	
	public void alertSeen(Alerts a) {
		begin();
		a.setStatus("SEEN");
		getSession().save(a);
		commit();
		close();
	}
	
	

}
