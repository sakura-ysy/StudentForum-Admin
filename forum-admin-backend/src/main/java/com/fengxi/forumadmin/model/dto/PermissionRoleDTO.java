package com.fengxi.forumadmin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionRoleDTO implements Serializable {
    private static final long serialVersionUID = -5385731223218068645L;

    private String roleId;

    private String permId;
}
