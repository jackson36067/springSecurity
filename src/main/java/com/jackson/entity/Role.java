package com.jackson.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("roles")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "role_name")
    private String roleName;
    @TableField(exist = false)
    private List<Employee> employees;
}
