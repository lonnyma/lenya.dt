package cn.lenya.soft.db.redis;

import java.util.Properties;

import redis.clients.jedis.JedisPoolConfig;

/*
 * 此类用于配置连接池
 */
public class RedisCachePoolConfigProvider {

	
		public static JedisPoolConfig getRedisPoolConfig() {
			Properties p = RedisCacheConfiguration.configuration().configurationRedisPool();
			if(p == null) {
				throw new IllegalStateException("JedisPoolConfig is must,but not it is null,please check");
			}
			JedisPoolConfig jpc = new JedisPoolConfig();
			
			int maxactive = Integer.parseInt(p.getProperty("maxactive"));
			long maxwait = Long.parseLong(p.getProperty("maxwait"));
			int maxidle = Integer.parseInt(p.getProperty("maxidle"));
			boolean testOnBorrow = Boolean.parseBoolean( p.get("testOnBorrow").toString());
			boolean testOnReturn = Boolean.parseBoolean(p.getProperty("testOnReturn").toString());
			
			jpc.setMaxActive(maxactive);
			jpc.setMaxWait(maxwait);
			jpc.setMaxIdle(maxidle);
			jpc.setTestOnBorrow(testOnBorrow);
			jpc.setTestOnReturn(testOnReturn);
			
			//jpc.setTestWhileIdle(true);//minEvictableIdleTimeMillis  timeBetweenEvictionRunsMillis  numTestsPerEvictionRun 
			//jpc.setMinEvictableIdleTimeMillis(60000);
			//jpc.setTimeBetweenEvictionRunsMillis(30000);		
			//jpc.setNumTestsPerEvictionRun(-1);
			return jpc;
		}
}