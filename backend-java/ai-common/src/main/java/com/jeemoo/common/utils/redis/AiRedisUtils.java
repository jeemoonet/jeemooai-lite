package com.jeemoo.common.utils.redis;

import cn.hutool.core.collection.ListUtil;
import com.jeemoo.common.core.domain.model.LoginUser;

import java.util.*;
import java.util.stream.Collectors;

public class AiRedisUtils {

    public static String RECENT_KEY = "RECENT_";

    public static boolean addRecent(LoginUser loginUser, Long promptId) {
//        List<Long> recentIds = new ArrayList<>();
//        recentIds.add(0,promptId);
//        return RedisUtils.setCacheList(RECENT_KEY + loginUser.getUserId()+"_"+loginUser.getCompanyId(),recentIds.stream().distinct().collect(Collectors.toList()));
        return true;
    }

    public static boolean setRecent(LoginUser loginUser, List<Long> recentIds) {
//        RedisUtils.deleteObject(RECENT_KEY+loginUser.getUserId()+"_"+loginUser.getCompanyId());
//        return RedisUtils.setCacheList(RECENT_KEY + loginUser.getUserId()+"_"+loginUser.getCompanyId(), recentIds);
        return true;
    }

    public static List<Long> getRecent(LoginUser loginUser) {
//        List<Long> recentIds = RedisUtils.getCacheList(RECENT_KEY + loginUser.getUserId() + "_" + loginUser.getCompanyId());
//        if (recentIds == null) {
//            recentIds = new ArrayList<>();
//        }
//        return ListUtil.reverse(recentIds);
        return new ArrayList<>();
    }
}
