package dfh.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*  
 * 返回客户端信息工具类  
 * by netwild  
 */
public class ClientInfo {
	/*  
	 * 获取操作系统名称  
	 */
	public static String getOSName(String info) {
		String str = "未知";
		try{
			info=info.toLowerCase();
			if (info.indexOf("windows") != -1 || info.indexOf("win32")!=-1) {
				if(info.indexOf("windows nt 5.0")!=-1){
					str = "Windows Server 2000";
				}else if(info.indexOf("windows nt 5.1")!=-1){
					str = "Windows XP";
				}else if(info.indexOf("windows nt 5.2")!=-1){
					str = "Windows Server 2003";
				}else if(info.indexOf("windows nt 6.0")!=-1){
					str = "Windows Server 2008";
				}else if(info.indexOf("windows nt 6.1")!=-1){
					str = "Windows 7";
				}else if(info.indexOf("windows nt 6.2")!=-1){
					str = "Windows 8";
				}
			}else if(info.indexOf("macintosh")!=-1 || info.indexOf("mac os x")!=-1){
				str = "Macintosh";
			}else if(info.indexOf("linux")!=-1){
				str = "Linux";
			}else if(info.indexOf("adobeair")!=-1){
				str = "Adobeair";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
}