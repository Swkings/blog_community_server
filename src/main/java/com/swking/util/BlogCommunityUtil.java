package com.swking.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : BlogCommunityUtil
 * @Desc : 工具类，commons-lang3
 **/
public class BlogCommunityUtil {
    // 生成随机字符串
    public static String GenerateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // test -> dagfdhhahf
    // test + abc -> dafdgafdhrtehtr4ht
    public static String GetMD5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String GetJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String GetJSONString(int code, String msg) {
        return GetJSONString(code, msg, null);
    }

    public static String GetJSONString(int code) {
        return GetJSONString(code, null, null);
    }

    public static String GetObjectString(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Swking");
        map.put("age", 25);
        System.out.println(GetJSONString(0, "ok", map));
    }
}
