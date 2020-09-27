package app.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import app.config.FTPProperties;

public class PreferentialClientUtil extends FTPClientUtil {
	
	private static Logger log = Logger.getLogger(PreferentialClientUtil.class);
	
	private static FTPClient ftpClient = null;
	private static final String SEPARATOR = "/";
	private static final String PATH = FTPProperties.getInstance().getValue("server_path_prefix") + FTPProperties.getInstance().getValue("preferential_image_upload_url");
	
	public static String uploadFile(String fileName, File file) throws Exception {
		
		try {
		
			ftpClient = getFTPClient();
		} catch (Exception e) {
			
			log.error("PreferentialClientUtil工具类获取FTP客户端对象发生异常，异常信息：" + e.getMessage());
			
			throw e;
		}
		
		log.error("PreferentialClientUtil工具类成功获取FTP客户端对象...." + ftpClient);
		
		if (createDir(ftpClient, PATH)) {
			
			log.error("PreferentialClientUtil工具类往目标服务器上成功建立了文件夹，准备上传图片....");
			
			String remoteFileName = uploadFile(ftpClient, fileName, file);
			
			log.info("PreferentialClientUtil工具类执行uploadFile方法后返回结果：" + remoteFileName);
			
			if (StringUtils.isNotBlank(remoteFileName)) {
				
				String filePath = PATH + SEPARATOR + remoteFileName;
				
				disposeFtpClient(ftpClient);
				
				return filePath;
			}			
		}
		
		return null;
	}
	
}