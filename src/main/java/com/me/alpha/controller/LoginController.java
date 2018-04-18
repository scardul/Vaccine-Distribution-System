package com.me.alpha.controller;

import javax.servlet.http.HttpServletRequest;

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
		String ret=null;
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		DaoFactory df = new DaoFactory();
		UserDAO ud = df.createUserDAO();
		
		User u = ud.getUser(username, password);
		String role = null;
		int eId,nId,oId = -1;
		role = u.getRole();
		
		if(role.equals("admin")) {
			ret = "admin-page";
		}                 		
		return ret;
	}
}
