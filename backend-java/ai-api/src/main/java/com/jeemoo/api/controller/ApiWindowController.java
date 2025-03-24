package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.AiWindow;
import com.jeemoo.system.domain.bo.AiRecordBo;
import com.jeemoo.system.domain.bo.AiWindowBo;
import com.jeemoo.system.domain.vo.AiPromptVo;
import com.jeemoo.system.domain.vo.AiRecordVo;
import com.jeemoo.system.domain.vo.AiWindowVo;
import com.jeemoo.system.param.*;
import com.jeemoo.system.service.IAiPromptService;
import com.jeemoo.system.service.IAiRecordService;
import com.jeemoo.system.service.IAiWindowService;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/window")
public class ApiWindowController {

    private final IAiWindowService windowService;
    private final IAiPromptService promptService;
    private final IAiRecordService recordService;
    private final HttpServletRequest request;

    @Log(title = "创建自由问答", businessType = BusinessType.API)
    @PostMapping("/createFree")
    public R<CreateWindowResponse> createFreeWindow() {
        String platform = request.getHeader("Platform");
        if (StringUtils.isEmpty(platform)) {
            platform = "pc";
        }
        LoginUser loginUser = LoginHelper.getLoginUser();

        AiWindowBo window = new AiWindowBo();
        window.setWindowName("自由问答新会话");
        window.setPlatform(platform);
        window.setCompanyId(loginUser.getCompanyId());
        window.setUserId(loginUser.getUserId());

        windowService.insertByBo(window);
        return R.ok();
    }

    @Log(title = "通过提示器创建", businessType = BusinessType.API)
    @PostMapping("/create")
    public R<CreateWindowResponse> create(@RequestBody JSONObject param) {
        Long promptId = param.getLong("promptId");
        String platform = request.getHeader("Platform");
        if (platform == null) platform = "pc";
        if (promptId == null) {
            return R.fail("提示器id不能为空");
        }

        Integer talkType = param.getInt("talkType",0);

        LoginUser loginUser = LoginHelper.getLoginUser();
        AiPromptVo promptVo = promptService.queryById(promptId);
        if (promptVo == null) {
            return R.fail("提示器不存在");
        }

        Long windowId = windowService.findOneFreeWindow(loginUser.getUserId(), platform, promptId);
        String windowName = "";
        if (platform.equals("pc") || (windowId == null && platform.equals("miniapp"))) {
            AiWindowBo window = new AiWindowBo();
            window.setWindowName(promptVo.getPromptName());
            window.setUserId(loginUser.getUserId());
            window.setPromptId(promptId);
            window.setTalkType(talkType);
            window.setCompanyId(loginUser.getCompanyId());
            window.setPlatform(platform);
            windowService.insertByBo(window);

            AiRecordBo recordBo = new AiRecordBo();
            recordBo.setWindowId(window.getWindowId());
            recordBo.setUserId(loginUser.getUserId());
            recordBo.setCreateTimestamp(System.currentTimeMillis());
            recordBo.setContent(promptVo.getInitMessage());
            recordBo.setRole(ChatMessageRole.ASSISTANT.value());
            recordBo.setIsInitMessage(1);
            recordBo.setPromptId(promptId);
            recordBo.setCompanyId(loginUser.getCompanyId());
            recordService.insertByBo(recordBo);

            windowId = window.getWindowId();
            windowName = window.getWindowName();
        } else {
            AiWindow window = windowService.findOne(windowId);
            windowName = window.getWindowName();
        }

        CreateWindowResponse response = new CreateWindowResponse();
        response.setWindowId(windowId);
        response.setWindowName(windowName);
        response.setPromptDesc(promptVo.getPromptDesc());
        response.setPromptName(promptVo.getPromptName());
        response.setPromptIcon(promptVo.getPromptIcon());
        response.setPromptId(promptId);
        response.setIsContext(promptVo.getIsContext());
        response.setDigitPersonId(promptVo.getDigitPerson());

        promptService.addUseNum(promptId,1);

        return R.ok(response);
    }

