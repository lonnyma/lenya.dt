/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lenya.soft.core.bean.Base;
import cn.lenya.soft.db.dao.IBaseDao;

/**
 * 作用：数据库结果集处理接口实现类
 * 
 * @author Lonny Ma
 * @Date 2015.9.9
 *
 */
public abstract class BaseDaoImpl extends Base implements IBaseDao {

	public abstract <T> T getObject(String sql, Object[] args, Class<T> clazz, Map<String, Object> orm)
			throws Exception;

	@SuppressWarnings("unchecked")
	public <T> T getObject(Map<String, Object> rs, Class<T> clazz, Map<String, Object> orm) throws Exception {
		T rs0 = (T) initBean(rs, clazz, orm);
		return rs0;
	}

	public abstract <T> Map<String, T> getMap(String sql, Object[] args) throws Exception;

	public abstract <T> Map<String, T> getObjectMap(String sql, Object[] args, Class<?> clazz, Map<String, Object> orm)
			throws Exception;

	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getObjectMap(Map<String, Object> rs0, Class<T> clazz, Map<String, Object> orm)
			throws Exception {
		init(rs0, clazz, orm);
		Map<String, T> rs = new HashMap<String, T>();
		Map<String, Field> st = new HashMap<String, Field>();
		for (Object field : BaseDaoImpl.getAllFields(st, clazz).keySet()) {

			if (rs0.containsKey(orm.get(field.toString()))) {
				String key = (String) orm.get(field.toString());
				rs.put(field.toString().toString(), (T) rs0.get(key));
			}
		}
		return rs;
	}

	public abstract <T> List<T> getList(String sql, Object[] args, Class<T> clazz, Map<String, Object> orm)
			throws Exception;

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(List<Map<String, Object>> rslist, Class<T> clazz, Map<String, Object> orm)
			throws Exception {
		List<T> rs = new ArrayList<T>();

		for (Map<String, Object> obj : rslist) {
			rs.add((T) initBean(obj, clazz, orm));
		}

		return rs;
	}
}