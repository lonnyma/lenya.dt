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

import cn.lenya.soft.core.common.utils.ClassUtil;
import redis.clients.jedis.JedisShardInfo;

/*
 * 此类读取redis-nospring.xml
 */
public class RedisCacheConfiguration {

	
		private static final Logger log = LoggerFactory.getLogger(RedisCacheConfiguration.class);
		
		private static final String path = "redis-nospring.xml";
		private static Properties poolConfigProperties = null;
		private static List<JedisShardInfo> shards = null;
		
		private static RedisCacheConfiguration cacheRedisConfiguration = null;
		
		public static RedisCacheConfiguration configuration() {
			if(cacheRedisConfiguration == null) {
				synchronized (RedisCacheConfiguration.class) {
					cacheRedisConfiguration = new RedisCacheConfiguration();
				}
			}
			return cacheRedisConfiguration;
		}
		
				
		private RedisCacheConfiguration(){
			InputStream is = new ClassUtil().loadResource(path);
			if(is ==null) {
				log.error(path + " is not found !!=============");
			}else {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				
				SAXReader reader = new SAXReader();
				
				try {
					Document document = reader.read(is);
					Element root = document.getRootElement();
					for(Iterator<?> it = root.elementIterator(); it.hasNext();) {
						Element e = (Element) it.next();
						System.out.println("element:"+e.getName());
						if(e.getName().equalsIgnoreCase("jedispoolconfig")) {
							poolConfigProperties = new Properties();
							for(Iterator<?> it2 = e.elementIterator();it2.hasNext();) {
								Element e2 = (Element) it2.next();
								poolConfigProperties.setProperty(e2.getName(), e2.getStringValue());
							}
						}
						
						if(e.getName().equalsIgnoreCase("jedisshardinfos")) {
							shards = new ArrayList<JedisShardInfo>();
							
							for(Iterator<?> it1 = e.elementIterator(); it1.hasNext();) {
								Element e1 = (Element) it1.next();
								if(e1.getName().equalsIgnoreCase("jedisshardinfo")) {
									JedisShardInfo jsi = null;
									String ip = null;
									int port = 0;
									for(Iterator<?> it11 = e1.attributeIterator();it11.hasNext();) {
										Attribute att = (Attribute) it11.next();
										if(att.getName().equalsIgnoreCase("ip")) {
											ip = att.getStringValue();
										}
										else if(att.getName().equalsIgnoreCase("port")) {
											port = Integer.parseInt(att.getStringValue());
										}
									}
								//	jsi = new JedisShardInfo(ip, port,100000);
									jsi = new JedisShardInfo(ip, port);
									shards.add(jsi);
								}
							}
						}
					}
				} catch (DocumentException de) {
					log.error("src/redis-nospring.xml，读取出错", de);
					throw new IllegalStateException("src/redis-nospring.xml，读取出错");
				}
			}
			
		}
		
		public Properties configurationRedisPool() {
			if(poolConfigProperties!=null) {
				Iterator<Entry<Object, Object>> it = poolConfigProperties.entrySet().iterator();  
		        while (it.hasNext()) {  
		            Entry<Object, Object> entry = it.next();  
		            Object key = entry.getKey();  
		            Object value = entry.getValue();  
		            System.out.println("key   :" + key);  
		            System.out.println("value :" + value);  
		            System.out.println("---------------");  
		        }  
			}
			else {
				System.out.println("poolConfigProperties is null");
			}
			return poolConfigProperties;
		}
		
		public List<JedisShardInfo> configurationJedisShardInfos() {
			return shards;
		}
		
		
	}