    @Log(title = "会话列表", businessType = BusinessType.API)
    @GetMapping("/list")
    public R<List<WindowListResponse>> list(Long filterWindowId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        String platform = request.getHeader("Platform");
        if (StringUtils.isEmpty(platform)) {
            platform = "pc";
        }
        List<WindowListResponse> list = windowService.listByUser(loginUser, 0, platform, filterWindowId);
        return R.ok(list);
    }

    @Log(title = "企业管理会话列表", businessType = BusinessType.API)
    @GetMapping("/page")
    public TableDataInfo<CompanyWindowResponse> page(CompanyWindowParam param, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return windowService.pageByCompany(loginUser,param,pageQuery);
    }

    @Log(title = "会话详情", businessType = BusinessType.API)
    @GetMapping("/record")
    public TableDataInfo<AiRecordVo> record(Long windowId, Long timestamp, PageQuery pageQuery) {
        if (timestamp == null) {
            timestamp = 0L;
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiRecordBo bo = new AiRecordBo();
        bo.setWindowId(windowId);
        bo.setCompanyId(loginUser.getCompanyId());
        bo.setTimestamp(timestamp);
        bo.setAsc(true);

        TableDataInfo<AiRecordVo> result = recordService.queryPageList(bo, pageQuery);
        List<AiRecordVo> rows = result.getRows();
        for (AiRecordVo row : rows) {
            if (StrUtil.isNotEmpty(row.getDocInfo())) {
                row.setDocInfoList(JSONUtil.toList(row.getDocInfo(),JSONObject.class));
            }
        }
        result.setRows(rows);
        return result;
    }

    @Log(title = "重命名会话", businessType = BusinessType.API)
    @PostMapping("/rename")
    public R<Object> rename(@RequestBody @Validated RenameWindowParam param) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiWindowVo windowVo = windowService.queryById(param.getWindowId());
        if (windowVo == null) {
            return R.fail();
        }
        if (!Objects.equals(windowVo.getUserId(), loginUser.getUserId())) {
            return R.fail();
        }

        String windowName = param.getWindowName().length() > 20 ? param.getWindowName().substring(0,20) : param.getWindowName();
        param.setWindowName(windowName);
        if (windowService.rename(param,loginUser)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "删除会话", businessType = BusinessType.API)
    @DeleteMapping("")
    public R<Object> delete(Long windowId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiWindowVo windowVo = windowService.queryById(windowId);
        if (windowVo == null) {
            return R.fail();
        }
        if (!Objects.equals(windowVo.getUserId(), loginUser.getUserId())) {
            return R.fail();
        }

        if (windowService.deleteById(windowId)) {
            return R.ok();
        } else {
            return R.fail("删除失败");
        }
    }

    @Log(title = "删除会话", businessType = BusinessType.API)
    @PostMapping("/delete")
    public R<Object> deleteByIds(@RequestBody JSONObject param) {
        List<Long> windowIds = param.getBeanList("windowId", Long.class);
        if (windowIds == null || windowIds.isEmpty()) {
            return R.fail("会话id不能为空");
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (windowService.deleteByIds(windowIds.toArray(new Long[0]), loginUser)) {
            return R.ok();
        } else {
            return R.fail("删除失败");
        }
    }

    @GetMapping("/debuggerWindowId")
    public R<Object> debuggerWindowId(Long promptId) {
        if (promptId == null || promptId == 0L) {
            return R.fail("promptId不能为空");
        }
        String platform = request.getHeader("Platform");
        if (platform == null) platform = "pc";
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiWindow window = windowService.getOne(new QueryWrapper<AiWindow>()
                .eq("talk_type",3)
                .eq("user_id",loginUser.getUserId())
                .eq("prompt_id", promptId)
                .last("limit 1")
        );
        if (window == null) {
            window = new AiWindow();
            window.setWindowName("工作流调试");
            window.setUserId(loginUser.getUserId());
            window.setCompanyId(loginUser.getCompanyId());
            window.setTalkType(3);
            window.setPromptId(promptId);
            window.setPlatform(platform);
            windowService.insert(window);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("windowId", window.getWindowId());
        return R.ok(jsonObject);
    }
}
