package dfh.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.caucho.hessian.util.IntMap;

/**
 * Throttles the filter to only a limited number of requests.
 */
public class ThrottleFilter implements Filter {
	private static Logger log = Logger.getLogger(ThrottleFilter.class);

	private IntMap _throttleCache = new IntMap();

	private int _maxConcurrentRequests = 2;

	/**
	 * Sets the maximum number of concurrent requests for a single IP.
	 */
	public void setMaxConcurrentRequests(int max) {
		_maxConcurrentRequests = max;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain nextFilter) throws ServletException, IOException {
		String ip = request.getRemoteAddr();
		boolean isOverflow;

		synchronized (this) {
			int count = _throttleCache.get(ip);

			if (count <= 0)
				count = 0;

			if (count < _maxConcurrentRequests) {
				isOverflow = false;
				_throttleCache.put(ip, count + 1);
			} else
				isOverflow = true;
		}

		if (isOverflow) {
			log.info("'" + ip + "' has too many concurrent requests -- throttling.");

			if (response instanceof HttpServletResponse)
				((HttpServletResponse) response).sendError(503);
			return;
		}

		try {
			nextFilter.doFilter(request, response);
		} finally {
			synchronized (this) {
				int count = _throttleCache.get(ip);

				if (count <= 1)
					_throttleCache.remove(ip);
				else
					_throttleCache.put(ip, count - 1);
			}
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
