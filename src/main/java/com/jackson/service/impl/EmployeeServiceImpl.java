package com.jackson.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.dto.EmployeeDTO;
import com.jackson.entity.Employee;
import com.jackson.entity.Employee_Role;
import com.jackson.entity.Role;
import com.jackson.mapper.EmployeeMapper;
import com.jackson.mapper.EmployeeRoleMapper;
import com.jackson.mapper.RoleMapper;
import com.jackson.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private EmployeeRoleMapper employeeRoleMapper;

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(employeeDTO.getPassword());
        Employee employee = BeanUtil.copyProperties(employeeDTO, Employee.class);
        employee.setPassword(encodePassword);
        this.save(employee);
        employeeDTO.getRoleNameList().forEach(roleName -> {
            Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", roleName));
            Employee_Role employeeRole = Employee_Role.builder()
                    .eId(employee.getId())
                    .rId(role.getId())
                    .build();
            employeeRoleMapper.insert(employeeRole);
        });
    }
}
