package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
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

		begin();
		List<Inventory> il = getInventory(o);
		for (Inventory i : il) {
			if (i.getVaccine().equals(v)) {
				if (i.getQuantity() >= q) {
					int a = i.getQuantity() - q;
					i.setQuantity(a);
					getSession().save(i);
					commit();
					return true;
				}
			}
		}
		return false;
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
