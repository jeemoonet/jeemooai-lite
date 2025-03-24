package com.jeemoo.system.service;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeemoo.common.config.AiConfig;
import com.jeemoo.common.constant.CacheConstants;
import com.jeemoo.common.constant.Constants;
import com.jeemoo.common.core.domain.entity.SysMenu;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.event.LogininforEvent;
import com.jeemoo.common.core.domain.entity.SysUser;
import com.jeemoo.common.core.domain.model.RegisterBody;
import com.jeemoo.common.enums.UserType;
import com.jeemoo.common.exception.user.CaptchaException;
import com.jeemoo.common.exception.user.CaptchaExpireException;
import com.jeemoo.common.exception.user.UserException;
import com.jeemoo.common.utils.MessageUtils;
import com.jeemoo.common.utils.ServletUtils;
import com.jeemoo.common.utils.StringUtils;
import com.jeemoo.common.utils.redis.RedisUtils;
import com.jeemoo.common.utils.spring.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRegisterService {

    private final ISysUserService userService;
    private final ISysConfigService configService;
    private final ISysRoleService sysRoleService;
    private final ISysMenuService menuService;
    private final AiConfig aiConfig;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        String nickname = registerBody.getNickName();
        if (nickname == null || nickname.equals("")) {
            nickname = username;
        }
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关 由于小程序注册，不限制图形验证码
//        if (captchaEnabled && registerBody.getChannelShareId() == null) {
//            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
//        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(nickname);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);
        sysUser.setPhonenumber(username);
        sysUser.setAvatar(aiConfig.getPreviewUrl() + "/static/avatar.png");

        if (!userService.checkUserNameUnique(sysUser)) {
            throw new UserException("user.register.save.error", username);
        }
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private void recordLogininfor(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

    public void insertRole(Long companyId, Long userId) {
        SysRole defaultManagerRole = new SysRole();
        defaultManagerRole.setRoleId(null);
        defaultManagerRole.setCompanyId(companyId);
        defaultManagerRole.setRoleName("企业管理员");
        defaultManagerRole.setRoleKey("company_manager");

        List<SysMenu> menus = menuService.list(new QueryWrapper<SysMenu>().eq("is_company", 1));
        List<Long> menuIds = new ArrayList<>();
        if (menus != null && !menus.isEmpty()) {
            for (SysMenu menu : menus) {
                menuIds.add(menu.getMenuId());
            }
            defaultManagerRole.setMenuIds(menuIds.toArray(new Long[menuIds.size()]));
        }

        sysRoleService.insertRole(defaultManagerRole);
    }
}
