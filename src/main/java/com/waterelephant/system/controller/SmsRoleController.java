package com.waterelephant.system.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.system.base.BaseController;
import com.waterelephant.system.dto.SmsRoleDto;
import com.waterelephant.system.framework.web.domain.AjaxResult;
import com.waterelephant.system.framework.web.domain.Ztree;
import com.waterelephant.system.framework.web.page.TableDataInfo;
import com.waterelephant.system.service.SmsMenuService;
import com.waterelephant.system.service.SmsRoleService;

@Controller
@RequestMapping("/system/role")
public class SmsRoleController extends BaseController {

	 private String prefix = "system/role";

    @Autowired
    private SmsRoleService smsRoleService;
    
    @Autowired
    private SmsMenuService smsMenuService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role(){
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        startPage();
        List<SmsRoleDto> list = smsRoleService.selectRoleList();
        return getDataTable(list);
    }
    
    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", smsRoleService.selectRoleById(roleId));
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody JSONObject params)
    {
    	Long roleId = params.getLong("roleId");
    	List<Long> menuIds = params.getJSONArray("menuIds").toJavaList(Long.class);
        return toAjax(smsMenuService.updateRoleMenu(roleId,menuIds));
    }
    
    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleMenuTreeData")
    @ResponseBody
    public List<Ztree> roleMenuTreeData(Long roleId)
    {
        List<Ztree> ztrees = smsMenuService.roleMenuTreeData(roleId);
        return ztrees;
    }
    
}
