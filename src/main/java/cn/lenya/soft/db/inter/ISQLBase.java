package cn.lenya.soft.db.inter;

public interface ISQLBase {
	/**
	 * 功能描述：更新数据库记录
	 * @param sql
	 * @param args
	 * @return
	 */
	boolean saveOrUpdate(String sql, Object[] args);
	/**
	 * 功能描述：得到记录总数
	 * @param sql
	 * @param args
	 * @return
	 */
	Integer getCount(String sql, Object[] args);
}
