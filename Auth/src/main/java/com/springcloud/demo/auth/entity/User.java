package com.springcloud.demo.auth.entity;

import io.swagger.annotations.Api;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Api(tags = "用户")
@Data
@Entity
@Table(name = "tb_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"})})
public class User extends Base {

    @NotNull
    @Size(min = 3, max = 64)
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '用户名'")
    private String userName;
    @NotNull
    @Column(length = 64, columnDefinition = "varchar(64) COMMENT '密码'")
    private String passWord;
}

