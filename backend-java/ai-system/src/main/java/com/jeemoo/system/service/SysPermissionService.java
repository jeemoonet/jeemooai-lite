package com.jeemoo.system.service;

import com.jeemoo.common.core.domain.entity.SysUser;
import com.jeemoo.common.enums.UserType;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ai
 */
@RequiredArgsConstructor
@Service
public class SysPermissionService {

    private final ISysRoleService roleService;
    private final ISysMenuService menuService;
    private final IAiCompanyService companyService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        AiCompanyVo companyVo = companyService.queryById(user.getCompanyId());
        if (user.getUserType().equals(UserType.APP_USER.getUserType())) {
            if (!Objects.equals(companyVo.getUserId(), user.getUserId())){
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            } else {
                perms.addAll(menuService.selectAllCompanyPerms());
            }
        } else if (user.getUserType().equals(UserType.SYS_USER.getUserType())) {
            // 管理员拥有所有权限
            if (user.isAdmin()) {
                perms.add("*:*:*");
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
