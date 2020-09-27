package dfh.extend.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.log4j.Logger;

/**
 * 场景：监听后台有数据变更通知，才需要重新从数据库获取，否则直接走缓存
 * 
 * @author Erick
 *
 */
public class AnnocementListener implements IZKListener {

	private Logger logger = Logger.getLogger(this.getClass());

	private String nodePath;

	public AnnocementListener(String nodePath) {

		super();
		this.nodePath = nodePath;
	}

	@SuppressWarnings("resource")
	public void executor(CuratorFramework client) {

		final NodeCache cache = new NodeCache(client, nodePath, false);
		final NodeCache cache1 = new NodeCache(client, "/erick1/zootest1", false);

		try {

			cache.start();
			cache1.start();
		} catch (Exception e) {

			logger.error("cache.start error:" + e.getMessage());
		}

		cache.getListenable().addListener(new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {

				// 如果节点被删除，会报空指针异常 ， 恢复创建节点回归正常
				byte[] data = cache.getCurrentData().getData();

				if (null != data) {
					
					logger.info("cache---------->" + new String(data));
				}
			}
		});

		cache1.getListenable().addListener(new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {

				// 如果节点被删除，会报空指针异常 ， 恢复创建节点回归正常
				byte[] data = cache1.getCurrentData().getData();

				if (null != data) {

					logger.info("cache1---------->" + new String(data));
				}
			}
		});
	}
}