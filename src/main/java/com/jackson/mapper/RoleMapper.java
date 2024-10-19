package com.jackson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jackson.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
