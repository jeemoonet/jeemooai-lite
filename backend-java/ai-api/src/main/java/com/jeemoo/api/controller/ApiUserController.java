package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.core.controller.BaseController;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.entity.SysUser;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.enums.UserType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.common.utils.StreamUtils;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.param.InitResult;
import com.jeemoo.system.service.*;
import com.jeemoo.system.service.impl.AiCompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends BaseController {

    private final ISysUserService userService;
    private final ISysRoleService roleService;
    private final ISysPostService postService;
    private final AiCompanyServiceImpl companyService;
    private final IAiModelsService modelsService;
    private final AiConfig aiConfig;

    @Value("${spring.profiles.active}")
    private String active;

    @Log(title = "获取用户信息", businessType = BusinessType.API)
    @GetMapping("/getInfo")
    public R<Object> getInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        SysUser user = userService.selectUserById(loginUser.getUserId());
        AiCompanyVo company = companyService.queryById(user.getCompanyId());
        InitResult initResult = modelsService.isInit();
        user.setCompanyName(company.getCompanyName());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("isInit",initResult.getIsInit());

        return R.ok(ajax);
    }

    @Log(title = "获取用户列表",businessType = BusinessType.API)
    @GetMapping("/list")
    public TableDataInfo<SysUser> list(SysUser user, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return userService.selectPageByUser(user, loginUser, pageQuery);
    }

    @Log(title = "获取用户",businessType = BusinessType.API)
    @GetMapping("")
    public R<Map<String, Object>> getInfo(Long userId) {
        userService.checkUserDataScope(userId);
        Map<String, Object> ajax = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        ajax.put("posts", postService.selectPostAll());
        if (ObjectUtil.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put("user", sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", StreamUtils.toList(sysUser.getRoles(), SysRole::getRoleId));
        }
        return R.ok(ajax);
    }

    @Log(title = "创建用户", businessType = BusinessType.API)
    @PostMapping("/create")
    public R<Object> add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return R.fail("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        userService.checkUserLimit(loginUser);
        user.setCompanyId(loginUser.getCompanyId());
        user.setCompanyName(loginUser.getCompanyName());
        user.setUserType(UserType.APP_USER.getUserType());
        user.setAvatar(aiConfig.getPreviewUrl() + "/static/avatar.png");
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return R.ok(userService.insertUser(user));
    }

    @Log(title = "修改用户", businessType = BusinessType.API)
    @PostMapping("/update")
    public R<Object> edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(BCrypt.hashpw(user.getPassword()));
        }
        return R.ok(userService.updateUser(user));
    }

    @Log(title = "删除用户", businessType = BusinessType.API)
    @DeleteMapping("")
    public R<Object> remove(Long userId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        Long[] userIds = new Long[]{userId};
        if (ArrayUtil.contains(userIds, loginUser.getUserId())) {
            return R.fail("当前用户不能删除");
        }
        if (userService.deleteUserByIds(userIds) > 0) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "更新个人资料")
    @PostMapping("/updateInfo")
    public R<Void> updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        user.setUserId(loginUser.getUserId());
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserName(null);
        user.setPassword(null);
        user.setDeptId(null);
        if (userService.updateUserProfile(user) > 0) {
            return R.ok();
        }
        return R.fail("修改个人信息异常，请联系管理员");
    }

    @Log(title = "修改密码", businessType = BusinessType.API)
    @PostMapping("/password")
    public R<Void> password(@RequestBody JSONObject param) {
        String oldPassword = param.getStr("oldPassword");
        String newPassword = param.getStr("newPassword");
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        String userName = user.getUserName();
        String password = user.getPassword();
//        if (!BCrypt.checkpw(oldPassword, password)) {
//            return R.fail("修改密码失败，旧密码错误");
//        }
        if (BCrypt.checkpw(newPassword, password)) {
            return R.fail("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(userName, BCrypt.hashpw(newPassword)) > 0) {
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

    @Log(title = "用户管理-修改状态", businessType = BusinessType.API)
    @PostMapping("/changeStatus")
    public R<Object> changeStatus(@RequestBody SysUser user) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        userService.checkUserAllowed(user);
        if (Objects.equals(user.getUserId(), loginUser.getUserId())) {
            return R.fail("不允许操作自己");
        }
        userService.checkUserDataScope(user.getUserId());
        if (userService.updateUserStatus(user) > 0) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    @Log(title = "用户弹窗提示", businessType = BusinessType.API)
    @PostMapping("/alertTag")
    public R<Object> alertTag(@RequestBody JSONObject jsonObject) {
        Integer isAlert = jsonObject.getInt("isAlert");
        userService.setIsAlert(LoginHelper.getUserId(),isAlert);
        return R.ok();
    }
}
