package com.xxk.bookstore.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxk.bookstore.dao.AccountDAO;
import com.xxk.bookstore.dao.BookDAO;
import com.xxk.bookstore.dao.TradeDAO;
import com.xxk.bookstore.dao.TradeItemDAO;
import com.xxk.bookstore.dao.UserDAO;
import com.xxk.bookstore.domain.Book;
import com.xxk.bookstore.domain.ShoppingCart;
import com.xxk.bookstore.domain.ShoppingCartItem;
import com.xxk.bookstore.domain.Trade;
import com.xxk.bookstore.domain.TradeItem;
import com.xxk.bookstore.web.CriteriaBook;
import com.xxk.bookstore.web.Page;

@Service
public class BookService {
	
	@Autowired
	private BookDAO bookDAO;
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	private TradeDAO tradeDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TradeItemDAO tradeItemDAO;
	
	public Page<Book> getPage(CriteriaBook criteriaBook){
		return bookDAO.getPage(criteriaBook);
	}

	public Book getBook(int id) {
		
		return bookDAO.getBook(id);
	}

	public String addToCart(int id, ShoppingCart sc) {
		Book book = bookDAO.getBook(id);
		
		if(book != null) {
			sc.addBook(book);
			System.out.println(book.getTitle());
			return book.getTitle();
		}
		return null;
		
		
	}

	public void removeItemFromShoppingCart(ShoppingCart sc, int id) {
		sc.removeItem(id);
	}

	public void clearShoppingCart(ShoppingCart sc) {
		sc.clear();
	}

	public void updateItemQuantity(ShoppingCart sc, int id, int quantity) {
		sc.updateItemQuantity(id, quantity);
	}
	

	// 业务方法
	public void cash(ShoppingCart shoppingCart, String username, String accountId) {
		// 1. 更新mybooks 数据表相关记录的 salesamount 和 storenumber
		bookDAO.batchUpdateStoreNumberAndSalesAmount(shoppingCart.getItems());
		
		// 2. 更新 account 数据表的 balance
		accountDAO.updateBalance(Integer.parseInt(accountId), shoppingCart.getTotalMoney());
		
		// 3. 向 trade 数据表插入一条记录
		Trade trade = new Trade();
		
		trade.setTradeTime(new Date(new java.util.Date().getTime()));
		trade.setUserId(userDAO.getUser(username).getUserId());
		tradeDAO.insert(trade);
		
		// 4. 向tradeitem 数据表插入 n 条记录
		Collection<TradeItem> items = new ArrayList<>();
		for(ShoppingCartItem sci: shoppingCart.getItems()) {
			TradeItem tradeItem = new TradeItem();
			tradeItem.setBookId(sci.getBook().getId());
			tradeItem.setQuantity(sci.getQuantity());
			tradeItem.setTradeId(trade.getTradeId());
			
			items.add(tradeItem);
		}
		tradeItemDAO.batchSave(items);
		
		// 5.清空购物车
		shoppingCart.clear();
	}

}
