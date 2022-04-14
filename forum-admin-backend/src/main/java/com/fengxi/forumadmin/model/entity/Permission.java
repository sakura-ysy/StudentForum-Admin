package com.fengxi.forumadmin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@TableName("permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {

    private static final long serialVersionUID = -5672123559189857662L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("perm_name")
    private String permName;

    @TableField("perm_tag")
    private String permTag;

    @TableField("url")
    private String url;
}

