package dfh.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-10-31
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class GsonUtil {

    public static <T> T GsonObject(Object object) {
        try {
            Gson gson = new Gson();
            String result = gson.toJson(object);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
            PrintWriter out;
            out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
            return (T) (new Boolean(true));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入页面失败！");
            return (T) (new Boolean(false));
        }
    }

    public static <T> T GsonList(List<T> list) {
        try {
            Gson gson = new Gson();
            String result = gson.toJson(list);
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
            PrintWriter out;
            out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
            return (T) (new Boolean(true));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入页面失败！");
            return (T) (new Boolean(false));
        }
    }

}
