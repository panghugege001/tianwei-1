package app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import app.config.FTPProperties;

public abstract class FTPClientUtil {

	private static Logger log = Logger.getLogger(FTPClientUtil.class);
	
	public FTPClientUtil() {}
	
	public static FTPClient getFTPClient() throws Exception {
		
		FTPClient ftpClient = new FTPClient();
		
		ftpClient.setDefaultPort(Integer.parseInt(FTPProperties.getInstance().getValue("server_port")));
		ftpClient.connect(FTPProperties.getInstance().getValue("server_ip"));
		ftpClient.login(FTPProperties.getInstance().getValue("server_user"), FTPProperties.getInstance().getValue("server_password"));
		ftpClient.configure(getFTPConfig());
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.setDataTimeout(120000);
		ftpClient.setBufferSize(1024);
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		
		int reply = ftpClient.getReplyCode();
		
		if (!FTPReply.isPositiveCompletion(reply)) {
	
			ftpClient.disconnect();
			log.info("FTP server refused connection....");
		}
		
		return ftpClient;
	}
	
	private static FTPClientConfig getFTPConfig() {
		
		FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);

		return ftpConfig;
	}
	
	public static boolean createDir(FTPClient ftpClient, String path) throws Exception {
		
		boolean flag = true;
		
		try {
			
			String[] arr = path.split("/");
			
			for(String name : arr) {
				
				if (!ftpClient.changeWorkingDirectory(name)) {

					if (ftpClient.makeDirectory(name)) {
						
						if (!ftpClient.changeWorkingDirectory(name)) {
							
						}
					}
				}
			}
			
			log.info("在目标服务器上成功建立了文件夹: " + path);
		} catch (Exception e) {
			
			flag = false;
			log.error("在目标服务器上创建文件夹失败，异常信息：" + e.getMessage());
			
			throw e;
		}
		
		return flag;
	}

	public static String uploadFile(FTPClient ftpClient, String fileName, File file) {
	
		String remoteFileName = "";
		InputStream inputStream = null;
		
		try {
			// 获取文件的扩展名
	        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
	        // 创建新的文件名   = 年月日   + "_" + 当前时间戳   + "." + 文件扩展名
	        remoteFileName = DateUtil.getCurrentDateFormat("yyyyMMdd") + "_" + String.valueOf(System.currentTimeMillis()) + "." + extensionName;
	        
	        inputStream = new FileInputStream(file);
	        
	        remoteFileName = new String(remoteFileName.getBytes("UTF-8"), "iso-8859-1");
	        
	        ftpClient.enterLocalPassiveMode();
			boolean flag = ftpClient.storeFile(remoteFileName, inputStream);
			
	        log.info("执行storeFile方法后返回结果：" + flag);
	        
	        if (flag) {
	        	
	        	log.info("图片上传成功....");
	        	
	        	return remoteFileName;
	        }
		} catch (IOException e) {
			
			log.error("往目标服务器上上传文件失败，异常信息：" + e.getMessage());
		} catch (Exception e) { 
			
			log.error("连接FTP服务器发生异常，异常信息：" + e.getMessage());
		} finally {
			
			try {
				
				if (null != inputStream) {
				
					inputStream.close();
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}  
		}
		
		return null;
	}
	
	public static void disposeFtpClient(FTPClient ftpClient) throws IOException {
		
		if (ftpClient != null) {
			
			if (ftpClient.isConnected()) {
				
				ftpClient.disconnect();
			}
		}
	}
	
}