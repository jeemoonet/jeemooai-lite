package com.jeemoo.web.controller.biz;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.annotation.Post;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.system.service.impl.ScientificService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/scientific")
public class AiScientificController {

    private final ScientificService scientificService;

    @PostMapping("/count")
    public R<Object> getCount(@RequestBody JSONObject param) {
        List<String> rangeTime = param.getBeanList("rangeTime", String.class);
        String startTime = "";
        String endTime = "";
        if (rangeTime != null && rangeTime.size() == 2) {
            startTime = rangeTime.get(0);
            endTime = rangeTime.get(1);
        }
        JSONObject result = scientificService.getCount(param.getLong("companyId"), startTime,endTime);
        return R.ok(result);
    }

    @PostMapping("/dayCount")
    public R<Object> getDayCount(@RequestBody JSONObject param) {
        JSONArray rangeTime = param.getJSONArray("rangeTime");
        String startTime = "";
        String endTime = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (rangeTime != null && rangeTime.size() == 2) {
            startTime = rangeTime.getStr(0);
            endTime = rangeTime.getStr(1);
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalTime lastTime = LocalTime.of(23,59,59);
            LocalDateTime currentDateTime = currentDate.atTime(lastTime);

            // 减去7天得到默认开始时间
            LocalDate defaultStartTime = currentDate.minusDays(7);

            // 格式化日期时间
            startTime = defaultStartTime.atStartOfDay().format(formatter);
            endTime = currentDateTime.format(formatter);
        }
        List<JSONObject> result = scientificService.getDayCount(param.getLong("companyId"), startTime,endTime);
        return R.ok(result);
    }

    @PostMapping("/topCount")
    public R<Object> getTopCount(@RequestBody JSONObject param) {
        JSONArray rangeTime = param.getJSONArray("rangeTime");
        String startTime = "";
        String endTime = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (rangeTime != null && rangeTime.size() == 2) {
            startTime = rangeTime.getStr(0);
            endTime = rangeTime.getStr(1);
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalTime lastTime = LocalTime.of(23,59,59);
            LocalDateTime currentDateTime = currentDate.atTime(lastTime);

            // 减去7天得到默认开始时间
            LocalDate defaultStartTime = currentDate.minusDays(7);

            // 格式化日期时间
            startTime = defaultStartTime.atStartOfDay().format(formatter);
            endTime = currentDateTime.format(formatter);
        }
        List<JSONObject> result = scientificService.getTopCount(param.getLong("companyId"), startTime, endTime, param.getStr("type"));
        return R.ok(result);
    }
}
