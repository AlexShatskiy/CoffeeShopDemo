package com.sh.coffeeshop.config.core;

import com.sh.coffeeshop.config.AppConfig;
import com.sh.coffeeshop.config.SecurityConfig;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	//for utf-8 in spring
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		//create the root Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfig.class, SecurityConfig.class);

		servletContext.addListener(new ContextLoaderListener(rootContext));

		//Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		servletAppContext.register(AppConfig.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		// throw NoHandlerFoundException to controller ExceptionHandler.class. Used for <error-page> analogue
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

		//register and map the dispatcher servlet
		//note Dispatcher servlet with constructor arguments
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.addMappingForUrlPatterns(null, true, "/*");
	}
}