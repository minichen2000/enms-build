package com.nsb.enms.adapter.server.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class AccessControlFilter implements javax.servlet.Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		res.addHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Origin,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
}