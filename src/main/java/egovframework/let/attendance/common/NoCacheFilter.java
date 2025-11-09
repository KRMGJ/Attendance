package egovframework.let.attendance.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) res;
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", 0);
		chain.doFilter(req, res);
	}
}
