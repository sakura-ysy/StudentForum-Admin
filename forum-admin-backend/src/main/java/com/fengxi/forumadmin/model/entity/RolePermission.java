package com.fengxi.forumadmin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("role_permission")
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("role_id")
    private String roleId;

    @TableField("perm_id")
    private String permId;

}
