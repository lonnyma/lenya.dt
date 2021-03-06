/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.dao.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import cn.lenya.soft.db.inter.ISQLBase;

public class BaseDao extends BaseDaoImpl implements ISQLBase{

	
	private JdbcTemplate ds;
	
	 private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	public void setDs(DataSource ds) {
		this.ds = new JdbcTemplate(ds);
	}
	
	public void setJdbcTemplate(JdbcTemplate ds) {
		this.ds = ds;
	}	
	
	
	
	public void setNamedJdbcTemplate(JdbcTemplate ds) {
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(String sql, Object[] args,Class<T> clazz,Map<String, Object> orm) throws Exception {
		 T rs = null;
		try {		
			//Map<String,Object> obj = getMap(sql, args);			
			rs= (T) initBean(getMap(sql, args), clazz, orm);
			
		} catch (Exception e) {			
			//e.printStackTrace();
		}		
		return rs;
	}
	
	public <T> T getObject(Map<String, Object> rs, Class<T> clazz,
			Map<String, Object> orm) throws Exception {
		
		return super.getObject(rs, clazz, orm);
	}

	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getMap(String sql, Object[] args)
			throws Exception {			
			return (Map<String, T>) ds.queryForMap(sql, args);
	}

	@SuppressWarnings("unchecked")
	public <T> Map<String, T>getObjectMap(String sql, Object[] args,Class<?> clazz,Map<String, Object> orm)
	throws Exception {		
		Map<String, T> rs = new HashMap<String, T>();
		Map<String, Object> rss = ds.queryForMap(sql, args);
		Map<String, Field> st = new HashMap<String, Field>();
		for(Object field:BaseDao.getAllFields(st , clazz).keySet()){		
			
			if(rss.containsKey(orm.get(field.toString()))){				
				String key = (String) orm.get(field.toString());
				rs.put(field.toString().toString(),  (T) rss.get(key));
			}
		}			
			return rs;
	}
	
	public <T> Map<String, T> getObjectMap(Map<String, Object> rs0,
			Class<T> clazz, Map<String, Object> orm) throws Exception {
		return super.getObjectMap(rs0, clazz, orm);
//		init(rs0,clazz,orm);
//		Map<String, T> rs = new HashMap<String, T>();
//		Map<String, Field> st = new HashMap<String, Field>();
//		for(Object field:BaseDao.getAllFields(st , clazz).keySet()){		
//			
//			if(rs0.containsKey(orm.get(field.toString()))){				
//				String key = (String) orm.get(field.toString());
//				rs.put(field.toString().toString(),  (T) rs0.get(key));
//			}
//		}			
//			return rs;
	}
	


	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String sql, Object[] args,Class<T> clazz,Map<String, Object> orm) throws Exception {		
	
		List<T> rs = new ArrayList<T>();
		try {			
			 List<Map<String, Object>> rslist = ds.queryForList(sql, args);
			 for(Map<String, Object> obj:rslist){
				 rs.add((T) initBean(obj, clazz, orm));		
				}
		} catch (Exception e) {			
		}		
		return rs;		
	}


	public <T> List<T> getList(List<Map<String, Object>> rslist,
			Class<T> clazz, Map<String, Object> orm) throws Exception {
		return super.getList(rslist, clazz, orm);	
	}



	public boolean saveOrUpdate (String sql, Object[] args)throws DataAccessException,SQLException {
		int rs = ds.update(sql, args);
		if(rs>=0)return true;
		return false;
	}

	public boolean saveOrUpdate (String sql, MapSqlParameterSource args)throws DataAccessException,SQLException {
		setNamedJdbcTemplate(ds);
		int rs = namedJdbcTemplate.update(sql, args);
		if(rs>=0)return true;
		return false;
	}
	
	
	public Integer getCount(String sql, Object[] args) {
		return ds.queryForObject(sql, Integer.class,args);
	}

	public int saveOrUpt (String sql, Object[] args)throws DataAccessException {
		return ds.update(sql, args);
		
	}



}
