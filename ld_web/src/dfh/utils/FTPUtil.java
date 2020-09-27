package dfh.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.FileTransferInputStream;
import com.enterprisedt.net.ftp.FileTransferOutputStream;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * FTP
 * 
 * @author Administrator
 * 
 */
public class FTPUtil {

	public static final SimpleDateFormat YMDHMS_SDFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	private static final String ip = "47.90.81.197";
	private static final int port = 8688;
	private static final String loginname = "payimg2016ftp";
	private static final String password = "Ns-2BcdmK8[7Y*Q0Ml007";
	private static final String prefix = "e68";
	
	private static FileTransferClient ftp = null ;
	
	public static FileTransferClient getFtpInstance() throws Exception{
		if(ftp == null){
			ftp = new FileTransferClient();
			ftp.setRemoteHost(ip);
			ftp.setRemotePort(port);
			ftp.setUserName(loginname);
			ftp.setPassword(password);
			ftp.getAdvancedSettings().setControlEncoding("UTF-8");
			ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);// 被动模式，数据连接由客户端发起
			ftp.setContentType(FTPTransferType.BINARY); // BINARY模式用来传送可执行文件,压缩文件,和图片文件.
			return ftp ;
		}else{
			return ftp ;
		}
	}
	
	/**
	 * 初始化连接
	 * 
	 * @param url
	 *            FTP服务端IP地址 如，192.168.1.254
	 * @param port
	 *            FTP服务端端口 如，21
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static FileTransferClient getFileTransferClient(String url,
			Integer port, String username, String password) throws Exception {
		FileTransferClient ftp = new FileTransferClient();
		ftp.setRemoteHost(url);
		ftp.setRemotePort(port);
		ftp.setUserName(username);
		ftp.setPassword(password);
		ftp.getAdvancedSettings().setControlEncoding("UTF-8");
		ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);// 被动模式，数据连接由客户端发起
		ftp.setContentType(FTPTransferType.BINARY); // BINARY模式用来传送可执行文件,压缩文件,和图片文件.
		ftp.connect();
		return ftp;
	}

	/**
	 * 关闭FTP
	 */
	public static void closeFileTransferClient(FileTransferClient ftp)
			throws Exception {
		if (ftp != null && ftp.isConnected()) {
			ftp.disconnect();
		}
	}

	/**
	 * 上传单个文件
	 * 
	 * @param localFilePath
	 *            本地文件路径
	 * @param remoteFilePath
	 *            远程存放路径
	 * @param folderPath
	 *            存放文件的路径 /车型/车号/类型拼音/年/月/日/
	 */
	public static void upload(String localFilePath, String remoteFileName,
			String folderPath, FileTransferClient ftp) throws Exception {
		createDirectory(ftp, folderPath);
		ftp.uploadFile(localFilePath, folderPath + remoteFileName);
	}

	/**
	 * 上传多个文件
	 * 
	 * @localFilePathArr 本地文件路径
	 * @remoteFilePathArr 远程服务器文件存放路径
	 * @folderPath 远程服务器存放文件的文件夹
	 * @ftp ftp客服端连接
	 */
	public static void upload(String[] localFilePathArr,
			String[] remoteFilePathArr, String folderPath,
			FileTransferClient ftp) throws Exception {
		try {
			createDirectory(ftp, folderPath);
			for (int i = 0; i < localFilePathArr.length; i++) {
				ftp.uploadFile(localFilePathArr[i], remoteFilePathArr[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载单个文件
	 * 
	 * @param localFilePath
	 *            本地文件路径
	 * @param remoteFilePath
	 *            远程存放路径
	 */
	public static void download(String localFilePath, String remoteFilePath,
			FileTransferClient ftp) throws Exception {
		ftp.downloadFile(localFilePath, remoteFilePath);
	}

	/**
	 * 按宽度高度压缩图片文件
	 * 
	 * @param oldFile
	 *            要进行压缩的文件全路径
	 * @param newFile
	 *            新文件
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param quality
	 *            质量 0.0 -- 1.0 越小，压缩后图像效果越差,图片越小
	 * @return 返回压缩后的文件的全路径
	 */
	public static void zipWidthHeightImageFile(String oldFile, String newFile,
			int width, int height, float quality, FileTransferClient ftp)
			throws Exception {
		if (oldFile == null) {
			return;
		}
		/** 对服务器上的临时文件进行处理 */
		String tempPath = "ftp://" + ftp.getUserName() + ":"
				+ ftp.getPassword() + "@" + ftp.getRemoteHost() + ":"
				+ ftp.getRemotePort() + oldFile;
		FileTransferInputStream ftis = FileTransferClient
				.downloadURLStream(tempPath);

		Image srcFile = ImageIO.read(ftis);

		/** 宽,高设定 */
		BufferedImage tag = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);

		/** 压缩之后存放位置 */
		FileTransferOutputStream ftos = null;
		try {
			ftos = ftp.uploadStream(newFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ftos);
		JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
		/** 压缩质量 */
		jep.setQuality(quality, true);
		encoder.encode(tag, jep);

		ftis.close();
		ftos.flush();
		ftos.close();
	}

	/**
	 * 按宽度高度压缩图片文件
	 * 
	 * @param oldFile
	 *            要进行压缩的文件全路径
	 * @param newFile
	 *            新文件
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param quality
	 *            质量 0.0 -- 1.0 越小，压缩后图像效果越差,图片越小
	 * @return 返回压缩后的文件的全路径
	 */
	public static void zipWidthHeightImageFile(String[] oldFiles,
			String[] newFiles, int width, int height, float quality,
			FileTransferClient ftp) throws Exception {

		String tempPath = "ftp://" + ftp.getUserName() + ":"
				+ ftp.getPassword() + "@" + ftp.getRemoteHost() + ":"
				+ ftp.getRemotePort() + "/";

		FileTransferInputStream ftis = null;
		FileTransferOutputStream ftos = null;
		Image srcFile = null;
		BufferedImage tag = null;
		JPEGImageEncoder encoder = null;
		JPEGEncodeParam jep = null;
		for (int i = 0; i < oldFiles.length; i++) {
			ftis = FileTransferClient.downloadURLStream(tempPath + oldFiles[i]);
			srcFile = ImageIO.read(ftis);

			/** 宽,高设定 */
			tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);

			/** 压缩之后存放位置 */

			ftos = ftp.uploadStream(newFiles[i]);

			encoder = JPEGCodec.createJPEGEncoder(ftos);
			jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/** 压缩质量 */
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);

			ftis.close();
			ftos.flush();
			ftos.close();
		}
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param remotePath
	 *            远程文件或文件夹路径
	 * @param type
	 *            0:文件 其他：文件夹
	 */
	public static void delete(String remotePath, int type,
			FileTransferClient ftp) throws Exception {
		if (remotePath != null && "".equals(remotePath)) {
			if (type == 0) {
				try {
					ftp.deleteFile(remotePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					ftp.deleteDirectory(remotePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param remotePath
	 *            远程文件或文件夹路径
	 * @param type
	 *            0:文件 其他：文件夹
	 */
	public static void delete(String[] remotePaths, int type,
			FileTransferClient ftp) throws Exception {
		if (type == 0) {
			for (String path : remotePaths) {
				if (path != null && !"".equals(path)) {
					try {
						ftp.deleteFile(path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			for (String path : remotePaths) {
				if (path != null && "".equals(path)) {
					try {
						ftp.deleteFile(path);
					} catch (Exception e) {
						ftp.deleteDirectory(path);
					}
				}
			}
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 *            存放的文件夹路径 /车型/车号/类型拼音/年/月/日/
	 */
	private static void createDirectory(FileTransferClient ftpClient,
			String folderPath) throws Exception {
		if (ftpClient.directoryList(folderPath).length == 0) {
			String[] fileNames = folderPath.split("/");
			String temp = "";
			for (int i = 1; i < fileNames.length; i++) {
				temp = temp + "/" + fileNames[i];
				if (ftpClient.directoryList(temp).length == 0) {
					ftpClient.createDirectory(temp + "/");
				} else {
					continue;
				}
			}
		}
	}

	/**
	 * 获取图片的名字
	 * 
	 * @param localFilePath
	 *            上传图片路径 请以"/"结尾
	 * @return 返回Map imgurl 原图 preimgurl 压缩图
	 */
	public static Map<String, String[]> getImagePath(String[] localFilePath,
			String folderPath) {
		Map<String, String[]> urlMap = new HashMap<String, String[]>();
		String[] imgurl = null;
		String[] preimgurl = null;
		if (localFilePath != null && localFilePath.length != 0) {
			int size = localFilePath.length;
			imgurl = new String[size];
			preimgurl = new String[size];
			String temp;
			for (int i = 0; i < size; i++) {
				temp = System.currentTimeMillis() + "_"
						+ (int) (Math.random() * 1000000);
				;
				imgurl[i] = folderPath + temp + getFileSuffix(localFilePath[i]);
				preimgurl[i] = folderPath + temp + "s"
						+ getFileSuffix(localFilePath[i]);
			}
		}
		urlMap.put("imgurl", imgurl);
		urlMap.put("preimgurl", preimgurl);
		return urlMap;
	}

	/**
	 * 获取文件名
	 * 
	 * @return
	 */
	public static String getFileSuffix(String filePath) {
		String suffix = "";
		if (filePath != null && !"".equals(filePath)) {
			suffix = filePath.substring(filePath.lastIndexOf("."))
					.toLowerCase();
		}
		return suffix;
	}
	
	public static Image getImage(String picPath) throws Exception{
		String tempPath = "ftp://" + ftp.getUserName() + ":"
				+ ftp.getPassword() + "@" + ftp.getRemoteHost() + ":"
				+ ftp.getRemotePort() + picPath;
		FileTransferInputStream ftis = FileTransferClient
				.downloadURLStream(tempPath);

		Image img = ImageIO.read(ftis);
		return img;
	}
	public static void uploadImgToRemote(String loginname , File file , String fileName) throws Exception{
		FileTransferClient ftp = getFtpInstance();
		if(!ftp.isConnected()){
			ftp.connect();
		}
		FileInputStream fis = new FileInputStream(file);
		FileTransferOutputStream ftos = ftp.uploadStream(prefix+"/"+loginname+"_"+DateUtil.fmtyyyyMMddHHmmss(new Date())+fileName);
		byte[] bytes = new byte[1024];
		
		int c;
		while ((c = fis.read(bytes)) != -1) {
			ftos.write(bytes, 0, c);
		}
		ftos.flush();
		ftos.close();
		fis.close();
		ftp.disconnect();
		file.delete();
	}
	
	public static void uploadImgToRemote(String path , File file) throws Exception{
		FileTransferClient ftp = getFtpInstance();
		if(!ftp.isConnected()){
			ftp.connect();
		}
		FileInputStream fis = new FileInputStream(file);
		FileTransferOutputStream ftos = ftp.uploadStream(path);
		byte[] bytes = new byte[1024];
		
		int c;
		while ((c = fis.read(bytes)) != -1) {
			ftos.write(bytes, 0, c);
		}
		ftos.flush();
		ftos.close();
		fis.close();
		ftp.disconnect();
	}


	public static void main(String[] args) {
		try {
			FTPUtil.uploadImgToRemote("woodytest", new File("E:\\Chrysanthemum.jpg"));
			FTPUtil.uploadImgToRemote("woodytest1", new File("E:\\Chrysanthemum.jpg"));
			FTPUtil.uploadImgToRemote("woodytest2", new File("E:\\Chrysanthemum.jpg"));
			
			 /*FileTransferClient ftpClient = FTPUtil.getFileTransferClient("192.168.0.201",21,"gsmcftp","gsmc123456");  
			 ftpClient.setContentType(FTPTransferType.BINARY);
	            FileInputStream fis = new FileInputStream(new File("E:\\Chrysanthemum.jpg"));  
	            FileTransferOutputStream ftos = ftpClient.uploadStream("/ufa/Chrysanthemum.jpg");  
	            byte[] bytes = new byte[1024];     
	            int c;     
	            while ((c = fis.read(bytes)) != -1) {
	                ftos.write(bytes, 0, c);
	            }
	            ftos.flush();  
	            ftos.close();  
	            fis.close();  
	            ftpClient.disconnect();*/  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
