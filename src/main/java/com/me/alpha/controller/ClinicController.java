package com.me.alpha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.me.alpha.dao.ADao;
import com.me.alpha.dao.DaoFactory;
import com.me.alpha.dao.InternalRequestDAO;
import com.me.alpha.dao.InventoryDAO;
import com.me.alpha.dao.PersonDAO;
import com.me.alpha.dao.UserDAO;
import com.me.alpha.dao.VaccineDAO;
import com.me.alpha.dao.WorkRequestDAO;
import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Inventory;
import com.me.alpha.pojo.Network;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Person;
import com.me.alpha.pojo.TemporaryRequest;
import com.me.alpha.pojo.User;
import com.me.alpha.pojo.Vaccine;

@Controller
public class ClinicController {

	@RequestMapping(value = "/clinic-register.htm", method = RequestMethod.GET)
	public String registerpage(HttpServletRequest req) {

		return "clinic-admin-add";
	}

	public void sendEmail(String useremail, String message) {
		try {
			Email email = new SimpleEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setSSLOnConnect(true);
			email.setAuthenticator(new DefaultAuthenticator("contactapplication2018@gmail.com", "springmvc"));
			email.setFrom("no-reply@gmail.com");
			email.setSubject("Validate email");
			email.setMsg(message);
			email.addTo(useremail);
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Email sending failed");
		}

	}

	@RequestMapping(value = "/clinic-register.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String registeruser(HttpServletRequest req) {

		String fname = req.getParameter("fname");
		String lname = req.getParameter("lname");
		String phone = req.getParameter("phone");
		String email = req.getParameter("email");
		String username = req.getParameter("username");
		String role = req.getParameter("role");

		User u = (User) req.getSession().getAttribute("user");

		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();

		DaoFactory df = new DaoFactory();
		PersonDAO pd = df.createPersonDAO();

		Person p = pd.addPerson(fname, lname, phone, e, n, o);

		UserDAO ud = df.createUserDAO();
		User newuser = ud.createUser(username, role, e, n, o, p);
		if (newuser != null) {
			String message = "Hello " + fname.toUpperCase()
					+ ", \nYour account has been created with the following details: \n Username: " + username
					+ " \n Password: root \n \n Enjoy.";
			sendEmail(email, message);
		}
		return "User registered. Password has been mailed.";
	}

	@RequestMapping(value = "/clinic-delete.htm", method = RequestMethod.GET)
	public String deletepage(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");

		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();

		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();

		List<User> users = ud.getUsers(e, n, o);
		req.setAttribute("users", users);

		return "clinic-admin-remove";
	}

	@RequestMapping(value = "/clinic-delete.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String deleteUsers(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");

		String[] a = req.getParameterValues("delete");
		if (a != null) {
			DaoFactory df = new DaoFactory();
			UserDAO ud = df.createUserDAO();
			int x = a.length;
			int count = 0;
			for (int i = 0; i < x; i++) {
				int userId = Integer.parseInt(a[i]);
				if (ud.deleteUser(userId)) {
					count++;
				}
			}
			req.setAttribute("deleted", count);
			return count+" users deleted.";
		}

		return "Something went wrong. Delete failed.";
	}

	@RequestMapping(value = "/prescribe.htm", method = RequestMethod.GET)
	public String doctor(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");
		DaoFactory df = new DaoFactory();
		VaccineDAO vd = df.createVaccineDAO();

		List<Vaccine> vl = vd.getVaccines();
		req.setAttribute("vaccines", vl);

		return "doctor-view";
	}

	@RequestMapping(value = "/prescribe.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String doctorPrescribe(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");

		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();

		String vaccine = req.getParameter("vaccine");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		
		DaoFactory df = new DaoFactory();
		
		VaccineDAO vd = df.createVaccineDAO();

		List<Vaccine> vl = vd.getVaccines();
		
		Vaccine send=null;
		for(Vaccine v : vl) {
			if(v.getVaccineName().equals(vaccine)) {
				send = v;
			}
		}
		InventoryDAO id = df.createInventoryDAO();
		List<Inventory> li = id.getInventory(o);
		for(Inventory i : li) {
			if(i.getVaccine().equals(send)) {
				int iq = i.getQuantity();
				if(iq > quantity) {
					iq = iq - quantity;
					i.setQuantity(iq);
					id.saveInventory(i);
					ADao ad = df.createADAO();
					ad.administerVaccine(o, quantity, send);
					return "Vaccine administered.";
				}
			}
		}
		InternalRequestDAO rd = df.createInternalReqDAO();
		
		rd.addInternalRequest(u,o,send,quantity);
		
		return "There wasn't enough vaccine. Request sent to Inventory Manager.";
	}
	
	
	@RequestMapping(value = "/clinicinventory.htm", method = RequestMethod.GET)
	public String inventoryManager(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");
		DaoFactory df = new DaoFactory();
		InternalRequestDAO ir = df.createInternalReqDAO();
		Organization o = u.getOrganization();
		List<TemporaryRequest> rl = ir.getRequests(o);
		InventoryDAO id = df.createInventoryDAO();
		List<Inventory> li = id.getInventory(o);
		req.setAttribute("li", li);
	    
		
		req.getSession().setAttribute("data", rl);
		
		return "clinic-inventory-view";
	}
	
	@RequestMapping(value = "/clinicinventory.htm", method = RequestMethod.POST)
	public String sendOrder(HttpServletRequest req) {
		DaoFactory df = new DaoFactory();
		User u = (User) req.getSession().getAttribute("user");
		Organization o = u.getOrganization();
		Enterprise e = u.getEnterprise();
		List<TemporaryRequest> rl = (List<TemporaryRequest>) req.getSession().getAttribute("data");
		String vaccineName = req.getParameter("select");
		Vaccine v = null;
		int quantity = 0;
		List<Integer> li = null;
		for(TemporaryRequest t : rl) {
			if(t.getVaccine().getVaccineName().equals(vaccineName)) {
				v = t.getVaccine();
				quantity = t.getQuantity();
				li = t.getLi();
			}
		}
		InternalRequestDAO ir = df.createInternalReqDAO();
		ir.setSeen(li);
		WorkRequestDAO wd = df.createWorkRequestDAO();
		boolean x = wd.createCDCRequest(quantity, o, e, v);
		if(x) {
			return "success";
		}
		return "fail";
	}

}
