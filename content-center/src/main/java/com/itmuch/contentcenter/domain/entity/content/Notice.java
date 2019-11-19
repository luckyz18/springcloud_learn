package com.itmuch.contentcenter.domain.entity.content;

import java.util.Date;
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notice")
public class Notice {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 内容

     */
    private String content;

    /**
     * 是否显示 0：否 1：是
     */
    @Column(name = "show_flag")
    private Boolean showFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}