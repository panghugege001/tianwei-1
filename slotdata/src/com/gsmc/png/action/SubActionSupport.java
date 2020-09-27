package com.gsmc.png.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;


public class SubActionSupport extends ActionSupport {


	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else
			return remoteAddr;
	}
	
	public String getIp(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
        if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;
    }
	
	 /**
     * struts action 使用
     * 访问成功
     * @param object
     */
    public static void Gson200(Object object) {
    	write(HttpStatus.SC_OK ,object,ServletActionContext.getResponse());
    }
	
	/**
     * 请求拒绝
     * @param object
     */
    public static void Gson500(Object object,HttpServletResponse response) {
    	write(500,object,response);
    }
    
    /**
     * struts action 使用
     * 请求拒绝
     * @param object
     */
    public static void Gson500(Object object) {
    	write(500,object,ServletActionContext.getResponse());
    }
	
	/**
     * 产生 
     * @param httpStatus 
     * @param object
     */
    private static void write(int httpStatus,Object object,HttpServletResponse response){
    	try {
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
            response.setStatus(httpStatus);
            
            PrintWriter out = response.getWriter();
            out.write(new Gson().toJson(object));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public void writeText(String text){
        try {
        	getResponse().setContentType("text/plain;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(text);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void writeObjectJson(Object obj){
        try {
        	getResponse().setContentType("text/plain;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(JSONObject.fromObject(obj).toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
