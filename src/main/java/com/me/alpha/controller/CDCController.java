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
import com.me.alpha.dao.PersonDAO;
import com.me.alpha.dao.UserDAO;
import com.me.alpha.dao.WorkRequestDAO;
import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Network;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Person;
import com.me.alpha.pojo.User;
import com.me.alpha.pojo.Vaccine;
import com.me.alpha.pojo.WorkRequest;

@Controller
public class CDCController {

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

	@RequestMapping(value = "/cdc-register.htm", method = RequestMethod.GET)
	public String register(HttpServletRequest req) {

		return "cdc-admin-add";
	}

	@RequestMapping(value = "/cdc-register.htm", method = RequestMethod.POST)
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
		return "cdc-admin-view";
	}

	@RequestMapping(value = "/cdc-delete.htm", method = RequestMethod.GET)
	public String deletepage(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");

		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		Organization o = u.getOrganization();

		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();

		List<User> users = ud.getEnterpriseUsers(e, n);
		req.setAttribute("users", users);

		return "cdc-admin-remove";
	}

	@RequestMapping(value = "/cdc-delete.htm", method = RequestMethod.POST)
	public String deleteUsers(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");
		Enterprise e = u.getEnterprise();
		Network n = u.getNetwork();
		int a = Integer.parseInt(req.getParameter("delete"));

		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();
		int userId = a;
		ud.deleteUser(userId);
		
		List<User> users = ud.getEnterpriseUsers(e, n);
		req.setAttribute("users", users);
		req.setAttribute("deleted", a);
		
		return "cdc-admin-remove";


	}

	@RequestMapping(value = "/cdc-ad-view.htm", method = RequestMethod.GET)
	public String filler(HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("user");
		Enterprise e = u.getEnterprise();
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		List<WorkRequest> lwr = wd.getADRequests(e);

		req.getSession().setAttribute("data", lwr);

		return "cdc-ad-view";
	}

	@RequestMapping(value = "/cdc-approve.htm", method = RequestMethod.POST)
	public String approve(HttpServletRequest req) {

		User u = (User) req.getSession().getAttribute("user");
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		Enterprise e = u.getEnterprise();
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		WorkRequest w = wd.getRequest(workRequestId);
		Vaccine v = w.getVaccine();
		int q = w.getQuantity();
		wd.sendOrder(e, v, q, w);
		wd.updateWorkRequest(w.getWorkRequestId(),"SENT");
		List<WorkRequest> lwr = wd.getADRequests(e);
		req.getSession().setAttribute("data", lwr);

		return "cdc-ad-view";
	}

	@RequestMapping(value = "/cdc-decline.htm", method = RequestMethod.POST)
	public String decline(HttpServletRequest req) {
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		wd.decline(workRequestId);

		return "order-declined";
	}
}
