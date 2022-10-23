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
@TableName("sys_log")
@ApiModel(value = "SysLog对象", description = "")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("操作地址的IP")
    private String ip;

    @ApiModelProperty("操作时间")
    private LocalDateTime createBy;

    @ApiModelProperty("操作内容")
    private String remark;

    @ApiModelProperty("操作的参数")
    private String operateParam;

    @ApiModelProperty("操作的访问地址")
    private String operateUrl;

    @ApiModelProperty("操作的浏览器")
    private String operateBy;

    @ApiModelProperty("是否有效")
    private Boolean isEffective;
}
