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
 * 这张表用来保存题图url，每一篇文章都应该有题图
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Getter
@Setter
@TableName("tbl_article_picture")
@ApiModel(value = "TblArticlePicture对象", description = "这张表用来保存题图url，每一篇文章都应该有题图")
public class TblArticlePicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("对应文章id")
    private Long articleId;

    @ApiModelProperty("图片url")
    private String pictureUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime modifiedBy;

    @ApiModelProperty("是否有效")
    private Boolean isEffective;
}
