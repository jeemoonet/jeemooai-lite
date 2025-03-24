package com.jeemoo.system.service.impl;

import cn.hutool.json.JSONObject;
import com.jeemoo.system.mapper.ScientificMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ScientificService {

    private final ScientificMapper scientificMapper;

    public JSONObject getCount(Long companyId, String startTime, String endTime) {
        JSONObject result = new JSONObject();
        int companyCount = scientificMapper.companyCount();
        result.set("companyCount",companyCount);
        int userCount = scientificMapper.userCount();
        result.set("userCount",userCount);
        int promptCount = scientificMapper.promptCount();
        result.set("promptCount",promptCount);
        int repoCount = scientificMapper.repoCount();
        result.set("repoCount",repoCount);

        return result;
    }

    public List<JSONObject> getDayCount(Long companyId, String startTime, String endTime) {
        List<String> betweenDays = this.generatorDateBetween(startTime,endTime);
        List<JSONObject> result = new ArrayList<>();

        List<JSONObject> companyDayCount = scientificMapper.companyDayCount(companyId,startTime,endTime);
        List<JSONObject> userDayCount = scientificMapper.userDayCount(companyId,startTime,endTime);
        List<JSONObject> windowDayCount = scientificMapper.windowDayCount(companyId,startTime,endTime);
        List<JSONObject> recordDayCount = scientificMapper.recordDayCount(companyId,startTime,endTime);

        for (String betweenDay : betweenDays) {
            JSONObject item = new JSONObject();
            item.set("date_str",betweenDay);
            item.set("company",0);
            item.set("user",0);
            item.set("window",0);
            item.set("record",0);

            for (JSONObject entries : companyDayCount) {
                if (Objects.equals(entries.getStr("date_str"), betweenDay)) {
                    item.set("company",entries.getInt("num"));
                }
            }

            for (JSONObject entries : userDayCount) {
                if (Objects.equals(entries.getStr("date_str"), betweenDay)) {
                    item.set("user",entries.getInt("num"));
                }
            }

            for (JSONObject entries : windowDayCount) {
                if (Objects.equals(entries.getStr("date_str"), betweenDay)) {
                    item.set("window",entries.getInt("num"));
                }
            }

            for (JSONObject entries : recordDayCount) {
                if (Objects.equals(entries.getStr("date_str"), betweenDay)) {
                    item.set("record",entries.getInt("num"));
                }
            }

            result.add(item);
        }

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

    public List<JSONObject> getTopCount(Long companyId, String startTime, String endTime, String type) {
        if (Objects.equals(type, "company")) {
            return scientificMapper.companyTop(companyId, startTime, endTime);
        }
        if (Objects.equals(type, "prompt")) {
            return scientificMapper.promptTop(companyId, startTime, endTime);
        }
        if (Objects.equals(type, "model")) {
            return scientificMapper.modelTop(companyId, startTime, endTime);
        }
        return new ArrayList<>();
    }
}
