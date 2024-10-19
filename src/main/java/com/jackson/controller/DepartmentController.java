package com.jackson.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("depts")
public class DepartmentController {

    // "hasAuthority('dept:insert')" -> springEl表达式
    // @PreAuthorize("hasAuthority('dept:insert')")
    @GetMapping("/insert")
    public String insert() {
        return "dept-insert";
    }

    // @PreAuthorize("hasAuthority('dept:update')")
    @GetMapping("/update")
    public String update() {
        return "dept-update";
    }

    // @PreAuthorize("hasAuthority('dept:delete')")
    @GetMapping("/delete")
    public String delete() {
        return "dept-delete";
    }

    @GetMapping("/list")
    public String list() {
        return "dept-list";
    }
}