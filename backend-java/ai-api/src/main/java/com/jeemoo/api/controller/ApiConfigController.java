package com.jeemoo.api.controller;

import cn.hutool.json.JSONObject;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.entity.SysDictData;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.system.service.ISysConfigService;
import com.jeemoo.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ApiConfigController {

    private final ISysConfigService sysConfigService;
    private final ISysDictDataService dictDataService;

    @Log(title = "获取系统配置", businessType = BusinessType.CUSTOMER)
    @GetMapping("/{configKey}")
    public R<Object> getConfig(@PathVariable(name = "configKey") String configKey) {
        String value = this.sysConfigService.selectConfigByKey(configKey);
        return R.ok("操作成功",value);
    }

    @Log(title = "字典列表", businessType = BusinessType.CUSTOMER)
    @GetMapping("/options/{dictType}")
    public R<Object> dictData(@PathVariable(name = "dictType") String dictType) {
        List<SysDictData>  list = dictDataService.selectDictDataByType(dictType);
        List<JSONObject> result = new ArrayList<>();
        for (SysDictData sysDictData : list) {
            JSONObject item = new JSONObject();
            item.set("id",sysDictData.getDictCode());
            item.set("name",sysDictData.getDictLabel());
            item.set("value",sysDictData.getDictValue());
            result.add(item);
        }

        return R.ok(result);
    }

}
