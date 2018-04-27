package com.me.alpha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.me.alpha.dao.DaoFactory;
import com.me.alpha.dao.WorkRequestDAO;
import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.User;
import com.me.alpha.pojo.WorkRequest;

@Controller
public class ProviderController {

	
	@RequestMapping(value = "/manager-view.htm", method = RequestMethod.GET)
	public String getIncomingRequests(HttpServletRequest req) {
		
		User u = (User) req.getSession().getAttribute("user");
		Enterprise e = u.getEnterprise();
		
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		List<WorkRequest> lwr = wd.findProviderRequest(e);
		req.setAttribute("lwr", lwr);
		
		
		return "manager-view";
	}
	
	@RequestMapping(value = "/manager-approve.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String managerApprove(HttpServletRequest req) {
		int workRequestId = Integer.parseInt(req.getParameter("select"));
		
		DaoFactory df = new DaoFactory();
		WorkRequestDAO wd = df.createWorkRequestDAO();
		String display = wd.approveProviderRequest(workRequestId);
		
		
		return display;
	}			
	
}
