package com.jeemoo.api.service;

import cn.hutool.json.JSONObject;
import com.jeemoo.api.mapper.HomeMapper;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatisticalService {

    @Autowired
    private HomeMapper homeMapper;

    public List<JSONObject> dayIntegral(LoginUser loginUser, String startTime, String endTime) {
        List<JSONObject> list = homeMapper.dayIntegral(loginUser.getCompanyId(), startTime, endTime);
        if (list == null) list = new ArrayList<>();
        List<JSONObject> list2 = homeMapper.appDayIntegral(loginUser.getCompanyId(), startTime, endTime);
        if (list2 == null) list2 = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        for (JSONObject jsonObject : list) {
            map.put(jsonObject.getStr("dateStr"),jsonObject.getInt("integral"));
        }
        for (JSONObject jsonObject : list2) {
            Integer integral = jsonObject.getInt("integral");
            if (map.get(jsonObject.getStr("dateStr")) != null) {
                map.put(jsonObject.getStr("dateStr"),map.get(jsonObject.getStr("dateStr")) + integral);
            } else {
                map.put(jsonObject.getStr("dateStr"),jsonObject.getInt("integral"));
            }
        }

        List<String> allDays = this.generatorDateBetween(startTime, endTime);
        for (String date : allDays) {
            if (!map.containsKey(date)) {
                map.put(date,0);
            }
        }

        List<JSONObject> result = new ArrayList<>();
        for (String key : map.keySet()) {
            JSONObject item = new JSONObject();
            item.set("dateStr",key);
            item.set("integral",map.get(key));
            result.add(item);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 使用Comparator比较日期字段
        Comparator<JSONObject> comparator = Comparator.comparing(obj -> LocalDate.parse(obj.getStr("dateStr"), formatter));

        // 根据日期字段正序排序
        Collections.sort(result, comparator);

        return result;
    }

    public List<String> generatorDateBetween(String startTime, String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startTime.split(" ")[0], formatter);
        LocalDate endDate = LocalDate.parse(endTime.split(" ")[0], formatter);

        List<String> allDays = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            allDays.add(currentDate.format(formatter));
            currentDate = currentDate.plusDays(1);
        }

        return allDays;
    }

    public List<JSONObject> promptTop(LoginUser loginUser, String startTime, String endTime) {
        return homeMapper.promptTop(loginUser.getCompanyId(), startTime, endTime);
    }

    public List<JSONObject> userTop(LoginUser loginUser, String startTime, String endTime) {
        return homeMapper.userTop(loginUser.getCompanyId(), startTime, endTime);
    }

    public List<JSONObject> promptWindowCount(LoginUser loginUser, Long promptId, String startTime, String endTime) {
        List<JSONObject> list = homeMapper.promptWindowCount(loginUser.getCompanyId(),promptId,startTime,endTime);
        HashMap<String, JSONObject> hashMap = new HashMap<>();
        for (JSONObject jsonObject : list) {
            String key = jsonObject.getStr("dateStr");
            hashMap.put(key,jsonObject);
        }

        List<String> allDays = generatorDateBetween(startTime,endTime);
        List<JSONObject> result = new ArrayList<>();
        for (String allDay : allDays) {
            JSONObject item = new JSONObject();
            item.set("dateStr",allDay);
            item.set("count",0);
            JSONObject data = hashMap.get(allDay);
            if (data != null) {
                item.set("count",data.getInt("count"));
            }
            result.add(item);
        }

        return result;
    }

    public List<JSONObject> promptSelect(Long companyId) {
        return homeMapper.promptSelect(companyId);
    }

    public List<JSONObject> promptQuestionCount(LoginUser loginUser, Long promptId, String startTime, String endTime) {
        List<JSONObject> list = homeMapper.promptQuestionCount(loginUser.getCompanyId(),promptId,startTime,endTime);
        HashMap<String, JSONObject> hashMap = new HashMap<>();
        for (JSONObject jsonObject : list) {
            String key = jsonObject.getStr("dateStr");
            hashMap.put(key,jsonObject);
        }

        List<String> allDays = generatorDateBetween(startTime,endTime);
        List<JSONObject> result = new ArrayList<>();
        for (String allDay : allDays) {
            JSONObject item = new JSONObject();
            item.set("dateStr",allDay);
            item.set("count",0);
            JSONObject data = hashMap.get(allDay);
            if (data != null) {
                item.set("count",data.getInt("count"));
            }
            result.add(item);
        }

        return result;
    }

    public List<JSONObject> promptGradeCount(LoginUser loginUser, Long promptId, String startTime, String endTime) {
        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> list = homeMapper.promptGradeCount(loginUser.getCompanyId(),promptId,startTime,endTime);

        for (float i = 0.5f; i <= 5; i += 0.5f) {
            JSONObject item = new JSONObject();
            item.set("star", i + "星");
            item.set("count", 0);
            for (JSONObject jsonObject : list) {
                if (i == jsonObject.getFloat("star")) {
                    item.set("count", jsonObject.getInt("count"));
                    break;
                }
            }
            result.add(item);
        }

        return result;
    }
}
