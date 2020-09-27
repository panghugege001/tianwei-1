package app.util;

import java.io.*;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import app.config.FTPProperties;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;

public class FTPClientUtilX extends FTPClientUtil {
	
	private static Logger log = Logger.getLogger(FTPClientUtilX.class);
	
	private static FTPClient ftpClient = null;
	private static final String SEPARATOR = "/";

	public static String uploadFile(String fileName,File file, String ftpDirectoryConfigKey) throws Exception {
	
		try {
		
			ftpClient = getFTPClient();
		} catch (Exception e) {
			
			log.error("获取FTP客户端对象发生异常，异常信息：" + e.getMessage());
			
			throw e;
		}
		
		String filePath = "";
		
		String path = FTPProperties.getInstance().getValue("server_path_prefix") + FTPProperties.getInstance().getValue(ftpDirectoryConfigKey);
		
		if (createDir(ftpClient, path)) {
			
			String remoteFileName = uploadFile(ftpClient, fileName, file);
			
			filePath = path + SEPARATOR + remoteFileName;
			
			disposeFtpClient(ftpClient);
		}
		
		return filePath;
	}
	public static void downLoadFile(String fileName) throws Exception {
		try {
			ftpClient = getFTPClient();
		} catch (Exception e) {
			log.error("获取FTP客户端对象发生异常，异常信息：" + e.getMessage());
			throw e;
		}
		String path = FTPProperties.getInstance().getValue("server_path_prefix") + FTPProperties.getInstance().getValue("constraintAddressConfigure_excel_download_url");
		BufferedInputStream bis = null;
		BufferedOutputStream out = null;
		log.info(ftpClient  );
		try {
			ftpClient.changeWorkingDirectory(path);//转移到FTP服务器目录
			ftpClient.enterLocalPassiveMode();
			FTPFile[] fs = ftpClient.listFiles();
			log.info(fs.length);
			for(FTPFile ff:fs){
				if(ff.getName().equals(fileName)){
					log.info( ff.getName() );
					HttpServletResponse response = ServletActionContext.getResponse();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
					OutputStream outputStream = response.getOutputStream();
					out = new BufferedOutputStream(outputStream);
					ftpClient.retrieveFile(ff.getName(), out);
					out.flush();
					out.close();
					break;
				}
			}
			ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					throw ioe;
				}
			}
		}
	}
	
}