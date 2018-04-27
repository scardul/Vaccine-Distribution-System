package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.me.alpha.pojo.Inventory;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;

public class InventoryDAO extends DAO {

	public List<Inventory> getInventory(Organization o) {

		Criteria c = getSession().createCriteria(Inventory.class);
		c.add(Restrictions.eq("organization", o));
		List<Inventory> il = c.list();
		return il;
	}

	public boolean approveRequest(Vaccine v, int q, Organization o) {

		int a = 0;
		List<Inventory> il = getInventory(o);
		for (Inventory i : il) {
			if (i.getVaccine().equals(v)) {
				if (i.getQuantity() >= q) {
					a = i.getQuantity() - q;
				}
				else {
					return false;
				}
			}
		}
		if(a==0) {
			return false;
		}
		begin();
		Query query = getSession().createQuery("update Inventory set quantity = :q" + " where vaccine = :v");
		query.setInteger("q", a);
		query.setParameter("v", v);
		int r = query.executeUpdate();
		commit();
		close();
		
		if(r>0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int checkInvent(Vaccine v, Organization o) {
		
		int x=-1;
		begin();
		Criteria c = getSession().createCriteria(Inventory.class);
		c.add(Restrictions.eq("vaccine", v));
		c.add(Restrictions.eq("organization",o));
		c.list();
		
		return x;
		
	}
	
	public boolean receiveRequest(Vaccine v, int q, Organization o) {

		begin();
		List<Inventory> il = getInventory(o);
		for (Inventory i : il) {
			if (i.getVaccine().equals(v)) {
				int x = i.getQuantity();
				i.setQuantity(q + x);
				getSession().save(i);
				commit();
				close();
				return true;
			}
		}
		Inventory i = new Inventory();
		i.setOrganization(o);
		i.setQuantity(q);
		i.setVaccine(v);
		getSession().save(i);
		commit();
		close();
		return true;

	}

	public void saveInventory(Inventory i) {

		begin();
		getSession().save(i);
		commit();
		close();
	}
}
