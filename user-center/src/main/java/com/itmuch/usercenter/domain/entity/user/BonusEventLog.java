package com.itmuch.usercenter.domain.entity.user;

import lombok.*;

import java.util.Date;
import javax.persistence.*;
@Getter
@Setter
@ToString
@Builder
@Table(name = "bonus_event_log")
public class BonusEventLog {
    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * user.id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 积分操作值
     */
    private Integer value;

    /**
     * 发生的事件
     */
    private String event;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 描述
     */
    private String description;

}