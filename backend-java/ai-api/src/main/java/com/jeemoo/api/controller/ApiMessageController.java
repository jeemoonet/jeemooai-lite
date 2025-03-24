package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.service.IAiRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class ApiMessageController {

    private final IAiRecordService recordService;

    @Log(title = "喜欢", businessType = BusinessType.API)
    @GetMapping("/like")
    public R<Object> like(Long messageId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (recordService.likeOrUnlike(messageId,1,loginUser.getUserId())) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "不喜欢", businessType = BusinessType.API)
    @GetMapping("/unlike")
    public R<Object> unlike(Long messageId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (recordService.likeOrUnlike(messageId,2,loginUser.getUserId())) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @DeleteMapping("")
    public R<Object> deleteMessage(String masterUuid) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (recordService.deleteByMasterUuid(loginUser,masterUuid)) {
            return R.ok("删除成功");
        } else {
            return R.fail("删除失败");
        }
    }
}
