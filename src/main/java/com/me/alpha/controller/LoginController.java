package com.me.alpha.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.me.alpha.dao.DaoFactory;
import com.me.alpha.dao.UserDAO;
import com.me.alpha.pojo.User;

@Controller
public class LoginController {

	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public String home(HttpServletRequest req) {
		String ret = null;
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();

		User u = ud.getUser(username, password);
		String role = null;
		int eId, nId, oId = 0;

		if (u != null) {
			role = u.getRole();
			HttpSession s = req.getSession();
			s.setAttribute("user", u);
			s.setAttribute("login", "xxx");
		}

		switch (role) {

		case "HA":
			ret = "head-analyst-view";
			break;
		case "NA":
			ret = "network-analyst-view";
			break;
		case "CDCAdmin":
			ret = "cdc-admin-view";
			break;
		case "AD":
			ret = "redirect:/cdc-ad-view.htm";
			break;
		case "Manager":
			ret = "redirect:/manager-view.htm";
			break;
		case "EA":
			ret = "enterprise-analyst-view";
			break;
		case "Doctor":
			ret = "redirect:/prescribe.htm";
			break;
		case "IM":
			ret = "redirect:/clinicinventory.htm";
			break;
		case "DistIM":
			ret = "redirect:/distributer-inventory-view.htm";
			break;
		case "ClinicAdmin":
			ret = "clinic-admin-view";
			break;
		case "DistAdmin":
			ret = "distributer-admin-view";
			break;
		}

		return ret;
	}
	
	
	@RequestMapping(value = "/logout.htm", method = RequestMethod.POST)
	public String logout(HttpServletRequest req) {
		req.getSession().setAttribute("user", null);
		req.getSession().setAttribute("login", null);
		req.getSession().setAttribute("data", null);
		return "home";
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
	
}
