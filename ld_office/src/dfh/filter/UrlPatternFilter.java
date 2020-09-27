package dfh.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

///author:sun
public class UrlPatternFilter implements Filter {
	private static Logger log = Logger.getLogger(UrlPatternFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (req.getRequestURI().endsWith(".asp")) {
			req.getRequestDispatcher(req.getRequestURI().replace(req.getContextPath(), "").replace(".asp", ".jsp")).forward(req, res);
		}
	}

	public void init(FilterConfig config) throws ServletException {
	}
}