/* Copyright (c) 2019 白羊智慧区块网络技术. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.redis;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 作用：
 * 
 * @version v1.0
 * @author Alpaca
 * @since 2019年6月7日
 */
@Configuration
public class RedisConfig {

	/**
	 * 集群节点
	 */
	@Value("${mredis.cluster.address}")
	private String clusterNodes;

	/**
	 * 访问超时限制
	 */
	@Value("${mredis.timeout}")
	private Long timeout;

	/**
	 * 读取超时限制
	 */
	@Value("${mredis.read.timeout}")
	private Long readTimeout;

	/**
	 * 登录密码
	 */
	@Value("${mredis.password}")
	private String password;

	/**
	 * 最大重连数
	 */
	@Value("${mredis.maxRedirects}")
	private int maxRedirects;

	/**
	 * 最大连接数
	 */
	@Value("${mredis.maxTotal}")
	private int maxTotal;

	/**
	 * 最大空闲连接数
	 */
	@Value("${mredis.maxIndle}")
	private int maxIndle;

	/**
	 * 最小空闲连接数
	 */
	@Value("${mredis.minIdle}")
	private int minIdle;

	/**
	 * 最大等待时间（单位毫秒） 默认-1
	 */
	@Value("${mredis.maxWaitMillis}")
	private int maxWaitMillis;

	/**
	 * 最大等待时间（单位毫秒） 默认-1
	 */
	@Value("${project.appName}")
	private String appName;

	/**
	 * 逐出连接的最小空闲时间
	 */
	private int minEvictableIdleTimeMillis = 300000;

	/**
	 * 次逐出检查时，逐出的最大数目
	 */
	private int numTestsPerEvictionRun = 3;
	/**
	 * 逐出扫描的时间间隔（毫秒），如果为负数，则不运行逐出线程，默认-1
	 */
	private int timeBetweenEvictionRunsMills = 60000;

	/**
	 * 在获取连接时检查有效性
	 */
	private boolean testOnBorrow = true;

	/**
	 * 返回连接池时是否进行校验
	 */
	private boolean testOnReturn = true;

	/**
	 * 空闲时检查有效性
	 */
	private boolean testWhileIdle = true;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
				.builder();
		jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
		jedisClientConfiguration.readTimeout(Duration.ofMillis(readTimeout));
		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder poolConfig = jedisClientConfiguration
				.usePooling();
		poolConfig.poolConfig(getJedisPoolConfig());
		return new JedisConnectionFactory(getJedisClusterConfiguration(), jedisClientConfiguration.build());
	}

	@Bean(name = "jedisClusterConfiguration")
	public RedisClusterConfiguration getJedisClusterConfiguration() {
		Map<String, Object> source = new LinkedHashMap<>();
		source.put("spring.redis.cluster.nodes", clusterNodes);
		source.put("spring.redis.cluster.max-redirects", maxRedirects);
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(
				new MapPropertySource("RedisClusterConfiguration", source));
		if (!StringUtils.isEmpty(password)) {
			redisClusterConfiguration.setPassword(password);
		}
		return redisClusterConfiguration;
	}

	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxIdle(maxIndle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMills);
		return jedisPoolConfig;
	}

	@Primary
	@Bean
	public JedisCluster getJedisCluster() {
		String[] nodes = clusterNodes.split(",");
		Set<HostAndPort> jedisClusterNodes = new HashSet<>();

		for (String node : nodes) {
			jedisClusterNodes.add(new HostAndPort(node.split(":")[0], Integer.parseInt(node.split(":")[1])));
		}
		if (!StringUtils.isEmpty(password)) {
			return new JedisCluster(jedisClusterNodes, timeout.intValue(), readTimeout.intValue(), 5, password,
					getJedisPoolConfig());
		}
		return new JedisCluster(jedisClusterNodes, timeout.intValue(), readTimeout.intValue(), 5, getJedisPoolConfig());

	}

	@Bean
	public JedisClusterConnection getJedisClusterConnection() {
		return (JedisClusterConnection) jedisConnectionFactory().getConnection();
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}

	@Bean(name = "redisAtomic")
	public RedisAtomicLong getRedisAtomicLong() {
		return new RedisAtomicLong(appName, jedisConnectionFactory());
	}
}
