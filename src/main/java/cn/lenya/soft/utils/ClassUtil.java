/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.utils;

import java.io.InputStream;

public class ClassUtil {

	
	public InputStream loadResource(String name) {
	        InputStream in = getClass().getResourceAsStream(name);
	        if (in == null) {
	            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	            if (in == null) {
	                in = getClass().getClassLoader().getResourceAsStream(name);
	            }
	        }
	        return in;
	    }
}
