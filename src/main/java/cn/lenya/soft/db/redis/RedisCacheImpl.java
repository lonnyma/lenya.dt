package cn.lenya.soft.db.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lenya.soft.db.inter.ICache;
import cn.lenya.soft.utils.ObjectConverter;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;

/*
 * 兼容CacheRedisFactory，当spring失效时，自己加载非Spring配置文件；
 * 为了兼容通过纯Spring的方式；
 * 也为了兼容CacheRedisSpringFactory，自己加载Spring配置文件。
 */
public class RedisCacheImpl implements ICache {

	private static final Logger log = LoggerFactory
			.getLogger(RedisCacheImpl.class);

	private static ShardedJedisPool shardedJedisPool = null;

	private static final int ONE_HOURS = 1000 * 60 * 60;

	// private static ShardedJedis jedis = null;

	public RedisCacheImpl() {
		log.info("----------------Redis init begining  --------");
		checkShardedJedisPool();
	}

	private void checkShardedJedisPool() {
		if (shardedJedisPool == null) {
			shardedJedisPool = RedisCacheFactory.initShardedJedisPool();
		}

	}

	private ShardedJedis checkShardedJedis() {
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = shardedJedisPool.getResource();
		} catch (JedisException e) {
			log.error("failed:jedisPool getResource.", e);
			isBroken = false;
			if (jedis != null) {
				release(jedis, isBroken);
			}
			throw e;
		}
		return jedis;
	}

	/**
	 * 功能描述：释放redis对象 ，返还连接
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	protected void release(ShardedJedis jedis, boolean isBroken) {
		if (jedis != null) {
			if (isBroken) {
				shardedJedisPool.returnBrokenResource(jedis);
			} else {
				shardedJedisPool.returnResource(jedis);
			}
		}
	}

	private void releaseShardedJedis(ShardedJedis jedis) {
		shardedJedisPool.returnResource(jedis);
	}

	// -------------------------------最质朴--------------------------------------
	public <T> void saveOrUpdate(String key, T object) {

		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(object);

			jedis.set(keyBytes, valueBytes);
		} catch (Exception e) {
			isBroken = true;
			// 释放redis对象 shardedJedisPool.returnBrokenResource(jedis);
			log.error("failed:" + key, e);
		} finally { // 返还到连接池
			// shardedJedisPool.returnResource(jedis);
			release(jedis, isBroken);
		}

	}

	/*
	 * 插入对象到名称为key的List
	 */
	public <T> void saveToListFromLeft(String key, T object) {
		ShardedJedis jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(object);
			jedis.lpush(keyBytes, valueBytes);
			releaseShardedJedis(jedis);
		}
	}

	/**
	 * 从右边插入对象到列表，配合从左边（lpop,lrange）取出的方法，就可以模拟一个队列。
	 * 
	 * @param key
	 * @param object
	 */
	public <T> void saveToListFromRight(String key, T object) {
		ShardedJedis jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(object);
			jedis.rpush(keyBytes, valueBytes);// 从右边放进去
			jedis.expire(keyBytes, ONE_HOURS);// 设置生命周期为一个小时
			releaseShardedJedis(jedis);
		}
	}

	// ------------------List操作-------------------------
	/*
	 * 返回名称为key的List的头部元素；相当于删除
	 */
	public Object getTopFromList(String key) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = jedis.lpop(keyBytes);
			if (valueBytes == null)
				return null;
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(valueBytes);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public <T> T getTopFromList(String key, Class<T> clazz) {
		return (T) getTopFromList(key);
	}

	/**
	 * 功能描述： 返回名称为key的List的指定范围的值,左边（lrange）出列表
	 */
	public <T> List<T> getAllFromList(String key) {
		List<byte[]> value = null;
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			if (jedis != null) {
				byte[] keyBytes = key.getBytes();
				value = jedis.lrange(keyBytes, 0, jedis.llen(key).intValue());
				if (value == null) {
					return null;
				}
			}
		} catch (Exception e) {
			isBroken = true;
			log.error("failed:" + key, e);
		} finally { // 返还到连接池
			release(jedis, isBroken);
		}
		return ObjectConverter.toObject(value);
	}

	/*
	 * 返回名称为key的List的指定范围的值,左边（lrange）出列表
	 */
	public <T> List<T> getFromListWithScope(String key, int start, int end) {
		List<byte[]> value = null;
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			if (jedis != null) {
				byte[] keyBytes = key.getBytes();
				value = jedis.lrange(keyBytes, start, end);
				if (value == null) {
					return null;
				}

			}
		} catch (Exception e) {
			isBroken = true;
			log.error("failed:" + key, e);
		} finally { // 返还到连接池
			release(jedis, isBroken);
		}
		return ObjectConverter.toObject(value);
	}

	/*
	 * 插入对象到名称为key的List. 可以设置List的长度。 当插入大于长度的对象时，舍弃最早进入的对象
	 */
	public <T> void saveToList(String key, T object, int length) {
		String value = null;
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			if (jedis != null) {
				byte[] keyBytes = key.getBytes();
				byte[] valueBytes = ObjectConverter.toByteArray(object);
				jedis.lpush(keyBytes, valueBytes);

				long len = jedis.llen(keyBytes);
				if (len > length) {
					int i = (int) (len - length);
					for (int j = 0; j < i; j++) {
						jedis.rpop(keyBytes);
					}
				}
				log.info("len=" + jedis.llen(keyBytes));
			}
		} catch (Exception e) {
			isBroken = true;
			log.error("failed:" + key + " " + value, e);
		} finally { // 返还到连接池
			release(jedis, isBroken);
		}

	}

	// -----------------------------Map----------------------
	/*
	 * 插入Map对象到名称为key的Map
	 */
	public void saveToMap(String key, Map<Object, Object> value) {
		ShardedJedis jedis =  checkShardedJedis();
		if (jedis != null) {
			// 用来打印在集群时，选择了哪个Host
			// log.info("host="+jedis.getShard(key).getClient().getHost());
			log.info("saveToMap,key=" + key + ",map.size=" + value.size());
			byte[] keyBytes = key.getBytes();
			Map<byte[], byte[]> valueBytes = ObjectConverter.toByteArray(value);
			jedis.hmset(keyBytes, valueBytes);
			releaseShardedJedis(jedis);
		}
	}

	/*
	 * 返回名称为key的Map的key值；值类型是Set
	 */
	public <T> Set<T> getKeysFromMap(String key) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			Set<byte[]> value = jedis.hkeys(keyBytes);
			if (value == null) {
				return null;
			}
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(value);
		}
		return null;

	}

	/*
	 * 返回名称为key的Map的value值；值类型是List
	 */
	public <T> List<T> getValuesFromMap(String key) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			List<byte[]> values = (List<byte[]>) jedis.hvals(keyBytes);
			if (values == null) {
				return null;
			}
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(values);
		}
		return null;
	}

	/*
	 * 返回名称为key的Map的key-value值对；值类型是Map
	 */
	public Map<Object, Object> getAllFromMap(String key) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			Map<byte[], byte[]> values = jedis.hgetAll(keyBytes);
			if (values == null) {
				return null;
			}
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(values);
		}
		return null;
	}

	/*
	 * 返回名称为key的Map中的key为mapKeyp的value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromMap(String key, String mapKey) {
		Map<Object, Object> results = getAllFromMap(key);
		if (results.containsKey(mapKey)) {
			return (T) results.get(mapKey);
		}
		return null;
	}

	// ----------------------------Set--------------------------
	/*
	 * 插入对象到名称为key的Set;插入对象可以是POJO，也可以是Set
	 */
	public <T> void saveToSet(String key, T value) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(value);
			jedis.sadd(keyBytes, valueBytes);
			shardedJedisPool.returnResource(jedis);
		}
	}

	/*
	 * 返回名称为key的Set的值
	 */
	public <T> Set<T> getFromSet(String key) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			Set<byte[]> values = jedis.smembers(keyBytes);
			if (values == null) {
				return null;
			}
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(values);
		}
		return null;
	}

	// ---------------------------Sorted Set-------------------
	/*
	 * 插入对象到名称为key的Sorted Set
	 */
	public <T> void saveToSortedSet(String key, double score, T value) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(value);
			jedis.zadd(keyBytes, score, valueBytes);
			releaseShardedJedis(jedis);
		}
	}

	/*
	 * 返回名称为key的Sorted Set的值
	 */
	public <T> Set<T> getFromSortedSet(String key, int start, int end) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			Set<byte[]> values = jedis.zrange(keyBytes, start, end);
			if (values == null) {
				return null;
			}
			releaseShardedJedis(jedis);
			return ObjectConverter.toObject(values);
		}
		return null;
	}

	/*
	 * 从缓存中取
	 */

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		return (T) get(key);
	}

	public Object get(String key) {
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			byte[] keyBytes = key.getBytes();
			byte[] values = jedis.get(keyBytes);
			if (values == null)
				return null;
			return ObjectConverter.toObject(values);
		} catch (Exception e) {
			isBroken = true;
			log.error("failed:" + key + " ", e);
		} finally { // 返还到连接池
			release(jedis, isBroken);
		}

		return null;

	}

	/*
	 * 保存，不会覆盖。
	 */
	public <T> void saveIfNotExist(String key, T object) {
		ShardedJedis jedis = null;
		jedis = checkShardedJedis();
		if (jedis != null) {
			byte[] keyBytes = key.getBytes();
			byte[] valueBytes = ObjectConverter.toByteArray(object);
			jedis.setnx(keyBytes, valueBytes);
			releaseShardedJedis(jedis);
		}
	}

	/*
	 * 可以设置过期
	 */
	public <T> void saveWithExpired(String key, T object, int seconds) {
		ShardedJedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = checkShardedJedis();
			if (jedis != null) {
				byte[] keyBytes = key.getBytes();
				byte[] valueBytes = ObjectConverter.toByteArray(object);
				jedis.setex(keyBytes, seconds, valueBytes);
			}
		} catch (Exception e) {
			isBroken = true;
			log.error("jedis =", e);
		} finally {
			release(jedis, isBroken);
		}
	}

	/*
	 * 删除某个缓存 返回值是删除的个数 如果key不存在，则返回0
	 */

	public long delete(String key) {

		ShardedJedis jedis = null;
		boolean isBroken = false;
		long number = 0;
		try {
			jedis = checkShardedJedis();
			if (jedis != null) {
				number = jedis.del(key);
			}
		} catch (Exception e) {
			isBroken = true;
			log.error("jedis =", e);
		} finally {
			release(jedis, isBroken);
		}
		return number;
	}

	public void closeResource(ShardedJedis jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					shardedJedisPool.returnBrokenResource(jedis);
				} else {
					shardedJedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				log.error(
						"Error happen when return jedis to pool, try to close it directly.",
						e);
			}
		}
	}

}
