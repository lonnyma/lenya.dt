package cn.lenya.soft.db.dao;

import java.util.List;
import java.util.Map;
/**
 * 作用：数据库结果集处理接口,主要用于对象转换
 * @author lONNY MA
 *
 */
public interface IBaseDao  {
	

	/**
	 * 功能描述：将结果转换成对象，并用list装载
	 * @param rslist
	 * @param clazz
	 * @param orm
	 * @return
	 * @throws Exception
	 */
	<T> List<T> getList(List<Map<String, Object>> rslist,Class<T> clazz,Map<String, Object> orm)throws Exception;
	
	<T> List<T> getList(String sql, Object[] args,Class<T> clazz,Map<String, Object> orm)throws Exception;
	
	<T> T getObject(Map<String, Object> rs0,Class<T> clazz,Map<String, Object> orm)throws Exception;
	
	<T> T getObject(String sql, Object[] args,Class<T> clazz,Map<String, Object> orm)throws Exception;
	
	<T> Map<String,T> getObjectMap(Map<String, Object> rs0,Class<T> clazz,Map<String, Object> orm)throws Exception;
	
	<T> Map<String,T> getObjectMap(String sql, Object[] args,Class<?> clazz,Map<String, Object> orm)throws Exception;
}
