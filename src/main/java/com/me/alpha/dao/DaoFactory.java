package com.me.alpha.dao;

public class DaoFactory {
	
	public DaoFactory(){
		
	}
	
	public UserDAO createUserDAO() {
		
		return new UserDAO();
	}
	
	public PersonDAO createPersonDAO() {
		
		return new PersonDAO();
		
	}
	
	public VaccineDAO createVaccineDAO() {
		
		return new VaccineDAO();
	}
	
	public InternalRequestDAO createInternalReqDAO() {
		return new InternalRequestDAO();
	}

	public WorkRequestDAO createWorkRequestDAO() {
		// TODO Auto-generated method stub
		return new WorkRequestDAO();
	}

}
