package com.zyh.hu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码合法性验证
 * @author Hu
 */
public class MobileUtil {
    public static boolean isMobileNO(String mobile){
        if (mobile.length() != 11)
        {
            return false;
        }else{
            /**
             * 移动号段正则表达式
             */
            String pat1 = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$";
            String yd = "134,135,136,137,138,139,147";
            String yds[] = yd.split(",");
            StringBuilder builder = new StringBuilder();
            builder.append("^(");
            for (int i=0;i<yds.length;i++){
                builder.append("("+yds[i]+")|");
            }
//            if (){
//                builder.l
//            }
            builder.append(")");
            /**
             * 联通号段正则表达式
             */
            String pat2  = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
            /**
             * 电信号段正则表达式
             */
            String pat3  = "^((133)|(153)|(177)|(18[0,1,9])|(149)|(199))\\d{8}$";
            /**
             * 虚拟运营商正则表达式
             */
            String pat4 = "^((170))\\d{8}|(1718)|(1719)\\d{7}$";

            Pattern pattern1 = Pattern.compile(pat1);
            Matcher match1 = pattern1.matcher(mobile);
            boolean isMatch1 = match1.matches();
            if(isMatch1){
                return true;
            }
            Pattern pattern2 = Pattern.compile(pat2);
            Matcher match2 = pattern2.matcher(mobile);
            boolean isMatch2 = match2.matches();
            if(isMatch2){
                return true;
            }
            Pattern pattern3 = Pattern.compile(pat3);
            Matcher match3 = pattern3.matcher(mobile);
            boolean isMatch3 = match3.matches();
            if(isMatch3){
                return true;
            }
            Pattern pattern4 = Pattern.compile(pat4);
            Matcher match4 = pattern4.matcher(mobile);
            boolean isMatch4 = match4.matches();
            if(isMatch4){
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        String yd = "134,135";
        String yds[] = yd.split(",");
        StringBuilder builder = new StringBuilder();
        builder.append("^(");
        for (int i=0;i<yds.length;i++){
            builder.append("("+yds[i]+")|");
        }
        int t = builder.lastIndexOf("|");
        builder.replace(t,t+1,"");
        builder.append(")");
        System.out.println(builder.toString());
        System.out.println(t);
    }
}
