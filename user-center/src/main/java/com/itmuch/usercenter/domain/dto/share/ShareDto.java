package com.itmuch.usercenter.domain.dto.share;


import java.util.Date;
import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareDto {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 发布人id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 分享标题
     */
    private String title;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否原创 0：否 1：是
     */
    @Column(name = "is_original")
    private Boolean isOriginal;

    /**
     * 作者
     */
    private String author;

    /**
     * 封面
     */
    private String cover;

    /**
     * 概要信息
     */
    private String summary;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 下载链接
     */
    @Column(name = "download_url")
    private String downloadUrl;

    /**
     * 下载数
     */
    @Column(name = "buy_count")
    private Integer buyCount;

    /**
     * 是否显示 0：否 1：是
     */
    @Column(name = "show_flag")
    private Integer showFlag;

    /**
     * 审核状态 NOT_YET:待审核  PASSED：审核通过
     */
    @Column(name = "audit_status")
    private String auditStatus;

    /**
     * 审核不通过原因
     */
    private String reson;
}
