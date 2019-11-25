package com.xxk.bookstore.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class EncodingFilter
 */

@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EncodingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String encoding = filterConfig.getServletContext().getInitParameter("encoding");
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=utf-8");
		chain.doFilter(request, response);
		
	}

	private FilterConfig filterConfig = null;
	
	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

}
