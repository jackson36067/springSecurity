package com.jackson.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jackson.entity.Employee;
import com.jackson.entity.Employee_Role;
import com.jackson.mapper.EmployeeMapper;
import com.jackson.mapper.EmployeeRoleMapper;
import com.jackson.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private EmployeeRoleMapper employeeRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("username", username));
        if (employee == null) {
            throw new UsernameNotFoundException(username);
        }
        List<Employee_Role> employeeRoleList = employeeRoleMapper.selectList(new QueryWrapper<Employee_Role>().eq("e_id", employee.getId()));
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = null;
        if (!employeeRoleList.isEmpty()) {
            List<Long> roleIdList = employeeRoleList.stream().map(Employee_Role::getRId).toList();
            simpleGrantedAuthorities = roleMapper.selectBatchIds(roleIdList).stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
        }
        return new User(
                employee.getUsername(),
                employee.getPassword(),
                simpleGrantedAuthorities
        ); //权限列表
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
