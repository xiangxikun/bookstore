package com.xxk.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxk.bookstore.dao.AccountDAO;
import com.xxk.bookstore.domain.Account;

@Service
public class AccountService {
	
	@Autowired
	private AccountDAO accountDAO;
	
	public Account getAccount(int accountId) {
		return accountDAO.get(accountId);
	}
	

}
