package team.aphroraletters.article.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @author @TfiyuenLau
 * @since 2023-01-12
 */
@Getter
@Setter
@ApiModel(value = "Announcement对象", description = "")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("公告发布者")
    private String publisher;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String content;

    @ApiModelProperty("发布时间")
    private LocalDateTime createBy;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Boolean isEffective;

    /**
     * 获取格式化后的时间字符串
     *
     * @return String
     */
    public String getTime() {

        return this.getCreateBy().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

}
