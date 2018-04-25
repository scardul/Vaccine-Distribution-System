package com.me.alpha.dao;

import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;
import com.me.alpha.pojo.WorkRequest;

public class WorkRequestDAO extends DAO {
	
	public boolean createCDCRequest(int quantity,Organization sender,Enterprise receiver,Vaccine v) {
		
		begin();
		WorkRequest wr = new WorkRequest();
		wr.setQuantity(quantity);
		wr.setSenderOrganization(sender);
		wr.setReceiverEnterprise(receiver);
		wr.setVaccine(v);
		getSession().save(wr);
		commit();
		return true;
	}
}
