package dfh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBProperty {  
    private String className;  
  
    private String url;  
  
    private String name;  
  
    private String password;  
  
    public DBProperty(){
    	InputStream in = null;  
        Properties p = new Properties();  
        try {  
            in = Class.forName("dfh.utils.DBProperty").getResourceAsStream("/config/DB.properties");  
            p.load(in);  
        }catch (Exception e) {  
            e.printStackTrace();  
        }finally{
        	 try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        className = p.getProperty("className");
        url = p.getProperty("url"); 
        name = p.getProperty("name"); 
        password = p.getProperty("password"); 
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
} 
