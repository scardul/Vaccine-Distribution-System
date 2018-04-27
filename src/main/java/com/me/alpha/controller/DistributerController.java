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

import com.me.alpha.dao.DaoFactory;
import com.me.alpha.dao.InventoryDAO;
import com.me.alpha.dao.PersonDAO;
import com.me.alpha.dao.UserDAO;
import com.me.alpha.dao.WorkRequestDAO;
import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Inventory;
import com.me.alpha.pojo.Network;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Person;
import com.me.alpha.pojo.User;
import com.me.alpha.pojo.Vaccine;
import com.me.alpha.pojo.WorkRequest;

@Controller
public class DistributerController {
	
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

	@RequestMapping(value = "/distributer-register.htm", method = RequestMethod.GET)
	public String register(HttpServletRequest req) {

		return "distributer-admin-add";
	}

	@RequestMapping(value = "/distributer-register.htm", method = RequestMethod.POST)
	public String registerpage(HttpServletRequest req) {

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
		return "success";
	}
	
	@RequestMapping(value = "/distributer-delete.htm", method = RequestMethod.GET)
	public String deletepage(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");

		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();

		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();

		List<User> users = ud.getUsers(e, n, o);
		req.setAttribute("users", users);

		return "distributer-admin-remove";
	}
	
	@RequestMapping(value = "/distributer-delete.htm", method = RequestMethod.POST, produces="application/json")
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
			if(count==1) {
				return "Deleted 1 user";
			}
			return "Deleted "+count+" users.";
		}

		return "Select a user to delete";
	}
	
	
	@RequestMapping(value = "/distributer-assign.htm", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String assign(HttpServletRequest req) {
		
		User u = (User) req.getSession().getAttribute("user");
		Enterprise e = u.getEnterprise();
		
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		String display = wd.assignRequest(workRequestId,e);
		
		return display;
		
	}
	
	@RequestMapping(value = "/distributer-approve.htm", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String approve(HttpServletRequest req) {
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		User u = (User) req.getSession().getAttribute("user");
		Organization o = u.getOrganization();
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		String display = wd.approveRequest(workRequestId,o);
		
		return display;	
	}
	
	@RequestMapping(value = "/distributer-forward.htm", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String forward(HttpServletRequest req) {
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		User u = (User) req.getSession().getAttribute("user");
		Network n = u.getNetwork();
		Organization o = u.getOrganization();
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		WorkRequest w = wd.getRequest(workRequestId);
		int wid = w.getWorkRequest().getWorkRequestId();
		wd.updateWorkRequest(wid, "HOLD");
		String manufacturerName = w.getVaccine().getManufacturer().getManufacturerName();
		String display = wd.forward(w,manufacturerName,o,n);
		
		return display;
		
	}
	
	@RequestMapping(value = "/distributer-inventory-view.htm", method = RequestMethod.GET)
	public String load(HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("user");
		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();
		
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		List<WorkRequest> lwr = wd.getDistributerRequests(e);
		
		InventoryDAO id = df.createInventoryDAO();
		List<Inventory> li = id.getInventory(o);
		req.setAttribute("lwr", lwr);
		req.setAttribute("li", li);
		
		return "distributer-inventory-view";
	}

}
