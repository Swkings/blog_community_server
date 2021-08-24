package com.swking.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.swking.type.Node;
import com.swking.type.TSPData;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/23
 * @File : TSPUtil
 * @Desc : 将tsp数据存入文件，读取文件转成对象
 **/

@Slf4j
public class TSPUtil {

    public  static <T> void Object2File(String filePath, T object){
        String strObject = JSONObject.toJSONString(object, true);
        BufferedWriter writer = null;
        File file = new File(filePath);
        //如果文件不存在则新建
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
        //如果多次执行同一个流程，会导致json文件内容不断追加，在写入之前清空文件
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),"UTF-8"));
            writer.write("");
            writer.write(strObject);
        }catch (IOException e){
            log.error(e.getMessage());
        }finally {
            try{
                if (writer != null){
                    writer.close();
                }
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
    }
    public static <T> T File2Object(String filePath, Class<T> tClass){
        BufferedReader reader = null;
        String readJson = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null){
                readJson += tempString;
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }
            }
        }
        // 获取json
        T object = null;
        try {
            object = JSONObject.parseObject(readJson, tClass);
        }catch (JSONException e){
            log.error(e.getMessage());
        }
        return object;
    }

}
