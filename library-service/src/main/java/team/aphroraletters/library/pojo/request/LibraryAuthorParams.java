package team.aphroraletters.library.pojo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LibraryAuthorParams {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("作者名")
    private String characterName;

    @ApiModelProperty("作者头像图片地址")
    private String picUrl;

    @ApiModelProperty("作者简介")
    private String introduction;

}
