package com.jackson.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("depts")
public class DepartmentController_Role {

    // @PreAuthorize("hasRole('dept_mgr')")
    @Secured("ROLE_DEPT_MGR")
    @GetMapping("/insert")
    public String insert(){
        return "dept-insert";
    }

    @Secured("ROLE_DEPT_MGR") // 使用Secured注解时角色头部会自动添加ROLE_
    // @PreAuthorize("hasRole('dept_mgr')")
    @GetMapping("/update")
    public String update(){
        return "dept-update";
    }

    @Secured("ROLE_HR")
    // @PreAuthorize("hasRole('hr')")
    @GetMapping("/delete")
    public String delete(){
        return "dept-delete";
    }

    @Secured("ROLE_HR")
    // @PreAuthorize("hasRole('hr')")
    @GetMapping("/list")
    public String list(){
        return "dept-list";
    }
}