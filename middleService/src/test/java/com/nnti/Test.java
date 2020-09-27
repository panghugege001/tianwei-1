package com.nnti;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Created by wander on 2017/2/7.
 */
public class Test {

    private static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) {
        String sb = "e68/jh/zfb_wx";
        int line = sb.indexOf("/");
        String code = sb.substring(0, line);
        String rt = sb.substring(line, sb.length());

        //System.out.println(code + "   "+ rt);
        //System.out.println((int)(Math.random()*90+10));
        long date = new Date().getTime();
        System.out.println(date/1000);
    }

}
