package cn.lenya.soft.db.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lenya.soft.db.inter.ICache;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

public class RedisCacheFactory {
private static final Logger log = LoggerFactory.getLogger(RedisCacheFactory.class);
	
	private static JedisPoolConfig poolConfig = null;
	private static List<JedisShardInfo> shards = null;
	private static ShardedJedisPool pool = null;
	private static ICache cache = null;
	
	private static void configJedisPool() {
		log.info("configJedisPool");
		poolConfig = RedisCachePoolConfigProvider.getRedisPoolConfig();
	}
	
	private static void initJedisShardInfo() {
		log.info("initJedisShardInfo");
		shards = RedisCacheJedisShardInfoProvider.getJedisShardInfos();
		if(shards.size()<1) {
			throw new IllegalStateException("JedisShardInfo is must,but not it is null,please check");
		}
	}
	
	/*
	 * 
	 */
	public static ShardedJedisPool initShardedJedisPool() {
		log.info("initShardedJedisPool");
		if(poolConfig == null) {
			configJedisPool();
		}
		if(shards == null) {
			initJedisShardInfo();
		}
		log.info("redis instance numbers="+shards.size());
		
		//可能这里需要加个参数
		//Redis实现集群的方法主要是采用一致性哈稀分片（Shard），将不同的key分配到不同的redis server上，达到横向扩展的目的
		pool = new ShardedJedisPool(poolConfig, shards,Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);
		//pool = new ShardedJedisPool(poolConfig, shards);
		if(pool!=null) {
			return pool; 
		}
		log.info("pool is null");
		return null;
//		return pool;
	}
	
	public static ICache getRedis() {
		log.info("从RedisCacheFactory取得一个Redis");
		//在CacheRedisImpl里，有检查pool
//		if(pool == null) {
//			initShardedJedisPool();
//		}
		if(cache == null) {
			log.info("cache is null,so new CacheRedisImpl");
			cache = new RedisCacheImpl();
			return cache;
		}
		log.info("get Already exists Redis instance");
		return cache;
	}
}
