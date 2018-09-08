package cn.lenya.soft.db.redis;

import java.util.List;

import redis.clients.jedis.JedisShardInfo;

public class RedisCacheJedisShardInfoProvider {

	public static List<JedisShardInfo> getJedisShardInfos() {
		return RedisCacheConfiguration.configuration().configurationJedisShardInfos();
	}
}
