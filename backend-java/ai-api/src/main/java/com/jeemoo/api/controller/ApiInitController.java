package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.annotation.Post;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.annotation.RepeatSubmit;
import com.jeemoo.common.core.controller.BaseController;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.entity.SysDictData;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.domain.bo.AiModelsBo;
import com.jeemoo.system.domain.vo.AiModelsVo;
import com.jeemoo.system.param.*;
import com.jeemoo.system.service.IAiModelsService;
import com.jeemoo.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/init")
public class ApiInitController extends BaseController {

    private final IAiModelsService iAiModelsService;
    private final ISysDictDataService sysDictDataService;

    @GetMapping("")
    public R<Object> checkInit() {
        InitResult result = iAiModelsService.isInit();
        return R.ok(result);
    }

    @PostMapping("/setting")
    public R<Object> setting(@RequestBody InitSetting initSetting) {
        boolean bool = iAiModelsService.setModelConfig(initSetting);
        if (bool) {
            return R.ok();
        } else {
            return R.fail("修改失败");
        }
    }

    @GetMapping("/setting")
    public R<Object> setting() {
        return R.ok(iAiModelsService.getModelConfig());
    }

    @GetMapping("/models")
    public R<List<AiModelsVo>> all(AiModelsBo bo) {
        return R.ok(iAiModelsService.queryList(bo));
    }

    @PostMapping("/models/create")
    public R<Void> add(@RequestBody AiModelsBo bo) {
        return toAjax(iAiModelsService.insertByBo(bo));
    }

    @PostMapping("/models/update")
    public R<Void> edit(@RequestBody AiModelsBo bo) {
        List<SysDictData> platformNames = sysDictDataService.selectDictDataByType("model_platform_name");
        HashMap<String, String> map = new HashMap<>();
        for (SysDictData platformName : platformNames) {
            map.put(platformName.getDictValue(), platformName.getRemark());
        }
        bo.setIcon(map.get(bo.getPlatformName()));
        return toAjax(iAiModelsService.updateByBo(bo));
    }

    @PostMapping("/models/delete")
    public R<Void> remove(@RequestBody AiModelsBo bo) {
        return toAjax(iAiModelsService.deleteWithValidByIds(Arrays.asList(bo.getId()), true));
    }

    @PostMapping("/models/setDefault")
    public R<Void> setDefault(@RequestBody AiModelsBo bo) {
        return toAjax(iAiModelsService.setDefault(bo.getId()));
    }


    @PostMapping("/models/setReasoningDefault")
    public R<Void> setReasoningDefault(@RequestBody AiModelsBo bo) {
        return toAjax(iAiModelsService.setReasoningDefault(bo.getId()));
    }

    @GetMapping("/platform/list")
    public R<Object> platforms() {
        List<SysDictData> platformNames = sysDictDataService.selectDictDataByType("model_platform_name");
        List<String> names = new ArrayList<>();
        for (SysDictData platformName : platformNames) {
            names.add(platformName.getDictValue());
        }
        JSONObject result = new JSONObject();
        result.set("platform", names);
        return R.ok(result);
    }
}
