package dfh.extend.zookeeper;

import dfh.utils.Configuration;

public class GenerateNodePath {

	private static String BrandPrefix = Configuration.getInstance().getValue("brand.prefix");

	public static String generateUserLockForUpdate(String loginname) {

		return "/" + BrandPrefix + "/" + NodeEnum.USER_LOCK_FOR_UPDATE.getNodepath() + "/" + loginname;
	}
}