package com.itmuch.contentcenter.domain.dto.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投稿信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequestDTO {
    /**
     * 作者
     */
    private String author;

    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * 是否原创
     */
    private boolean isOriginal;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 简介
     */
    private String summary;
    /**
     * 标题
     */
    private String title;


}
