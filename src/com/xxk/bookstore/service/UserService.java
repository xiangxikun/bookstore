package com.xxk.bookstore.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxk.bookstore.dao.BookDAO;
import com.xxk.bookstore.dao.TradeDAO;
import com.xxk.bookstore.dao.TradeItemDAO;
import com.xxk.bookstore.dao.UserDAO;
import com.xxk.bookstore.domain.Trade;
import com.xxk.bookstore.domain.TradeItem;
import com.xxk.bookstore.domain.User;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TradeDAO tradeDAO;
	@Autowired
	private TradeItemDAO tradeItemDAO;
	@Autowired
	private BookDAO bookDAO;
	
	public User getUserByUserName(String username) {
		return userDAO.getUser(username);
	}
	
	public User getUserWithTrades(String username) {
		// 调用UserDAO的方法获取User对象
		User user = userDAO.getUser(username);
		if(user == null) {
			return null;
		}
		
		// 调用TradeDAO的方法获取Trade的集合，把其装配为User的属性
		int userId = user.getUserId();
		
		// 调用TradeltemDAO的方法获取每一个Trade中的Tradeltem的集合，并把其装配为Trade的属性
		Set<Trade> trades = tradeDAO.getTradesWithUserId(userId);

		if(trades != null) {
			for(Trade trade: trades) {
				int tradeId = trade.getTradeId();
				Set<TradeItem> items = tradeItemDAO.getTradeItemsWithTradeId(tradeId);
				
				if(items != null) {
					for(TradeItem item: items) {
						item.setBook(bookDAO.getBook(item.getBookId()));
					}
				}
				
				trade.setItems(items);
			}
		}
		
		user.setTrades(trades);
		return user;
		
	}

}
