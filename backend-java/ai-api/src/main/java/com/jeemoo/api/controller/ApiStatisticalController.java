package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.jeemoo.api.service.StatisticalService;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/statistical")
@RequiredArgsConstructor
public class ApiStatisticalController {

    private final StatisticalService statisticalService;

    @Log(title = "积分消耗趋势" ,businessType = BusinessType.API)
    @GetMapping("/dayIntegral")
    public R<Object> dayIntegral(String startTime, String endTime) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
        } else {
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            LocalTime lastTime = LocalTime.of(23,59,59);
            LocalDateTime currentDateTime = currentDate.atTime(lastTime);

            // 减去7天得到默认开始时间
            LocalDate defaultStartTime = currentDate.minusDays(7);

            // 格式化日期时间
            startTime = defaultStartTime.atStartOfDay().format(formatter);
            endTime = currentDateTime.format(formatter);
        }

        List<JSONObject> list = statisticalService.dayIntegral(loginUser, startTime, endTime);
        return R.ok(list);
    }

    @Log(title = "助手使用排行TOP10", businessType = BusinessType.API)
    @GetMapping("/promptTop")
    public R<Object> promptTop(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
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
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<JSONObject> list = statisticalService.promptTop(loginUser, startTime, endTime);
        return R.ok(list);
    }


    @Log(title = "用户使用排行TOP10", businessType = BusinessType.API)
    @GetMapping("/userTop")
    public R<Object> userTop(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
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
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<JSONObject> list = statisticalService.userTop(loginUser, startTime, endTime);
        return R.ok(list);
    }

    @Log(title = "使用统计-提示器下拉选择", businessType = BusinessType.API)
    @GetMapping("/promptSelect")
    public R<Object> promptSelect() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return R.ok(statisticalService.promptSelect(loginUser.getCompanyId()));
    }

    @Log(title = "使用统计-使用人数", businessType = BusinessType.API)
    @SaCheckPermission("company:statisticalPrompt:view")
    @GetMapping("/promptWindowCount")
    public R<Object> promptWindowCount(Long promptId, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
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
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<JSONObject> list = statisticalService.promptWindowCount(loginUser, promptId, startTime, endTime);
        return R.ok(list);
    }

    @Log(title = "使用统计-问题数量", businessType = BusinessType.API)
    @GetMapping("/promptQuestionCount")
    public R<Object> promptQuestionCount(Long promptId, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
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
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<JSONObject> list = statisticalService.promptQuestionCount(loginUser, promptId, startTime, endTime);
        return R.ok(list);
    }

    @Log(title = "使用统计-评分", businessType = BusinessType.API)
    @GetMapping("/promptGradeCount")
    public R<Object> promptGradeCount(Long promptId, String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StrUtil.isNotEmpty(startTime)) {
            LocalDate startDateTime = LocalDate.parse(startTime, formatter);
            LocalDate endDateTime = LocalDate.parse(endTime, formatter);
            // 计算日期区间
            long daysBetween = ChronoUnit.DAYS.between(startDateTime, endDateTime);

            if (daysBetween > 60) {
                // 超过最大区间限制，抛出异常或返回错误信息
                return R.fail("时间区间最大60天");
            }
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
        LoginUser loginUser = LoginHelper.getLoginUser();
        List<JSONObject> list = statisticalService.promptGradeCount(loginUser, promptId, startTime, endTime);
        return R.ok(list);
    }
}
