package com.marxist.leftwing_community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Getter
@Setter
@TableName("tbl_article_info")
@ApiModel(value = "TblArticleInfo对象", description = "")
public class TblArticleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章简介，默认100个汉字以内")
    private String summary;

    @ApiModelProperty("文章是否置顶，0为否，1为是")
    private Boolean isTop;

    @ApiModelProperty("文章访问量")
    private Integer traffic;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("修改日期")
    private LocalDateTime modifiedBy;
}
