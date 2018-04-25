package com.me.alpha.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.me.alpha.pojo.Enterprise;
import com.me.alpha.pojo.Network;
import com.me.alpha.pojo.Organization;
import com.me.alpha.pojo.Person;
import com.me.alpha.pojo.User;

public class UserDAO extends DAO {

	UserDAO() {

	}

	// for login
	public User getUser(String username, String password) {

		Session session = getSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("username", username));
		crit.add(Restrictions.eq("password", password));
		List<User> result = crit.list();
		User u = null;
		if (result != null) {
			u = result.get(0);
		}
		return u;
	}

	public List<User> getUsers(Enterprise e, Network n,Organization o) {

		Session session = getSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("enterprise", e));
		crit.add(Restrictions.eq("network", n));
		crit.add(Restrictions.eq("organization", o));
		List<User> result = crit.list();
		return result;
	}

	public User createUser(String username, String role, Enterprise e, Network n, Organization o, Person p) {

		begin();
		User u = new User();
		u.setPerson(p);
		u.setUsername(username);
		u.setRole(role);
		u.setPassword("root");
		u.setEnterprise(e);
		u.setOrganization(o);
		u.setNetwork(n);
		getSession().save(u);
		commit();
		close();
		return u;
	}
	
	public boolean deleteUser(int uid) {
		
		begin();
		try {
			String hql = "delete from User where userId= :userId";
			getSession().createQuery(hql).setInteger("userId", uid).executeUpdate();
			commit();
			close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
