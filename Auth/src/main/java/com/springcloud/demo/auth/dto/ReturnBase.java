package com.springcloud.demo.auth.dto;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class) // 应用自动赋予默认值功能，例如CreatedDate
@Table(name = "base")
public class ReturnBase {

    private Status status = Status.SUCCESS;

    private ErrorCode errorCode = ErrorCode.SUCCESS;

    private String errorMessage = "";

    static public ReturnBase success() {
        return new ReturnBase();
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
}