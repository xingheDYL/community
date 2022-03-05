package com.dyl.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author admin
 */
public class CommunityUtil {

    /**
     * 生成随机字符串
     *
     * @return String
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * MD5加密
     * hello -> abc123def456
     * hello + 3e4a8 -> abc123def456abc
     *
     * @param key key
     * @return String
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
