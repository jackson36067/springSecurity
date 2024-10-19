package com.jackson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jackson.dto.EmployeeDTO;
import com.jackson.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    void saveEmployee(EmployeeDTO employeeDTO);
}
