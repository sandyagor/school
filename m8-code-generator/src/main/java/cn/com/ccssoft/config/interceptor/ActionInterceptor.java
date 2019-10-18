package cn.com.ccssoft.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.com.ccssoft.config.datasources.DataSourceNames;
import cn.com.ccssoft.config.datasources.DynamicDataSource;

public class ActionInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String dataSourceName = request.getParameter("dataSourceName");
		if(dataSourceName != null && !dataSourceName.equals("")) {
			DynamicDataSource.setDataSource(dataSourceName);
		}else {
			DynamicDataSource.setDataSource(DataSourceNames.FIRST);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
			throws Exception {
	}
}
