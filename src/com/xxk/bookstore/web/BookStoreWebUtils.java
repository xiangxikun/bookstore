package com.xxk.bookstore.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.xxk.bookstore.domain.ShoppingCart;

public class BookStoreWebUtils {
	
	//private static ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
	
	/**
	 * 从IOC容器中获取组件的方法
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T>T getBean(Class<T> clazz) {
		
		// 获取IOC容器
		WebApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
		return ac.getBean(clazz);
	}
	
	/**
	 *  获取购物车对象：从 session 中获取， 若 session中没有该对象
	 *  则创建一个新的购物车对象，放入到session 中.
	 *  若有，则直接返回.
	 * @param request
	 * @return
	 */
	public static ShoppingCart getShoppingCart(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		ShoppingCart sc = (ShoppingCart) session.getAttribute("ShoppingCart");
		
		if(sc == null) {
			sc = new ShoppingCart();
			session.setAttribute("ShoppingCart", sc);
		}
		
		return sc;
		
	}

}
