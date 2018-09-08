/* Copyright (c) 2018 白羊人工智能在线技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.redis;

import java.util.List;

import redis.clients.jedis.JedisShardInfo;

public class RedisCacheJedisShardInfoProvider {

	public static List<JedisShardInfo> getJedisShardInfos() {
		return RedisCacheConfiguration.configuration().configurationJedisShardInfos();
	}
}
