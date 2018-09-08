/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringUtil implements ApplicationContextAware  {
	private static BeanFactory factory = null;
	private static SpringUtil springUtil = null;
	private static ApplicationContext appContext = null;
	private static final String appContextPath = "springContext.xml";
	
	public SpringUtil(){
		
	}

	
	public SpringUtil(ApplicationContext context){
		if(context == null){
			startSpringContainer();
		}else{
			appContext = context;
			factory = context;
		}
		
	}
	
	
	public static synchronized SpringUtil getInstance() {
		if (springUtil == null) {
			springUtil = new SpringUtil(appContext);
		}
		return springUtil;
	}

	
	/**
	 * 初始化上下问，并初始化工厂bean
	 */
	protected static void  initBeanFactory() {
		
		if(appContext == null){
			appContext = new ClassPathXmlApplicationContext(appContextPath);
		}
		if (factory == null) {
			//factory = new ClassPathXmlApplicationContext("springContext.xml");			
			factory = appContext;
		}
	}

	//启动容器
	public void startSpringContainer(){		
		initBeanFactory();
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringUtil.factory=applicationContext;		
	}
	
	public ApplicationContext getAppContext(){
		return appContext;
	}
	
	public static Object getBean(String id) {
		return factory.getBean(id);
	}

	public <T> T getBean(String name, Class<T> clazz) {

		return (T) factory.getBean(name, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> t) {
		return (T) factory.getBean(getCommonBeanName(t));
	}

	private static String getCommonBeanName(Class<?> clazz) {
		String name = clazz.getSimpleName();
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}


}
