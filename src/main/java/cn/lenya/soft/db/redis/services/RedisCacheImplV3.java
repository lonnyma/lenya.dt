/* Copyright (c) 2019 白羊智慧区块网络技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.redis.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.lenya.soft.db.inter.ICache;
import redis.clients.jedis.JedisCluster;

/**
 * 作用： 
 * 
 * @version v1.0
 * @author Alpaca 
 * @since 2019年6月8日
 */
@Service("redisCacheImplV3")
public class RedisCacheImplV3 implements ICache {

	private static final Logger log = LoggerFactory.getLogger(RedisCacheImplV3.class);

	@Resource
	private JedisCluster jedisCluster;
	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveOrUpdate(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void saveOrUpdate(String key, T object) {
		//jedisCluster.
		//redisTemplate.opsForValue().set(key, object);
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveIfNotExist(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void saveIfNotExist(String key, T object) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveWithExpired(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public <T> void saveWithExpired(String key, T object, int expiredSeconds) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T get(String key, Class<T> clazz) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#delete(java.lang.String)
	 */
	@Override
	public long delete(String key) {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#delete(java.lang.String[])
	 */
	@Override
	public void delete(String... keys) {
		if(ArrayUtils.isEmpty(keys)) {
			throw new RuntimeException("输入需要删除的keys为空");
		}
		if(keys.length==1) {
			redisTemplate.delete(keys[0]);
		}else {
			redisTemplate.delete(Arrays.asList(keys));
		}
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToListFromLeft(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void saveToListFromLeft(String key, T object) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToListFromRight(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void saveToListFromRight(String key, T object) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToList(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public <T> void saveToList(String key, T object, int length) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getTopFromList(java.lang.String)
	 */
	@Override
	public Object getTopFromList(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getTopFromList(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getTopFromList(String key, Class<T> clazz) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getFromListWithScope(java.lang.String, int, int)
	 */
	@Override
	public <T> List<T> getFromListWithScope(String key, int start, int end) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getAllFromList(java.lang.String)
	 */
	@Override
	public <T> List<T> getAllFromList(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToMap(java.lang.String, java.util.Map)
	 */
	@Override
	public void saveToMap(String key, Map<Object, Object> value) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getKeysFromMap(java.lang.String)
	 */
	@Override
	public <T> Set<T> getKeysFromMap(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getValuesFromMap(java.lang.String)
	 */
	@Override
	public <T> List<T> getValuesFromMap(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getAllFromMap(java.lang.String)
	 */
	@Override
	public Map<Object, Object> getAllFromMap(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getFromMap(java.lang.String, java.lang.String)
	 */
	@Override
	public <T> T getFromMap(String key, String mapKey) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToSet(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void saveToSet(String key, T value) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getFromSet(java.lang.String)
	 */
	@Override
	public <T> Set<T> getFromSet(String key) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#saveToSortedSet(java.lang.String, double, java.lang.Object)
	 */
	@Override
	public <T> void saveToSortedSet(String key, double score, T value) {
		

	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#getFromSortedSet(java.lang.String, int, int)
	 */
	@Override
	public <T> Set<T> getFromSortedSet(String key, int start, int end) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#generateUUID()
	 */
	@Override
	public String generateUUID() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#setLock(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean setLock(String lockId, String requestId) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#lock(java.lang.String, java.lang.String, java.lang.Long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Boolean lock(String lockId, String requestId, Long validTime, TimeUnit timeUnit) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.lenya.soft.db.inter.ICache#unLock(java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean unLock(String lockId, String requestId) {
		
		return null;
	}

}
