/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.inter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICache {

	// 以下定义接口

	// ---------------------最质朴---------------------
	 <T> void saveOrUpdate(String key, T object);

	 <T> void saveIfNotExist(String key, T object);

	 <T> void saveWithExpired(String key, T object, int seconds);

	 <T> T get(String key, Class<T> clazz);

	 Object get(String key);

	 long delete(String key);

	// ------------------List操作-------------------------
	 <T> void saveToListFromLeft(String key, T object);

	 <T> void saveToListFromRight(String key, T object);

	 <T> void saveToList(String key, T object, int length);

	 Object getTopFromList(String key);

	 <T> T getTopFromList(String key, Class<T> clazz);

	 <T> List<T> getFromListWithScope(String key, int start, int end);

	 <T> List<T> getAllFromList(String key);

	// ------------------Map操作-------------------------
	 void saveToMap(String key, Map<Object, Object> value);

	 <T> Set<T> getKeysFromMap(String key);

	 <T> List<T> getValuesFromMap(String key);

	 Map<Object, Object> getAllFromMap(String key);

	 <T> T getFromMap(String key, String mapKey);

	// -----------------Set操作--------------------------
	 <T> void saveToSet(String key, T value);

	 <T> Set<T> getFromSet(String key);

	// -----------------Sorted Set操作-------------------
	 <T> void saveToSortedSet(String key, double score, T value);

	 <T> Set<T> getFromSortedSet(String key, int start, int end);

}