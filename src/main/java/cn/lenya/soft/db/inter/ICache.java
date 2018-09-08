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
	public <T> void saveOrUpdate(String key, T object);

	public <T> void saveIfNotExist(String key, T object);

	public <T> void saveWithExpired(String key, T object, int seconds);

	public <T> T get(String key, Class<T> clazz);

	public Object get(String key);

	public long delete(String key);

	// ------------------List操作-------------------------
	public <T> void saveToListFromLeft(String key, T object);

	public <T> void saveToListFromRight(String key, T object);

	public <T> void saveToList(String key, T object, int length);

	public Object getTopFromList(String key);

	public <T> T getTopFromList(String key, Class<T> clazz);

	public <T> List<T> getFromListWithScope(String key, int start, int end);

	public <T> List<T> getAllFromList(String key);

	// ------------------Map操作-------------------------
	public void saveToMap(String key, Map<Object, Object> value);

	public <T> Set<T> getKeysFromMap(String key);

	public <T> List<T> getValuesFromMap(String key);

	public Map<Object, Object> getAllFromMap(String key);

	public <T> T getFromMap(String key, String mapKey);

	// -----------------Set操作--------------------------
	public <T> void saveToSet(String key, T value);

	public <T> Set<T> getFromSet(String key);

	// -----------------Sorted Set操作-------------------
	public <T> void saveToSortedSet(String key, double score, T value);

	public <T> Set<T> getFromSortedSet(String key, int start, int end);

}