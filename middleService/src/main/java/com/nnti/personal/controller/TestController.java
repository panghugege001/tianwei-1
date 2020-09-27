package com.nnti.personal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nnti.common.cache.RedisCache;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.personal.model.dto.ServiceStatusDTO;
import com.nnti.personal.model.vo.ServiceStatus;
import com.nnti.personal.service.implementations.IDBTestService;

@RestController
public class TestController {
	
	private static Logger log = Logger.getLogger(TestController.class);
	
	@Autowired
	private IDBTestService dbTestService;
	@Autowired
    private RedisCache redisCache;
	
	@RequestMapping(value = "/testZoo/{loginName}")
	public String testZoo(@PathVariable("loginName") String loginName) {
		
		String time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));
		
		Boolean lockFlag = false;
		
		try {

			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {

			log.error("玩家：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			
			if (lockFlag) {
				
				log.info("正在处理" + loginName + "的请求，执行时间：" + time);
				
				Thread.sleep(5 * 1000);
				
				log.info("处理完成" + loginName + "的请求，执行时间：" + time + "，结束时间：" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date()));
			} else {
				
				log.error("玩家：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				
				return "请稍后重试！";
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} finally {
			
			try {
				
				lock.release();
			} catch (Exception e) {
				
				e.printStackTrace();
				log.error("玩家：" + loginName + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			}
		}
		
		return "服务异常";
	}
	
	@RequestMapping(value = "/checkServiceStatus")
	public ServiceStatusDTO checkServiceStatus() {
		
		ServiceStatusDTO serviceStatusDTO = new ServiceStatusDTO();
		List<ServiceStatus> statusList = new ArrayList<>();
		Integer zkStatus = 0;
		String exceptionMsg = null;
		serviceStatusDTO.setStatus(statusList);
		String lockName = "checkMidServiceStatus";
		Long zkStartMs= System.currentTimeMillis();
		String time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(lockName));
		
		Boolean lockFlag = false;
		
		try {

			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
			if(!lockFlag){
				zkStatus = 1;
				exceptionMsg = "未获取到锁";
			}
		} catch (Exception e) {

			log.error("测试MidserviceZK获取锁发生异常，异常信息：", e);
			zkStatus = 2;
			exceptionMsg = e.getMessage();
		} finally {
			if (lockFlag) {
				try {
					if (null != lock) {
						lock.release();
					}
				} catch (Exception e) {
					log.error("测试MidserviceZK释放锁发生异常，执行时间：" + time + "，异常信息：", e);
					zkStatus = 3;
					exceptionMsg = e.getMessage();
				}
			}
		}
		
		Long zkEndMs = System.currentTimeMillis();
		ServiceStatus zk = new ServiceStatus();
		zk.setExceptionMsg(exceptionMsg);
		zk.setName("MidServiceZKStatus");
		zk.setExecMs(zkEndMs - zkStartMs);
		zk.setStatus(zkStatus);
		statusList.add(zk);
		
		Integer databaseStatus = 0;
		try {
			exceptionMsg = null;
			dbTestService.masterDBTest();
		} catch (Exception e) {
			log.error("测试Midservice发生异常：", e);
			exceptionMsg = e.getMessage();
			databaseStatus = 1;
		}
		ServiceStatus db = new ServiceStatus();
		db.setName("MidServiceDBStatus");
		db.setStatus(databaseStatus);
		db.setExceptionMsg(exceptionMsg);
		db.setExecMs(System.currentTimeMillis() - zkEndMs);
		statusList.add(db);
		
		Long redisStartMs = System.currentTimeMillis();
		Integer redisStatus = 0;
		String redisTestName = "MidServiceRedisStatus";
		try {
			exceptionMsg = null;
			redisCache.testPut(redisTestName, "love you");
			Object redMsg = redisCache.testGetByKey(redisTestName);
			log.info(redisTestName + redMsg);
		} catch (Exception e) {
			log.error("测试MidserviceRedis发生异常：", e);
			redisStatus = 1;
			exceptionMsg = e.getMessage();
		}
		
		ServiceStatus red = new ServiceStatus();
		red.setName("MidServiceRedisStatus");
		red.setStatus(redisStatus);
		red.setExceptionMsg(exceptionMsg);
		red.setExecMs(System.currentTimeMillis() - redisStartMs);
		statusList.add(red);
		
		return serviceStatusDTO;
	}
}