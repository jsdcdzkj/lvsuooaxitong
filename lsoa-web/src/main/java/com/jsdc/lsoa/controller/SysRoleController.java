package com.jsdc.lsoa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.lsoa.log.LogInfo;
import com.jsdc.lsoa.model.SysRole;
import com.jsdc.lsoa.service.SysRoleMenuService;
import com.jsdc.lsoa.service.SysRoleService;
import com.jsdc.lsoa.vo.ResultInfo;
import com.jsdc.lsoa.vo.SysRoleMenuVo;
import com.jsdc.lsoa.vo.SysRolePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/sysrole")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @PostMapping("/page")
    @ApiOperation("分页")
    public ResultInfo<Page<SysRole>> page(@RequestBody SysRolePageVO vo) {
        Page<SysRole> page = roleService.page(vo);
        return ResultInfo.success(page);
    }

    @PostMapping("/list")
    @ApiOperation("列表（不分页）")
    public ResultInfo<List<SysRole>> list() {
        List<SysRole> list = roleService.list();
        return ResultInfo.success(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增或修改")
    public ResultInfo<Void> save(@RequestBody SysRole entity) {
        roleService.save(entity);
        return ResultInfo.success();
    }

    @PostMapping("/delete")
    @LogInfo(type = "删除", mode = "权限管理")
    @ApiOperation("删除")
    public ResultInfo<Void> delete(@RequestBody SysRole entity) {
        roleService.delete(entity);
        return ResultInfo.success();
    }


    @PostMapping("/saveRoleMenu")
    @ApiOperation("角色赋权")
    @LogInfo(type = "角色赋权", mode = "权限管理")
    public ResultInfo<Void> saveRoleMenu(@RequestBody SysRoleMenuVo entity) {
        roleMenuService.saveRoleMenu(entity);
        return ResultInfo.success();
    }

    @GetMapping("/getSysRoleMenuList")
    @ApiOperation("查询角色权限")
    public ResultInfo getSysRoleMenuList(SysRoleMenuVo entity){
        return ResultInfo.success(roleMenuService.getSysRoleMenuList(entity));
    }


}
