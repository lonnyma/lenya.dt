package cn.lenya.soft.db.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;



public class BaseDaoHelper<T> extends BaseDao implements Runnable {
	 //���̼߳�����,���������е��߳���	 
    private CountDownLatch runningThreadNum;   	
	private List<?> result = null;
	private List<Map<String, Object>> rslist=null;
	private Class<T> clazz = null;
	private Map<String, Object> orm=null;
	
	public BaseDaoHelper (CountDownLatch runningThreadNum,List<Map<String, Object>> rslist,
			Class<T> clazz, Map<String, Object> orm){
		 this.runningThreadNum= runningThreadNum;
		 this.rslist = rslist;
		 this.clazz=clazz;
		 this.orm=orm;
	}
	
	
	public void run() {
		try {
			result = getList(rslist, clazz, orm);
			runningThreadNum.countDown();//�������е��߳����һ
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<?> getResult() {
		return result;
	}

	public void setResult(List<?> result) {
		this.result = result;
	}

	public List<Map<String, Object>> getRslist() {
		return rslist;
	}

	public void setRslist(List<Map<String, Object>> rslist) {
		this.rslist = rslist;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Map<String, Object> getOrm() {
		return orm;
	}

	public void setOrm(Map<String, Object> orm) {
		this.orm = orm;
	}

	public CountDownLatch getRunningThreadNum() {
		return runningThreadNum;
	}

	public void setRunningThreadNum(CountDownLatch runningThreadNum) {
		this.runningThreadNum = runningThreadNum;
	}
	
	
	

}
