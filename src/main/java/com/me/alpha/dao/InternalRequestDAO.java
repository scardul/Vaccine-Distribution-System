package com.me.alpha.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.me.alpha.pojo.InternalRequest;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.TemporaryRequest;
import com.me.alpha.pojo.User;
import com.me.alpha.pojo.Vaccine;

public class InternalRequestDAO extends DAO {

	public InternalRequestDAO() {

	}

	public void addInternalRequest(User u, Organization o, Vaccine send, int quantity) {
		begin();
		InternalRequest ir = new InternalRequest();
		ir.setOrganization(o);
		ir.setUser(u);
		ir.setVaccine(send);
		ir.setQuantity(quantity);
		getSession().save(ir);
		commit();
	}

	public List<TemporaryRequest> getRequests(Organization o) {

		Criteria c = getSession().createCriteria(InternalRequest.class);
		c.add(Restrictions.eqOrIsNull("organization", o));
		List<InternalRequest> l = c.list();
		List<Vaccine> vl = new ArrayList<>();
		List<TemporaryRequest> ltr = new ArrayList<>();
		
		for (InternalRequest ir : l) {
			Vaccine temp = ir.getVaccine();
			if (vl.contains(temp)) {
				for(TemporaryRequest t : ltr) {
					if(t.getVaccine().equals(temp)) {
						t.setQuantity(ir.getQuantity());
					}
				}
			} else {
				vl.add(temp);
				TemporaryRequest tr = new TemporaryRequest();
				tr.setQuantity(ir.getQuantity());
				tr.setVaccine(temp);
				ltr.add(tr);
			}
		}
		return ltr;
	}

}
