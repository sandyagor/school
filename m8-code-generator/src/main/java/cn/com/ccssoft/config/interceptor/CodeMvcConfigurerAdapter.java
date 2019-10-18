package cn.com.ccssoft.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CodeMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ActionInterceptor());
	}
}
