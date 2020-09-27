package com.nnti.office.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class StrutsPrintUtil {
	
	public static void printJsonResponse(HttpServletResponse response,Object obj){
        try {
        	response.setContentType("application/json;charset=UTF-8");
        	response.setHeader("Charset", "UTF-8");
        	response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.write(JSONUtil.toJSONString(obj));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
