package com.me.alpha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.me.alpha.dao.AlertsDAO;
import com.me.alpha.dao.DaoFactory;
import com.me.alpha.pojo.Alerts;
import com.me.alpha.pojo.Organization;

@Controller
public class AlertsController {
	
	
	@RequestMapping(value="/alert-get",method=RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String alertsGet(HttpServletRequest req) {
		
		int organizationId = Integer.parseInt(req.getParameter("organization"));
		
		DaoFactory df = new DaoFactory();
		AlertsDAO ad = df.createAlertsDAO();
		Organization o = ad.getOrganization(organizationId);
		
		String message = "";
		List<Alerts> la = ad.getAlerts(o);
		if(la.size()>0) {
			for(Alerts a : la) {
				message = message + "\n" + a.getMessage();
				ad.alertSeen(a);
			}
			
			return message;
		}
		return "No new shipments received.";		
	}
	

}
