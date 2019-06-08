/* Copyright (c) 2018 鐧界緤浜哄伐鏅鸿兘鍦ㄧ嚎鎶�鏈�. All rights reserved.
 * http://www.byond.cn
 */
package cn.lenya.soft.db.redis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.lenya.soft.utils.ClassUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/*
 * redis-nospring.xml
 */
public class RedisCacheConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RedisCacheConfiguration.class);

	private static final String path = "redis-nospring.xml";
	private static Properties poolConfigProperties = null;
	private static List<JedisShardInfo> shards = null;

	private static RedisCacheConfiguration cacheRedisConfiguration = null;

	public static RedisCacheConfiguration configuration() {
		if (cacheRedisConfiguration == null) {
			synchronized (RedisCacheConfiguration.class) {
				cacheRedisConfiguration = new RedisCacheConfiguration();
			}
		}
		return cacheRedisConfiguration;
	}

	private RedisCacheConfiguration() {
		InputStream is = new ClassUtil().loadResource(path);
		if (is == null) {
			log.error(path + " is not found !!=============");
		} else {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");

			SAXReader reader = new SAXReader();

			try {
				Document document = reader.read(is);
				Element root = document.getRootElement();
				for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
					Element e = (Element) it.next();
					log.info("element:{}", e.getName());
					if (e.getName().equalsIgnoreCase("jedispoolconfig")) {
						poolConfigProperties = new Properties();
						for (Iterator<?> it2 = e.elementIterator(); it2.hasNext();) {
							Element e2 = (Element) it2.next();
							poolConfigProperties.setProperty(e2.getName(), e2.getStringValue());
						}
					}

					if (e.getName().equalsIgnoreCase("jedisshardinfos")) {
						shards = new ArrayList<JedisShardInfo>();

						for (Iterator<?> it1 = e.elementIterator(); it1.hasNext();) {
							Element e1 = (Element) it1.next();
							if (e1.getName().equalsIgnoreCase("jedisshardinfo")) {
								JedisShardInfo jsi = null;
								String ip = null;
								int port = 0;
								for (Iterator<?> it11 = e1.attributeIterator(); it11.hasNext();) {
									Attribute att = (Attribute) it11.next();
									if (att.getName().equalsIgnoreCase("ip")) {
										ip = att.getStringValue();
									} else if (att.getName().equalsIgnoreCase("port")) {
										port = Integer.parseInt(att.getStringValue());
									}
								}
								// jsi = new JedisShardInfo(ip, port,100000);
								jsi = new JedisShardInfo(ip, port);
								shards.add(jsi);
							}
						}
					}
				}
			} catch (DocumentException de) {
				log.error("src/redis-nospring.xml parse failed ", de);
				throw new IllegalStateException("src/redis-nospring.xml parse failed !!");
			}
		}

	}
	public static List<JedisShardInfo> getJedisShardInfos() {
		return configuration().configurationJedisShardInfos();
	}
	
	public static JedisPoolConfig getRedisPoolConfig() {
		Properties p = RedisCacheConfiguration.configuration().configurationRedisPool();
		if (p == null) {
			throw new IllegalStateException("JedisPoolConfig is must,but not it is null,please check");
		}
		JedisPoolConfig jpc = new JedisPoolConfig();

		int maxactive = Integer.parseInt(p.getProperty("maxactive"));
		long maxwait = Long.parseLong(p.getProperty("maxwait"));
		int maxidle = Integer.parseInt(p.getProperty("maxidle"));
		boolean testOnBorrow = Boolean.parseBoolean(p.get("testOnBorrow").toString());
		boolean testOnReturn = Boolean.parseBoolean(p.getProperty("testOnReturn").toString());

		jpc.setMaxTotal(maxactive);
		jpc.setMaxWaitMillis(maxwait);
		jpc.setMaxIdle(maxidle);
		jpc.setTestOnBorrow(testOnBorrow);
		jpc.setTestOnReturn(testOnReturn);

		// jpc.setTestWhileIdle(true);
		// jpc.setMinEvictableIdleTimeMillis(60000);
		// jpc.setTimeBetweenEvictionRunsMillis(30000);
		// jpc.setNumTestsPerEvictionRun(-1);
		return jpc;
	}
	
	public Properties configurationRedisPool() {
		if (poolConfigProperties != null) {
			Iterator<Entry<Object, Object>> it = poolConfigProperties.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				log.info("key :{} ,value :{}" ,key, value);
				log.info("---------------");
			}
		} else {
			System.out.println("poolConfigProperties is null");
		}
		return poolConfigProperties;
	}

	public List<JedisShardInfo> configurationJedisShardInfos() {
		return shards;
	}
	
}
