package com.jeemoo.web.controller.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.core.domain.ModelsConfig;
import com.jeemoo.common.core.domain.entity.SysDictData;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.AiModels;
import com.jeemoo.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.jeemoo.common.annotation.RepeatSubmit;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.controller.BaseController;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.validate.AddGroup;
import com.jeemoo.common.core.validate.EditGroup;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.utils.poi.ExcelUtil;
import com.jeemoo.system.domain.vo.AiModelsVo;
import com.jeemoo.system.domain.bo.AiModelsBo;
import com.jeemoo.system.service.IAiModelsService;
import com.jeemoo.common.core.page.TableDataInfo;

/**
 * 模型
 *
 * @author ai
 * @date 2023-11-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/models")
public class AiModelsController extends BaseController {

    private final IAiModelsService iAiModelsService;
    private final ISysDictDataService sysDictDataService;

    /**
     * 查询模型列表
     */
    @SaCheckPermission("system:models:list")
    @GetMapping("/list")
    public TableDataInfo<AiModelsVo> list(AiModelsBo bo, PageQuery pageQuery) {
        return iAiModelsService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/all")
    public R<List<AiModelsVo>> all(AiModelsBo bo) {
        return R.ok(iAiModelsService.queryList(bo));
    }

    /**
     * 导出模型列表
     */
    @SaCheckPermission("system:models:export")
    @Log(title = "模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AiModelsBo bo, HttpServletResponse response) {
        List<AiModelsVo> list = iAiModelsService.queryList(bo);
        ExcelUtil.exportExcel(list, "模型", AiModelsVo.class, response);
    }

    /**
     * 获取模型详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:models:query")
    @GetMapping("/{id}")
    public R<AiModelsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer id) {
        return R.ok(iAiModelsService.queryById(id));
    }


}
