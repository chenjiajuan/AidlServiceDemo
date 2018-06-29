package com.chenjiajuan.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjiajuan on 2018/6/29.
 */

public class TestPattern {
    public  static  void main(String [] args){
        fun2();

    }

    public static void fun(){
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher("helloabcbbccbcccc");
        //find向前迭代
        while(matcher.find()){
            System.out.println(matcher.group());
        }
    }

    public static void fun2(){
        String result=" (function(){jsonp1({\"success\":true,\"message\":\"null\",\"url\":\"http://ma.taobao.com/rl/ed9296389eba0847a8c367075ef264f0\",\"t\":1530247579835,\"at\":\"0d5fbe735133628a05149eb754ddda83\"});})();\n";
        //jsonp1\(\{.+\"url\":\"\"
//        Pattern pattern=Pattern.compile("jsonp1\\(\\{.+\\\"url\\\":\\\"((\\w|\\/|:|\\.)+)\\\"");
//        String string="jsonp1\\(\\{.+\\\"url\\\":\\\"((\\w|\\/|:|\\.)+)\\\"";
//        String url="jsonp1\\(\\{.+\\\"url\\\":\\\"((\\w|\\/|:|\\.)+)\\\"";
        //          jsonp1 \( \{.+ \" url \": \" ((\w|\/|:|\.)+) \"

        String url="jsonp1\\(\\w\\)";
        ArrayList<String> list=new ArrayList<>();
        Pattern pattern=Pattern.compile(url);
        Matcher matcher=pattern.matcher(result);
        if (matcher.find(1)){
          String sb= matcher.group(1);
          System.out.println(sb);
        }
    }
}
