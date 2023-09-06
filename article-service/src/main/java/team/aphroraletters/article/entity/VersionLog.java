package team.aphroraletters.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
 * @since 2023-01-12
 */
@Getter
@Setter
@TableName("version_log")
@ApiModel(value = "VersionLog对象", description = "")
public class VersionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("版本日志")
    private String log;

    @ApiModelProperty("发布时间")
    private LocalDateTime createBy;

    /**
     * 获取格式化后的时间字符串
     *
     * @return String
     */
    public String getTime() {

        return this.getCreateBy().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
