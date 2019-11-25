package com.xxk.bookstore.test;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xxk.bookstore.dao.impl.BookDAOImpl;
import com.xxk.bookstore.domain.Book;

class BaseDAOTest {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
		BookDAOImpl bookDAO = ac.getBean(BookDAOImpl.class);
		Book book = bookDAO.getBook(1);
		long id = bookDAO.insert("INSERT INTO trade(userid, tradetime) VALUES(?,?)", 1, new Date());
		System.out.println(id);

		System.out.println(book);
	}

}
