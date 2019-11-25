package com.xxk.bookstore.dao.impl;

import org.springframework.stereotype.Repository;

import com.xxk.bookstore.dao.UserDAO;
import com.xxk.bookstore.domain.User;

@Repository
public class UserDAOImpl extends BaseDAO<User> implements UserDAO {

	@Override
	public User getUser(String username) {
		String sql = "SELECT userId, username, accountId "
				+ "FROM userinfo WHERE username = ?";
		return query(sql, username);
	}

}
