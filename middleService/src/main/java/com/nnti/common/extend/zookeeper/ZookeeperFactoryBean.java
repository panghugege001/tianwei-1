package com.nnti.common.extend.zookeeper;

import java.util.List;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import com.nnti.common.utils.ConfigUtil;

public class ZookeeperFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public static CuratorFramework zkClient;

	private List<IZKListener> listeners;

	public void setListeners(List<IZKListener> listeners) {
		this.listeners = listeners;
	}
	
	@Override
	public void destroy() throws Exception {
		
		zkClient.close();
	}
	
	// 创建ZK连接
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// 1000 是重试间隔时间基数，3 是重试次数
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		zkClient = createWithOptions(ConfigUtil.getValue("zk.conn.url"), retryPolicy, 2000, 10000);
		registerListeners(zkClient);
		zkClient.start();
	}
	
	@Override
	public CuratorFramework getObject() throws Exception {
		
		return zkClient;
	}

	@Override
	public Class<?> getObjectType() {
		
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		
		return true;
	}

	/**
	 * 通过自定义参数创建
	 */
	public CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
		
		return CuratorFrameworkFactory.builder().connectString(connectionString).retryPolicy(retryPolicy).connectionTimeoutMs(connectionTimeoutMs).sessionTimeoutMs(sessionTimeoutMs).build();
	}
	
	// 注册需要监听的监听者对像.
	private void registerListeners(CuratorFramework client) {
		
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
					
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {

				logger.info("CuratorFramework state changed: " + newState);
				
				if (newState == ConnectionState.CONNECTED || newState == ConnectionState.RECONNECTED) {
					
					for (IZKListener listener : listeners) {
						
						listener.executor(client);
						
						logger.info("Listener " + listener.getClass().getName() + " executed!");
					}
				}
			}
		});
		
		client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
			
			@Override
			public void unhandledError(String message, Throwable e) {
				
				logger.info("CuratorFramework unhandledError: " + message);
			}
		});
	}
}