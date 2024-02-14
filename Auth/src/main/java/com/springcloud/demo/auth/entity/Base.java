package com.springcloud.demo.auth.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class) // 应用自动赋予默认值功能，例如CreatedDate
@Table(name = "base")
public class Base {
    @Id
    @GeneratedValue
    private long id;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME COMMENT '最后修改时间'")
    private Date modifyTime;

    @CreatedDate
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
}
