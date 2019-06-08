/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.inter;

import org.springframework.dao.DataAccessException;

public interface ISQLBase {
	/**
	 * 功能描述：更新数据库记录
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	boolean saveOrUpdate(String sql, Object[] args)throws DataAccessException,Exception;

	/**
	 * 功能描述：得到记录总数
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	Integer getCount(String sql, Object[] args);
}
