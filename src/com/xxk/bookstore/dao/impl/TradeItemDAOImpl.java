package com.xxk.bookstore.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xxk.bookstore.dao.TradeItemDAO;
import com.xxk.bookstore.domain.TradeItem;

@Repository
public class TradeItemDAOImpl extends BaseDAO<TradeItem> implements TradeItemDAO {

	@Override
	public void batchSave(Collection<TradeItem> items) {
		String sql = "INSERT INTO tradeItem(bookid,quantity,tradeid) " + "VALUES(?, ?, ?)";
		Object[][] params = new Object[items.size()][3];

		List<TradeItem> tradeItems = new ArrayList<>(items);
		for (int i = 0; i < tradeItems.size(); i++) {
			
			params[i][0] = tradeItems.get(i).getBookId();
			params[i][1] = tradeItems.get(i).getQuantity();
			params[i][2] = tradeItems.get(i).getTradeId();
		}

		batch(sql, params);

	}

	@Override
	public Set<TradeItem> getTradeItemsWithTradeId(Integer tradeId) {
		String sql = "SELECT itemId tradeItemId, bookId, quantity, tradeId FROM tradeitem WHERE tradeid = ?";

		return new HashSet<>(queryForList(sql, tradeId));
	}

}
