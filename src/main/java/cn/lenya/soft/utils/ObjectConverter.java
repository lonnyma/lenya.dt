/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectConverter {

	
private static final Logger log = LoggerFactory.getLogger(ObjectConverter.class);
	
	public static <T> byte[] toByteArray(T object) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			bytes = bos.toByteArray();
//			log.debug("ObjectUtils.toObject invoked");
			oos.close();
			bos.close();
		} catch (IOException e) {
			log.error("IOException in ObjectUtils.toByteArray="+e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static Object toObject(byte[] bytes) {
		Object object = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			object =  ois.readObject();
//			log.debug("ObjectUtils.toObject invoked");
			ois.close();
			bis.close();
		} catch (IOException e) {
			log.error("IOException in ObjectUtils.toObject ：",e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException in ObjectUtils.toObject:",e.getMessage());
			e.printStackTrace();
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> toObject(List<byte[]> bytes) {
		List<T> result = new ArrayList<T>();
		for(byte[] aB : bytes) {
			Object object = toObject(aB);
			result.add((T) object);
		}
		return result;
	}
	
	public static Map<byte[],byte[]> toByteArray(Map<Object,Object> object) {
		Map<byte[],byte[]> result = new HashMap<byte[], byte[]>();
		byte[] mKey = null;
		byte[] mValue = null;
		Set<Entry<Object,Object>> set = object.entrySet();
		for(Iterator<Entry<Object,Object>> it = set.iterator(); it.hasNext();) {
			Entry<Object,Object> entry = it.next();
			mKey = toByteArray(entry.getKey());     
			mValue = toByteArray(entry.getValue());
			result.put(mKey, mValue);
		}
		return result;
	}    
	
	@SuppressWarnings("unchecked")
	public static <T> Set<T> toObject(Set<byte[]> object) {
		Set<T> result = new HashSet<T>();
		for(byte[] b : object) {
			Object o = toObject(b);
			result.add((T) o);
		}
		return result;
	}
	
	public static Map<Object,Object> toObject(Map<byte[],byte[]> object) {
		Map<Object,Object> result = new HashMap<Object, Object>();
		Object mKey = null;
		Object mValue = null;
		Set<Entry<byte[],byte[]>> set = object.entrySet();
		for(Iterator<Entry<byte[],byte[]>> it = set.iterator(); it.hasNext();) {
			Entry<byte[],byte[]> entry = it.next();
			mKey = ObjectConverter.toObject(entry.getKey());
			mValue = ObjectConverter.toObject(entry.getValue());
			result.put(mKey, mValue);
		}
		return result;
	}
	

	
}
