package com.me.alpha.dao;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.InternalRequest;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Vaccine;
import com.me.alpha.pojo.WorkRequest;

public class WorkRequestDAO extends DAO {

	public boolean createCDCRequest(int quantity, Organization sender, Enterprise receiver, Vaccine v) {

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

	public WorkRequest getRequest(int workRequestId) {

		Criteria c = getSession().createCriteria(WorkRequest.class);
		c.add(Restrictions.eq("workRequestId", workRequestId));
		WorkRequest w = (WorkRequest) c.uniqueResult();

		return w;
	}

	public void sendOrder(Enterprise e, Vaccine v, int q, WorkRequest w) {
		// TODO Auto-generated method stub
		begin();
		WorkRequest wr = new WorkRequest();
		wr.setQuantity(q);
		wr.setSenderEnterprise(e);
		wr.setVaccine(v);
		wr.setStatus("SENT");
		wr.setWorkRequest(w);
		getSession().save(wr);
		commit();
	}

	public void decline(int workRequestId) {
		begin();
		WorkRequest wr = getRequest(workRequestId);
		wr.setStatus("DECLINED");
		getSession().save(wr);
		commit();
	}

	public List<WorkRequest> getADRequests(Enterprise e) {
		// TODO Auto-generated method stub

		Criteria c = getSession().createCriteria(WorkRequest.class);
		c.add(Restrictions.eq("receiverEnterprise", e));
		c.add(Restrictions.isNull("status"));
		List<WorkRequest> lwr = c.list();
		return lwr;
	}

	public List<WorkRequest> getDistributerRequests(Enterprise e) {
		Criteria c = getSession().createCriteria(WorkRequest.class);
		c.add(Restrictions.isNotNull("workRequest"));
		c.add(Restrictions.eq("receiverEnterprise", e));
		c.add(Restrictions.ne("status", "HOLD"));
		List<WorkRequest> lwr = c.list();

		List<WorkRequest> l = helper();
		for (WorkRequest wr : l) {
			lwr.add(wr);
		}
		return lwr;
	}

	public List<WorkRequest> helper() {
		Criteria c = getSession().createCriteria(WorkRequest.class);
		c.add(Restrictions.isNull("receiverEnterprise"));
		List<WorkRequest> lwr = c.list();
		return lwr;
	}

	public String assignRequest(int workRequestId, Enterprise e) {
		begin();

		WorkRequest wr = getRequest(workRequestId);
		if(wr.getStatus().equals("APPROVED")) {
			return "Request has been completed and dispatched.";
		}		
		if (wr.getStatus().equals("FORWARD")) {
			return "Request already sent to manufacturer";
		}
		if(wr.getStatus().equals("COMPLETE")) {
			return "This request has already been completed";
		}
		if (wr.getStatus().equals("ASSIGNED")) {
			return "Request is already assigned";
		}
		wr.setStatus("ASSIGNED");
		wr.setReceiverEnterprise(e);
		getSession().save(wr);
		commit();

		return "Request assigned to " + e.getEnterpriseName();
	}

	public String approveRequest(int workRequestId, Organization o) {

		WorkRequest wr = getRequest(workRequestId);
		String z = wr.getStatus();
		DaoFactory df = new DaoFactory();
		InventoryDAO id = df.createInventoryDAO();
		Vaccine v = wr.getVaccine();
		int q = wr.getQuantity();
		if(z.equals("SENT")) {
			return "Request needs to be assigned.";
		}
		if(z.equals("COMPLETE")) {
			boolean x = id.approveRequest(v, q, o);
			Organization org = wr.getWorkRequest().getSenderOrganization();
			if(x) {
				id.receiveRequest(v,q,org);
				updateWorkRequest(workRequestId,"APPROVED");
				return "Request approved.";
			}
			else {
				return "Not enough vaccine.";
			}
		}
		if(z.equals("APPROVED")) {
			return "This request has been completed and dispatched.";
		}

		if (z.equals("ASSIGNED")) {
			
			boolean x = id.approveRequest(v, q, o);
			Organization org = wr.getWorkRequest().getSenderOrganization();
			if (x) {
				id.receiveRequest(v, q, org);
				updateWorkRequest(workRequestId,"APPROVED");
				return "Request approved.";
			} else {
				return "Request Cannot be completed.";
			}
		}
		if (z.equals("FORWARD")) {
			return "Request already sent to Manufacturer";
		}

		return "Request not assigned.";
	}

	public List<WorkRequest> findProviderRequest(Enterprise e) {
		// TODO Auto-generated method stub
		Criteria c = getSession().createCriteria(WorkRequest.class);
		c.add(Restrictions.eq("receiverEnterprise", e));
		List<WorkRequest> lwr = c.list();
		return lwr;
	}

	public String forward(WorkRequest w, String manufacturerName, Organization o) {
		// TODO Auto-generated method stub

		Criteria c = getSession().createCriteria(Enterprise.class);
		c.add(Restrictions.eq("enterpriseName", manufacturerName));
		Enterprise e = (Enterprise) c.uniqueResult();

		begin();
		String z = w.getStatus();
		if(z.equals("SENT")) {
			return "Request needs to be assigned.";
		}
		if (z.equals("APPROVED")) {
			return "This request is complete and dispatched.";
		}
		if (z.equals("COMPLETE")) {
			return "This request is complete and ready to be approved.";
		}

		if (z.equals("ASSIGNED")) {
			Enterprise sender = w.getReceiverEnterprise();
			Vaccine v = w.getVaccine();
			int q = w.getQuantity();
			WorkRequest nwr = new WorkRequest();
			nwr.setWorkRequest(w);
			nwr.setSenderEnterprise(sender);
			nwr.setReceiverEnterprise(e);
			nwr.setQuantity(q);
			nwr.setVaccine(v);
			nwr.setStatus("RECEIVED");
			w.setStatus("FORWARD");
			nwr.setSenderOrganization(o);
			getSession().save(nwr);
			getSession().save(w);
			commit();
			return "Request has been forwarded to Manufacturer";

		} else if (z.equals("FORWARD")) {
			return "Request is already sent to Manufacturer";
		}

		return "Request not assigned";
	}

	public String approveProviderRequest(int workRequestId) {
		// TODO Auto-generated method stub
		WorkRequest w = getRequest(workRequestId);
		String z = w.getStatus();
		WorkRequest w2 = w.getWorkRequest();
		int w2id = w2.getWorkRequestId();

		if (z.equals("RECEIVED")) {
			int q = w.getQuantity();
			Organization o = w.getSenderOrganization();
			Vaccine v = w.getVaccine();

			DaoFactory df = new DaoFactory();
			InventoryDAO id = df.createInventoryDAO();

			id.receiveRequest(v, q, o);
			updateWorkRequest(workRequestId,"APPROVED");
			updateWorkRequest(w2id, "COMPLETE");
			AlertsDAO ad = df.createAlertsDAO();
			ad.addAlert(v, q, o);
			String message = q + " units of " + v.getVaccineName() + " sent to " + o.getOrganizationName() + ".";
			return message;
		} else {
			return "Request already completed.";
		}
	}

	public void updateWorkRequest(int workRequestId,String x) {

		begin();
		Query query = getSession().createQuery("update WorkRequest set status = :status" + " where workRequestId = :w");
		query.setString("status", x);
		query.setInteger("w", workRequestId);
		query.executeUpdate();
		commit();
		close();
	}
}
