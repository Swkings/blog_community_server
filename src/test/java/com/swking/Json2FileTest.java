package com.swking;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.swking.type.Node;
import com.swking.type.TSPData;
import com.swking.util.TSPUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/23
 * @File : Json2FileTest
 * @Desc :
 **/

@Slf4j
@RunWith(SpringRunner.class)
// @SpringBootTest
// @ContextConfiguration(classes = BlogCommunityServerApplication.class)
public class Json2FileTest {

    // @Test
    // public void testJSON2String(){
    //     List<Node> list = new ArrayList<Node>(){{
    //         add(new Node(1, "", 1, 1));
    //         add(new Node(2, "", 2, 2));
    //         add(new Node(3, "", 3, 3));
    //     }};
    //
    //     TSPData tspData = new TSPData()
    //             .setNAME("tspTest")
    //             .setCOMMENT("test")
    //             .setTYPE("TSP")
    //             .setDIMENSION(3)
    //             .setEDGE_WEIGHT_TYPE("EUC_2D")
    //             .setNODE_COORD_SECTION(list);
    //     String strTSP = JSONObject.toJSONString(tspData, true);
    //     System.out.println(strTSP);
    // }
    //
    // @Test
    // public void testString2File(){
    //     List<Node> list = new ArrayList<Node>(){{
    //         add(new Node(1, "", 1, 1));
    //         add(new Node(2, "", 2, 2));
    //         add(new Node(3, "", 3, 3));
    //     }};
    //
    //     TSPData tspData = new TSPData()
    //             .setNAME("tspTest")
    //             .setCOMMENT("test")
    //             .setTYPE("TSP")
    //             .setDIMENSION(3)
    //             .setEDGE_WEIGHT_TYPE("EUC_2D")
    //             .setNODE_COORD_SECTION(list);
    //     String strTSP = JSONObject.toJSONString(tspData, true);
    //     System.out.println(strTSP);
    //
    //     String filePath = "E:/CodeSpace/JavaSpace/blog_community_server/ext/tsp-solver/"+tspData.getNAME()+".tsp";
    //     BufferedWriter writer = null;
    //     File file = new File(filePath);
    //     //如果文件不存在则新建
    //     if (!file.exists()){
    //         try {
    //             file.createNewFile();
    //         }catch (IOException e){
    //             log.error(e.getMessage());
    //         }
    //     }
    //     //如果多次执行同一个流程，会导致json文件内容不断追加，在写入之前清空文件
    //     try {
    //         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),"UTF-8"));
    //         writer.write("");
    //         writer.write(strTSP);
    //     }catch (IOException e){
    //         log.error(e.getMessage());
    //     }finally {
    //         try{
    //             if (writer != null){
    //                 writer.close();
    //             }
    //         }catch (IOException e){
    //             log.error(e.getMessage());
    //         }
    //     }
    // }
    //
    // @Test
    // public void testFile2JSON(){
    //     String filePath = "E:/CodeSpace/JavaSpace/blog_community_server/ext/tsp-solver/"+"tspTest"+".tsp";
    //     BufferedReader reader = null;
    //     String readJson = "";
    //     try {
    //         FileInputStream fileInputStream = new FileInputStream(filePath);
    //         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
    //         reader = new BufferedReader(inputStreamReader);
    //         String tempString = null;
    //         while ((tempString = reader.readLine()) != null){
    //             readJson += tempString;
    //         }
    //     }catch (IOException e){
    //         log.error(e.getMessage());
    //     }finally {
    //         if (reader != null){
    //             try {
    //                 reader.close();
    //             }catch (IOException e){
    //                 log.error(e.getMessage());
    //             }
    //         }
    //     }
    //     // 获取json
    //     try {
    //         TSPData tspData = JSONObject.parseObject(readJson, TSPData.class);
    //         System.out.println(JSONObject.toJSONString(tspData, true));
    //     }catch (JSONException e){
    //         log.error(e.getMessage());
    //     }
    // }
    //
    // @Test
    // public void testFile2Object(){
    //     String filePath = "E:/CodeSpace/JavaSpace/blog_community_server/ext/tsp-solver/"+"tspTest"+".tsp";
    //     TSPData tspData = TSPUtil.File2Object(filePath, TSPData.class);
    //     System.out.println(tspData);
    // }
    //
    // @Test
    // public void testObject2File(){
    //     String filePath = "E:/CodeSpace/JavaSpace/blog_community_server/ext/tsp-solver/"+"tspTest1"+".tsp";
    //     List<Node> list = new ArrayList<Node>(){{
    //         add(new Node(1, "", 1, 1));
    //         add(new Node(2, "", 2, 2));
    //         add(new Node(3, "", 3, 3));
    //     }};
    //
    //     TSPData tspData = new TSPData()
    //             .setNAME("tspTest")
    //             .setCOMMENT("test")
    //             .setTYPE("TSP")
    //             .setDIMENSION(3)
    //             .setEDGE_WEIGHT_TYPE("EUC_2D")
    //             .setNODE_COORD_SECTION(list);
    //     TSPUtil.Object2File(filePath, tspData);
    //     // System.out.println(tspData);
    // }

}
