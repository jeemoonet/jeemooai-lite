package com.jeemoo.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.RandomUtil;
import com.jeemoo.common.annotation.Log;
import com.jeemoo.common.core.domain.PageQuery;
import com.jeemoo.common.core.domain.R;
import com.jeemoo.common.core.domain.entity.SysMenu;
import com.jeemoo.common.core.domain.entity.SysRole;
import com.jeemoo.common.core.domain.entity.SysUser;
import com.jeemoo.common.core.domain.model.LoginUser;
import com.jeemoo.common.core.page.TableDataInfo;
import com.jeemoo.common.enums.BusinessType;
import com.jeemoo.common.helper.LoginHelper;
import com.jeemoo.system.domain.bo.AiDocumentBo;
import com.jeemoo.system.domain.vo.AiCompanyVo;
import com.jeemoo.system.service.IAiCompanyService;
import com.jeemoo.system.service.ISysMenuService;
import com.jeemoo.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/role")
public class ApiRoleController {

    private final ISysRoleService roleService;
    private final ISysMenuService menuService;
    private final IAiCompanyService companyService;

    @Log(title = "角色列表", businessType = BusinessType.API)
    @GetMapping("/list")
    public R<Object> list() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        SysRole role = new SysRole();
        role.setCompanyId(loginUser.getCompanyId());
        List<SysRole> list = roleService.selectRoleList(role);
        return R.ok(list);
    }

    @Log(title = "角色分页列表", businessType = BusinessType.API)
    @GetMapping("/page")
    public TableDataInfo<SysRole> page(SysRole role, PageQuery pageQuery) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        role.setCompanyId(loginUser.getCompanyId());
        return roleService.selectPageRoleList(role, pageQuery);
    }

    @Log(title = "角色详情", businessType = BusinessType.API)
    @GetMapping(value = "/info")
    public R<SysRole> getInfo(Long roleId) {
        SysRole role = roleService.selectRoleById(roleId);
        List<Long> menuIds = menuService.selectMenuListByRoleId(roleId);
        role.setMenuIds(menuIds.toArray(new Long[menuIds.size()]));
        return R.ok(role);
    }

    @Log(title = "角色管理", businessType = BusinessType.API)
    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody SysRole role) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        role.setCompanyId(loginUser.getCompanyId());
        role.setRoleKey(RandomUtil.randomStringUpper(10));
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色key已存在");
        }
        if (roleService.insertRole(role) > 0) {
            return R.ok();
        } else {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，请联系管理员");
        }
    }

    @Log(title = "角色管理", businessType = BusinessType.API)
    @PostMapping("/update")
    public R<Void> edit(@Validated @RequestBody SysRole role) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        role.setCompanyId(loginUser.getCompanyId());
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色key已存在");
        }

        if (roleService.updateRole(role) > 0) {
            roleService.cleanOnlineUserByRole(role.getRoleId());
            return R.ok();
        }
        return R.fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("")
    public R<Void> remove(Long roleId) {
        List<SysUser> users = roleService.getUserByRoleId(roleId);
        if (!users.isEmpty()) {
            return R.fail("该角色已被使用无法删除");
        }
        if (roleService.deleteRoleById(roleId) > 0) {
            return R.ok();
        } else {
            return R.fail();
        }
    }


    @GetMapping("/menuTree")
    public R<Object> menuTree(Long roleId) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        AiCompanyVo company = companyService.queryById(loginUser.getCompanyId());
        List<SysMenu> menus = menuService.selectCompanyMenuList(loginUser, company);
        Map<String, Object> ajax = new HashMap<>();
        if (roleId != null && roleId != 0){
            ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        }
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return R.ok(ajax);
    }
}
