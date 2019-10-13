package com.itmuch.usercenter.domain.entity.user;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user")
public class User {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 微信id
     */
    @Column(name = "wx_id")
    private String wxId;

    /**
     * 微信昵称
     */
    @Column(name = "wx_nickname")
    private String wxNickname;

    private String roles;

    /**
     * 头像地址
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private Integer bonus;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取微信id
     *
     * @return wx_id - 微信id
     */
    public String getWxId() {
        return wxId;
    }

    /**
     * 设置微信id
     *
     * @param wxId 微信id
     */
    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    /**
     * 获取微信昵称
     *
     * @return wx_nickname - 微信昵称
     */
    public String getWxNickname() {
        return wxNickname;
    }

    /**
     * 设置微信昵称
     *
     * @param wxNickname 微信昵称
     */
    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    /**
     * @return roles
     */
    public String getRoles() {
        return roles;
    }

    /**
     * @param roles
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * 获取头像地址
     *
     * @return avatar_url - 头像地址
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置头像地址
     *
     * @param avatarUrl 头像地址
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return bonus
     */
    public Integer getBonus() {
        return bonus;
    }

    /**
     * @param bonus
     */
    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }
}