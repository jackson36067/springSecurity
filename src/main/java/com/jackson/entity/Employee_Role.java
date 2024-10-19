package com.jackson.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("employee_role")
public class Employee_Role {
    @TableField(value = "e_id")
    private Long eId;
    @TableField(value = "r_id")
    private Long rId;
}
