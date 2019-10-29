package com.itmuch.usercenter.domain.entity.user;

import java.util.Date;
import javax.persistence.*;

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

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取user.id
     *
     * @return user_id - user.id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置user.id
     *
     * @param userId user.id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取积分操作值
     *
     * @return value - 积分操作值
     */
    public Integer getValue() {
        return value;
    }

    /**
     * 设置积分操作值
     *
     * @param value 积分操作值
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * 获取发生的事件
     *
     * @return event - 发生的事件
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置发生的事件
     *
     * @param event 发生的事件
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
}