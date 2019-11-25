package com.xxk.bookstore.dao.impl;

import org.springframework.stereotype.Repository;

import com.xxk.bookstore.dao.AccountDAO;
import com.xxk.bookstore.domain.Account;

@Repository
public class AccountDAOImpl extends BaseDAO<Account> implements AccountDAO {

	@Override
	public Account get(Integer accountId) {
		String sql = "SELECT accountId, balance FROM account WHERE accountId = ?";
		return query(sql, accountId);
	}

	@Override
	public void updateBalance(Integer accountId, float amount) {
		String sql = "UPDATE account SET balance = balance - ? WHERE accountId = ?";
		update(sql, amount, accountId);
	}

}
