package com.ngnis.walle.datasource.db.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserDO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 是否删除 0：否 1：是
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 加密后的登录密码
     */
    private String password;

    /**
     * 接口调用时使用的ak
     */
    @Column(name = "access_key")
    private String accessKey;

    /**
     * 接口调用时使用的sk
     */
    @Column(name = "secret_key")
    private String secretKey;
}