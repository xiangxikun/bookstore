package com.xxk.bookstore.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.xxk.bookstore.domain.Account;
import com.xxk.bookstore.domain.Book;
import com.xxk.bookstore.domain.ShoppingCart;
import com.xxk.bookstore.domain.ShoppingCartItem;
import com.xxk.bookstore.domain.User;
import com.xxk.bookstore.service.AccountService;
import com.xxk.bookstore.service.BookService;
import com.xxk.bookstore.service.UserService;
import com.xxk.bookstore.web.BookStoreWebUtils;
import com.xxk.bookstore.web.CriteriaBook;
import com.xxk.bookstore.web.Page;

@WebServlet(urlPatterns = "/bookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private BookService bookService = BookStoreWebUtils.getBean(BookService.class);
	
	protected void clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		bookService.clearShoppingCart(sc);
		
		request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);
	}
	
	private UserService userService = BookStoreWebUtils.getBean(UserService.class);
	
	protected void cash(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.简单验证：验证表单域的值是否符合基本的规范：是否为空，是否可以转为int类型，是否是一个email.不需要进行查询
		//数据库或调用任何的业务方法.
		String username = request.getParameter("username");
		String accountId = request.getParameter("accountId");
		
		StringBuffer errors = validateFormField(username, accountId);
		
		// 表单验证通过
		if(errors.toString().equals("")) {
			errors = validateUser(username, accountId);
			
			// 用户名和账号验证通过
			if(errors.toString().equals("")) {
				errors = validateBookStoreNumber(request);
				
				// 库存验证通过
				if(errors.toString().equals("")) {
					errors = validateBalance(request, accountId);
				}
			}
		}
		
		if(!errors.toString().equals("")) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/WEB-INF/pages/cash.jsp").forward(request, response);
			return;
		}
		
		// 验证通过执行具体的逻辑操作
		bookService.cash(BookStoreWebUtils.getShoppingCart(request), username, accountId);
		response.sendRedirect(request.getContextPath() + "/success.jsp");
	}
	
	private AccountService accountService = BookStoreWebUtils.getBean(AccountService.class);
	
	//验证余额是充足
	public StringBuffer validateBalance(HttpServletRequest request, String accountId) {
		
		StringBuffer errors = new StringBuffer("");
		ShoppingCart cart = BookStoreWebUtils.getShoppingCart(request);
		
		Account account = accountService.getAccount(Integer.parseInt(accountId));
		
		if(cart.getTotalMoney() > account.getBalance()) {
			errors.append("余额不足");
		}
		
		return errors;
	}
	
	//验证库存是否充足
	public StringBuffer validateBookStoreNumber(HttpServletRequest request) {
		
		StringBuffer errors = new StringBuffer("");
		
		ShoppingCart cart = BookStoreWebUtils.getShoppingCart(request);
		
		for(ShoppingCartItem sci: cart.getItems()) {
			int quantity = sci.getQuantity();
			int storeNumber = bookService.getBook(sci.getBook().getId()).getStoreNumber();
			
			if(quantity > storeNumber) {
				errors.append(sci.getBook().getTitle() + "库存不足<br>");
			}
		}
		
		return errors;
	}
	
	//验证用户名和账号是否匹配
	public StringBuffer validateUser(String username, String accountId) {
		boolean flag = false;
		User user = userService.getUserByUserName(username);
		if(user != null) {
			int accountId2 = user.getAccountId();
			if(accountId.trim().equals("" + accountId2)) {
				flag = true;
			}
		}
		
		StringBuffer errors2 = new StringBuffer("");
		if(!flag) {
			errors2.append("用户名和账号不匹配");
		}
		
		return errors2;
	}
	
	//验证表单域是否符合基本的规则：是否为空。
	public StringBuffer validateFormField(String username, String accountId) {
		StringBuffer errors = new StringBuffer("");
		
		if(username == null || username.trim().equals("")) {
			errors.append("用户名不能为空<br>");
		}
		
		if(accountId == null || accountId.trim().equals("")) {
			errors.append("账号不能为空");
		}
		
		return errors;
	}
	
	protected void updateItemQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//4.在updateItemQuantity方法中，获取quanity，id，再获取购物车对象，调用service的方法做修改
		String idStr = request.getParameter("id");
		String quantityStr = request.getParameter("quantity");
		
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		
		int id = -1;
		int quantity = -1;
		
		try {
			id = Integer.parseInt(idStr);
			quantity = Integer.parseInt(quantityStr);
		} catch (Exception e) {}
		
		if(id > 0 && quantity > 0)
		bookService.updateItemQuantity(sc, id, quantity);
		
		//5.传回JSON数据：bookNumber:xx，totalMoney
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bookNumber", sc.getBookNumber());
		result.put("totalMoney", sc.getTotalMoney());
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(result);
		response.setContentType("text/javascript");
		response.getWriter().print(jsonStr);
		
	}
	
	protected void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {}
		
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		bookService.removeItemFromShoppingCart(sc, id);
		
		if(sc.isEmpty()){
			request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
	}
	
	protected void forwardPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		request.getRequestDispatcher("/WEB-INF/pages/" + page).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		
		try {
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.setAccessible(true);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 获取商品的 id
		String idStr = request.getParameter("id");
		int id = -1;
		String titleStr = null;
		
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {}
		
		if(id > 0){
			//2. 获取购物车对象
			ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
			
			//3. 调用 BookService 的 addToCart() 方法把商品放到购物车中
			titleStr = bookService.addToCart(id, sc);
			request.setAttribute("title", titleStr);
		}
		
		if(titleStr != null){
			//4. 直接调用 getBooks() 方法. 
			getBooks(request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/error-1.jsp");
	}

	
	protected void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		int id = -1;
		
		Book book = null;
		
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {}
		
		if(id > 0) {
			book = bookService.getBook(id);
		}
		
		if(book == null) {
			response.sendRedirect(request.getContextPath() + "/error-1.jsp");
			return;
		}
		request.setAttribute("book", book);
		request.getRequestDispatcher("/WEB-INF/pages/book.jsp").forward(request, response);
	}
	
	protected void getBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageNoStr = request.getParameter("pageNo");
		String minPriceStr = request.getParameter("minPrice");
		String maxPriceStr = request.getParameter("maxPrice");
		
		int pageNo = 1;
		int minPrice= 0;
		int maxPrice = Integer.MAX_VALUE;
		
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		
		try {
			minPrice = Integer.parseInt(minPriceStr);
		} catch (NumberFormatException e) {}
		
		try {
			maxPrice = Integer.parseInt(maxPriceStr);
		} catch (NumberFormatException e) {}
		
		CriteriaBook criteriaBook = new CriteriaBook(minPrice, maxPrice, pageNo);
		Page<Book> page = bookService.getPage(criteriaBook);
		
		request.setAttribute("bookpage", page);
		
		request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request, response);
	}

}
