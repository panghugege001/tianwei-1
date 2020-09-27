package app.util;

import java.io.File;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import app.config.FTPProperties;

public class GiftClientUtil extends FTPClientUtil {
	
	private static Logger log = Logger.getLogger(GiftClientUtil.class);
	
	private static FTPClient ftpClient = null;
	private static final String SEPARATOR = "/";
	private static final String PATH = FTPProperties.getInstance().getValue("server_path_prefix") + FTPProperties.getInstance().getValue("gift_image_upload_url");
	
	public static String uploadFile(String fileName, File file) throws Exception {
	
		try {
		
			ftpClient = getFTPClient();
		} catch (Exception e) {
			
			log.error("GiftClientUtil工具类获取FTP客户端对象发生异常，异常信息：" + e.getMessage());
			
			throw e;
		}
		
		String filePath = "";
		
		if (createDir(ftpClient, PATH)) {
			
			String remoteFileName = uploadFile(ftpClient, fileName, file);
			
			filePath = PATH + SEPARATOR + remoteFileName;
			
			disposeFtpClient(ftpClient);
		}
		
		return filePath;
	}
}